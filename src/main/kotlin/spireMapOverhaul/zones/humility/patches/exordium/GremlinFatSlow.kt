package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.exordium.GremlinFat
import com.megacrit.cardcrawl.powers.SlowPower
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.patches.utils.addPreBattleAction

class GremlinFatSlow {
    @SpirePatch(
        clz = GremlinFat::class,
        method = SpirePatch.CONSTRUCTOR
    )
    class AddSlowAndMoreHP {
        companion object {
            @JvmStatic
            fun Postfix(__instance: GremlinFat, x: Float, y: Float) {
                if (HumilityZone.isNotInZone()) return

                __instance.currentHealth *= 3
                __instance.maxHealth = __instance.currentHealth
            }

            @JvmStatic
            fun Raw(ctBehavior: CtBehavior) {
                ctBehavior.addPreBattleAction(Companion::doPreBattleAction)
            }

            @JvmStatic
            fun doPreBattleAction(__instance: GremlinFat) {
                if (HumilityZone.isNotInZone()) return

                AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, SlowPower(__instance, 0)))
            }
        }
    }
}
