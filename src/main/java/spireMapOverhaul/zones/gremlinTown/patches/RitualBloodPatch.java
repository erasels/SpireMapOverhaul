package spireMapOverhaul.zones.gremlinTown.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;

import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

@SpirePatch(
        clz = CorruptHeart.class,
        method = "takeTurn"
)
public class RitualBloodPatch {
    @SpirePrefixPatch
    public static SpireReturn heartStopper(CorruptHeart __instance) {
        if (__instance.nextMove == 3) {
            atb(new VFXAction(new HeartMegaDebuffEffect()));
            atb(new ApplyPowerAction(adp(), __instance, new WeakPower(adp(), 2, true), 2));
            atb(new ApplyPowerAction(adp(), __instance, new FrailPower(adp(), 2, true), 2));
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }
}
