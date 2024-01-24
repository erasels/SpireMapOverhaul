package spireMapOverhaul.zones.humility.patches.city

import basemod.ReflectionHacks
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.city.Taskmaster
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone
import kotlin.math.ceil

class TaskmasterScaling {
    @SpirePatch(
        clz = Taskmaster::class,
        method = "getMove"
    )
    class GetMove {
        companion object {
            @JvmStatic
            fun Prefix(__instance: Taskmaster, num: Int, ___SCOURING_WHIP_DMG: Int): SpireReturn<Unit?> {
                if (HumilityZone.isNotInZone()) return SpireReturn.Continue()

                val turns = ceil((__instance.moveHistory.size + 1) / 2f).toInt()
                if (turns <= 1) {
                    __instance.setMove(2, AbstractMonster.Intent.ATTACK_DEBUFF, ___SCOURING_WHIP_DMG)
                } else {
                    __instance.setMove(2, AbstractMonster.Intent.ATTACK_DEBUFF, ___SCOURING_WHIP_DMG, turns, true)
                }
                return SpireReturn.Return(null)
            }
        }
    }

    @SpirePatch(
        clz = Taskmaster::class,
        method = "takeTurn"
    )
    class TakeTurn {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class
            )
            fun Insert(__instance: Taskmaster) {
                if (HumilityZone.isNotInZone()) return

                val intentMultiAmt = ReflectionHacks.getPrivate(__instance, AbstractMonster::class.java, "intentMultiAmt") as Int
                repeat(intentMultiAmt - 1) {
                    AbstractDungeon.actionManager.addToBottom(
                        DamageAction(
                            AbstractDungeon.player,
                            __instance.damage[1],
                            AbstractGameAction.AttackEffect.SLASH_HEAVY
                        )
                    )
                }
            }
        }

        private class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior?): IntArray {
                val finalMatcher = Matcher.NewExprMatcher(DamageAction::class.java)
                return LineFinder.findInOrder(ctBehavior, finalMatcher)
            }
        }
    }
}
