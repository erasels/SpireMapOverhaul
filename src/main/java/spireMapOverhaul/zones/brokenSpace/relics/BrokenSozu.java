package spireMapOverhaul.zones.brokenSpace.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnUsePotionRelic;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.TextCenteredAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.brokenSpace.actions.BetterTextCenteredAction;

import java.lang.reflect.Method;

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


        if (adp().hasRelic(makeID(ID)) && isInCombat() && !canUseOverridden(p)) {
            flash();
            adp().energy.use(POTION_COST);
            AbstractPotion secondPotion = AbstractDungeon.returnRandomPotion(true);
            secondPotion.use(AbstractDungeon.getRandomMonster());
            addToBot(new BetterTextCenteredAction(adp(), secondPotion.name, 0.5F));

        }
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
    public static class SozuPatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(AbstractPotion __instance) {
            if (AbstractDungeon.player.hasRelic(makeID(ID)) && !canUseOverridden(__instance)) {
                return SpireReturn.Return(canUsePotion(__instance));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = PotionPopUp.class, method = "render")
    public static class SozuEnergyTextPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"label"})
        public static void Insert(PotionPopUp __instance, @ByRef String[] label) {
            if (AbstractDungeon.player.hasRelic(makeID(ID))) {
                label[0] = getEnergyText(__instance, label[0]);
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
}