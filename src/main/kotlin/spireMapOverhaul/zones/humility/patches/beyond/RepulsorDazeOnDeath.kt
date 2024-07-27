package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.beyond.Repulsor
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.patches.utils.addPreBattleAction
import spireMapOverhaul.zones.humility.powers.DeathRattlePower

@SpirePatch(
    clz = Repulsor::class,
    method = SpirePatch.CONSTRUCTOR
)
class RepulsorDazeOnDeath {
    companion object {
        @JvmStatic
        fun Raw(ctBehavior: CtBehavior) {
            ctBehavior.addPreBattleAction(Companion::doPreBattleAction)
        }

        @JvmStatic
        fun doPreBattleAction(__instance: Repulsor) {
            if (HumilityZone.isNotInZone()) return

            AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, DeathRattlePower(__instance, 2), 2))
        }
    }
}
