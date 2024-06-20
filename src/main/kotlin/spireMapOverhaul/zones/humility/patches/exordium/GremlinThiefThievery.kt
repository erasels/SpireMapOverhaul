package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.EscapeAction
import com.megacrit.cardcrawl.actions.common.SetMoveAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.exordium.GremlinThief
import com.megacrit.cardcrawl.powers.ThieveryPower
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect
import javassist.CtBehavior
import javassist.expr.ExprEditor
import javassist.expr.NewExpr
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.patches.utils.addPreBattleAction
import kotlin.math.min

class GremlinThiefThievery {
    @SpirePatch(
        clz = GremlinThief::class,
        method = SpirePatch.CLASS
    )
    class StolenGoldField {
        companion object {
            @JvmField
            val stolenGold: SpireField<Int> = SpireField { 0 }
            @JvmField
            val turnsTaken: SpireField<Int> = SpireField { 0 }
        }
    }

    @SpirePatch(
        clz = GremlinThief::class,
        method = SpirePatch.CONSTRUCTOR
    )
    class AddThievery {
        companion object {
            @JvmStatic
            fun Raw(ctBehavior: CtBehavior) {
                ctBehavior.addPreBattleAction(Companion::doPreBattleAction)
            }

            @JvmStatic
            fun doPreBattleAction(__instance: GremlinThief) {
                if (HumilityZone.isNotInZone()) return

                AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, ThieveryPower(__instance, 10)))
            }
        }
    }

    @SpirePatch(
        clz = GremlinThief::class,
        method = "takeTurn"
    )
    class StealGold {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class
            )
            fun Insert(__instance: GremlinThief) {
                if (HumilityZone.isNotInZone()) return

                StolenGoldField.turnsTaken.set(__instance, StolenGoldField.turnsTaken.get(__instance) + 1)
                AbstractDungeon.actionManager.addToBottom(object : AbstractGameAction() {
                    override fun update() {
                        StolenGoldField.stolenGold.set(__instance, StolenGoldField.stolenGold.get(__instance) + goldToSteal(__instance))
                        isDone = true
                    }
                })
            }

            @JvmStatic
            fun Instrument(): ExprEditor =
                object : ExprEditor() {
                    override fun edit(e: NewExpr) {
                        if (HumilityZone.hasHumilityMod()) return

                        if (e.className == DamageAction::class.qualifiedName) {
                            e.replace(
                                "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                        "\$_ = new ${DamageAction::class.qualifiedName}($1, $2, ${StealGold::class.qualifiedName}.goldToSteal(this));" +
                                        "\$_.attackEffect = $3;" +
                                        "} else {" +
                                        "\$_ = \$proceed(\$\$);" +
                                        "}"
                            )
                        }
                    }
                }

            @JvmStatic
            fun goldToSteal(__instance: GremlinThief): Int {
                val thieveryAmt = __instance.getPower(ThieveryPower.POWER_ID)?.amount ?: 0
                return min(thieveryAmt, AbstractDungeon.player.gold)
            }
        }

        private class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior?): IntArray {
                val finalMatcher = Matcher.NewExprMatcher(DamageAction::class.java)
                return LineFinder.findInOrder(ctBehavior, finalMatcher)
            }
        }
    }

    @SpirePatch(
        clz = GremlinThief::class,
        method = "die"
    )
    class ReturnGold {
        companion object {
            @JvmStatic
            fun Postfix(__instance: GremlinThief) {
                val stolenGold = StolenGoldField.stolenGold.get(__instance)
                if (stolenGold > 0) {
                    AbstractDungeon.getCurrRoom().addStolenGoldToRewards(stolenGold)
                }
            }
        }
    }

    @SpirePatch(
        clz = GremlinThief::class,
        method = "takeTurn"
    )
    class EscapeWithGold {
        companion object {
            @JvmStatic
            fun Postfix(__instance: GremlinThief) {
                if (HumilityZone.isNotInZone()) return

                if (StolenGoldField.turnsTaken.get(__instance) >= 3) {
                    AbstractDungeon.actionManager.addToBottom(SetMoveAction(__instance, 98, AbstractMonster.Intent.ESCAPE))
                }

                if (__instance.nextMove == 98.toByte()) {
                    AbstractDungeon.actionManager.addToBottom(VFXAction(SmokeBombEffect(__instance.hb.cX, __instance.hb.cY)))
                    AbstractDungeon.actionManager.addToBottom(EscapeAction(__instance))
                    AbstractDungeon.actionManager.addToBottom(SetMoveAction(__instance, 98, AbstractMonster.Intent.ESCAPE))
                }
            }
        }
    }
}

