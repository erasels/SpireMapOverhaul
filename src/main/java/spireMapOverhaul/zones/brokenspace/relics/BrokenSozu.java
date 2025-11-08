package spireMapOverhaul.zones.brokenspace.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnUsePotionRelic;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.brokenspace.actions.BetterTextCenteredAction;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.isInCombat;

public class BrokenSozu extends BrokenRelic implements BetterOnUsePotionRelic {
    public static final String ID = "BrokenSozu";
    public static final int POTION_COST = 1;
    public static final String[] DESCRIPTIONS = CardCrawlGame.languagePack.getRelicStrings(makeID(ID)).DESCRIPTIONS;

    public BrokenSozu() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Sozu.ID);
    }

    @Override
    public void betterOnUsePotion(AbstractPotion p) {
        if (adp().hasRelic(makeID(ID)) && isInCombat() && isValidForBrokenSozu(p)) {
            flash();
            adp().energy.use(POTION_COST);
            AbstractPotion secondPotion = null;
            int i = 0;
            // Get a potion that can be used (which excludes things like Fairy in a Bottle) and excludes Smokebomb
            while (secondPotion == null || !secondPotion.canUse() || SmokeBomb.POTION_ID.equals(secondPotion.ID)) {
                if (i > 100) {
                    // Just in case some other mod makes every potion unusable (or we roll Fairy in a Bottle every time)
                    SpireAnniversary6Mod.logger.info("No usable potions found for Broken Sozu");
                    return;
                }
                secondPotion = AbstractDungeon.returnRandomPotion(true);
                i++;
            }
            secondPotion.use(AbstractDungeon.getRandomMonster());
            addToBot(new BetterTextCenteredAction(adp(), secondPotion.name, 0.5F));
            incrementPotionsStat(1);
        }
    }

    private static boolean isValidForBrokenSozu(AbstractPotion p) {
        // This validity check requires canUse not be overridden because our patch below only accounts for the method on
        // AbstractPotion itself, plus special cases for base game potions that override it. We could get modded potions
        // that override canUse to work by writing a dynamic patch, but we haven't done that because it's pretty niche.
        // We omit Fairy in a Bottle here because it can't be used.
        return p instanceof FruitJuice
                || p instanceof BloodPotion
                || p instanceof EntropicBrew
                || p instanceof SmokeBomb
                || !canUseOverridden(p);
    }

    private static boolean canUseOverridden(AbstractPotion p) {
        try {
            Method method = p.getClass().getMethod("canUse");
            if (!method.getDeclaringClass().equals(AbstractPotion.class)) {
                return true;
            }
        } catch (NoSuchMethodException e) {
            SpireAnniversary6Mod.logger.info("BrokenSozu: Potion " + p.name + " does not have a canUse method.");

        }
        return false;
    }

    @SpirePatch2(clz = AbstractPotion.class, method = "canUse")
    @SpirePatch2(clz = FruitJuice.class, method = "canUse")
    @SpirePatch2(clz = BloodPotion.class, method = "canUse")
    @SpirePatch2(clz = EntropicBrew.class, method = "canUse")
    @SpirePatch2(clz = SmokeBomb.class, method = "canUse")
    public static class SozuPatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(AbstractPotion __instance) {
            if (AbstractDungeon.player.hasRelic(makeID(ID)) && !canUsePotion(__instance)) {
                return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = PotionPopUp.class, method = "render")
    public static class SozuEnergyTextPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"label"})
        public static void Insert(PotionPopUp __instance, @ByRef String[] label) {
            if (AbstractDungeon.player.hasRelic(makeID(ID))) {
                AbstractPotion potion = ReflectionHacks.getPrivate(__instance, PotionPopUp.class, "potion");
                if (!(potion instanceof FairyPotion)) {
                    label[0] = getEnergyText(__instance, label[0]);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCenteredWidth");
                return new int[]{LineFinder.findInOrder(ctBehavior, matcher)[0] - 1};
            }
        }

    }

    public static String getEnergyText(PotionPopUp popup, String original) {
        if (AbstractDungeon.player.hasRelic(makeID(ID))) {
            return original + ": " + POTION_COST + " " + DESCRIPTIONS[1];

        }
        return original;
    }



    public static boolean canUsePotion(AbstractPotion p) {
        return EnergyPanel.totalCount >= POTION_COST;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String POTIONS_STAT = "potions";

    public String getStatsDescription() {
        return DESCRIPTIONS[2].replace("{0}", stats.get(POTIONS_STAT) + "");
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float potions = stats.get(POTIONS_STAT);
        String potionsPerCombat = format.format(potions / Math.max(totalCombats, 1));
        return getStatsDescription() + DESCRIPTIONS[3].replace("{0}", potionsPerCombat);
    }

    public void resetStats() {
        stats.put(POTIONS_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(POTIONS_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(POTIONS_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementPotionsStat(int amount) {
        stats.put(POTIONS_STAT, stats.getOrDefault(POTIONS_STAT, 0) + amount);
    }
}