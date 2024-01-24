package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.beyond.SpireGrowth
import com.megacrit.cardcrawl.powers.ConstrictedPower
import javassist.CtBehavior
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess
import javassist.expr.MethodCall
import spireMapOverhaul.zones.humility.HumilityZone

class SpireGrowthConstrictAttack {
    @SpirePatch(
        clz = SpireGrowth::class,
        method = "takeTurn"
    )
    class TakeTurn {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class
            )
            fun Insert(__instance: SpireGrowth) {
                if (HumilityZone.isNotInZone()) return

                AbstractDungeon.actionManager.addToBottom(DamageAction(AbstractDungeon.player, __instance.damage[0], AbstractGameAction.AttackEffect.BLUNT_HEAVY))
            }
        }

        private class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior?): IntArray {
                val finalMatcher = Matcher.NewExprMatcher(ConstrictedPower::class.java)
                return LineFinder.findAllInOrder(ctBehavior, finalMatcher)
            }
        }
    }

    @SpirePatch(
        clz = SpireGrowth::class,
        method = "getMove"
    )
    class ChangeIntent {
        companion object {
            @JvmStatic
            fun Instrument(): ExprEditor =
                object : ExprEditor() {
                    private var constrict = false

                    override fun edit(f: FieldAccess) {
                        if (f.isReader && f.fieldName == "STRONG_DEBUFF") {
                            constrict = true
                        }
                    }

                    override fun edit(m: MethodCall) {
                        if (HumilityZone.hasHumilityMod()) return

                        if (constrict && m.methodName == "setMove") {
                            m.replace(
                                "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                        "setMove($1, ${AbstractMonster.Intent::class.qualifiedName}.ATTACK_DEBUFF, ((${DamageInfo::class.qualifiedName})damage.get(0)).base);" +
                                        "} else {" +
                                        "\$_ = \$proceed(\$\$);" +
                                        "}"
                            )
                        }
                        constrict = false
                    }
                }
        }
    }
}
