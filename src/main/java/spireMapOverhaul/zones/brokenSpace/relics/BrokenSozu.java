package spireMapOverhaul.zones.brokenSpace.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnUsePotionRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.lang.reflect.Method;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

public class BrokenSozu extends BrokenRelic implements BetterOnUsePotionRelic {
    public static final String ID = "BrokenSozu";
    public static final int POTION_COST = 1;

    public BrokenSozu() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Sozu.ID);
    }

    @Override
    public void betterOnUsePotion(AbstractPotion p) {


        if (adp().hasRelic(makeID(ID)) && isInCombat() && !canUseOverridden(p)) {
            adp().energy.use(POTION_COST);
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

    public static boolean canUsePotion(AbstractPotion p) {


        if (EnergyPanel.totalCount > POTION_COST) {
            return true;
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + POTION_COST + DESCRIPTIONS[1];
    }
}