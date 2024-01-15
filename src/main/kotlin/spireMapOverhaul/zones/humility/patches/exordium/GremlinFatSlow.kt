package spireMapOverhaul.zones.humility.patches.exordium

import spireMapOverhaul.zones.humility.patches.utils.addPreBattleAction
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.exordium.GremlinFat
import com.megacrit.cardcrawl.powers.SlowPower
import javassist.CtBehavior

class GremlinFatSlow {
    @SpirePatch(
        clz = GremlinFat::class,
        method = SpirePatch.CONSTRUCTOR
    )
    class AddSlowAndMoreHP {
        companion object {
            @JvmStatic
            fun Postfix(__instance: GremlinFat, x: Float, y: Float) {
                __instance.currentHealth *= 3
                __instance.maxHealth = __instance.currentHealth
            }

            @JvmStatic
            fun Raw(ctBehavior: CtBehavior) {
                ctBehavior.addPreBattleAction(Companion::doPreBattleAction)
            }

            @JvmStatic
            fun doPreBattleAction(__instance: GremlinFat) {
                AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, SlowPower(__instance, 0)))
            }
        }
    }
}
