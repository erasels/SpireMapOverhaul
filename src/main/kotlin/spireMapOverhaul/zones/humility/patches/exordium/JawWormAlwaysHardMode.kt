package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.ByRef
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.exordium.JawWorm
import spireMapOverhaul.zones.humility.powers.BetterLifeLinkPower

class JawWormAlwaysHardMode {
    @SpirePatch2(
        clz = JawWorm::class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = [Float::class, Float::class, Boolean::class]
    )
    class SkipFirstMove {
        companion object {
            @JvmStatic
            fun Postfix(@ByRef(type = "boolean") ___firstMove: Array<Boolean>) {
                ___firstMove[0] = false
            }
        }
    }

    @SpirePatch2(
        clz = JawWorm::class,
        method = "usePreBattleAction"
    )
    class PreBattle {
        companion object {
            @JvmStatic
            fun Prefix(__instance: JawWorm, @ByRef(type = "boolean") ___hardMode: Array<Boolean>) {
                if (___hardMode[0]) {
                    AbstractDungeon.actionManager.addToBottom(
                        ApplyPowerAction(
                            __instance,
                            __instance,
                            BetterLifeLinkPower(__instance)
                        )
                    )
                }
                ___hardMode[0] = true
            }
        }
    }
}
