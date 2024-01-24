package spireMapOverhaul.zones.gremlinTown.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;
import spireMapOverhaul.zones.gremlinTown.powers.HeartImpairmentPower;

import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

@SpirePatch(
        clz = CorruptHeart.class,
        method = "takeTurn"
)
public class RitualBloodPatch {
    @SpirePrefixPatch
    public static SpireReturn heartStopper(CorruptHeart __instance) {
        if (__instance.nextMove == 3 && __instance.hasPower(HeartImpairmentPower.POWER_ID)) {
            atb(new VFXAction(new HeartMegaDebuffEffect()));
            atb(new WaitAction(0.1f));
            atb(new WaitAction(0.1f));
            atb(new ApplyPowerAction(adp(), __instance, new WeakPower(adp(), 2, true), 2));
            atb(new ApplyPowerAction(adp(), __instance, new FrailPower(adp(), 2, true), 2));
            atb(new WaitAction(0.1f));
            atb(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, false, false, (float) Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Slimed(), 1, true, false, false, (float)Settings.WIDTH * 0.5F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, false, false, (float)Settings.WIDTH * 0.7F, (float)Settings.HEIGHT / 2.0F));
            atb(new WaitAction(0.1f));
            atb(new WaitAction(0.1f));

            atb(new RollMoveAction(__instance));
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }
}
