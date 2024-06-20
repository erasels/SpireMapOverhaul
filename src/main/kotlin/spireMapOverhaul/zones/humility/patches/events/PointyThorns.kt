package spireMapOverhaul.zones.humility.patches.events

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.city.BanditPointy
import com.megacrit.cardcrawl.powers.ThornsPower
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.patches.utils.addPreBattleAction

@SpirePatch(
    clz = BanditPointy::class,
    method = SpirePatch.CONSTRUCTOR
)
class PointyThorns {
    companion object {
        @JvmStatic
        fun Raw(ctBehavior: CtBehavior) {
            ctBehavior.addPreBattleAction(Companion::doPreBattleAction)
        }

        @JvmStatic
        fun doPreBattleAction(__instance: BanditPointy) {
            if (HumilityZone.isNotInZone()) return

            AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, ThornsPower(__instance, 2), 2))
        }
    }
}
