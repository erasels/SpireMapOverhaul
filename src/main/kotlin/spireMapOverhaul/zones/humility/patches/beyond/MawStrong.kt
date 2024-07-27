package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.ByRef
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.beyond.Maw
import com.megacrit.cardcrawl.powers.StrengthPower
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.patches.utils.addPreBattleAction

class MawStrong {
    @SpirePatch(
        clz = Maw::class,
        method = "getMove"
    )
    class AlwaysSlam {
        companion object {
            @JvmStatic
            fun Prefix(__instance: Maw, @ByRef num: Array<Int>) {
                if (HumilityZone.isNotInZone()) return

                if (lastMove(__instance, 2)) {
                    num[0] = 51
                }
            }

            private fun lastMove(__instance: Maw, move: Byte): Boolean =
                __instance.moveHistory.isNotEmpty() && __instance.moveHistory.last() == move
        }
    }

    @SpirePatch(
        clz = Maw::class,
        method = SpirePatch.CONSTRUCTOR
    )
    class StartingStrength {
        companion object {
            @JvmStatic
            fun Raw(ctBehavior: CtBehavior) {
                ctBehavior.addPreBattleAction(Companion::doPreBattleAction)
            }

            @JvmStatic
            fun doPreBattleAction(__instance: Maw) {
                if (HumilityZone.isNotInZone()) return

                AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, StrengthPower(__instance, 2), 2))
            }
        }
    }
}
