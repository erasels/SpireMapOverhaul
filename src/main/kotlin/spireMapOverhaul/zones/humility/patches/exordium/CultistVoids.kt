package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction
import com.megacrit.cardcrawl.actions.common.RollMoveAction
import com.megacrit.cardcrawl.cards.status.VoidCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.exordium.Cultist
import javassist.CtBehavior
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess

class CultistVoids {
    @SpirePatch(
        clz = Cultist::class,
        method = "getMove"
    )
    class ChangeIntent {
        companion object {
            @JvmStatic
            fun Instrument(): ExprEditor =
                object : ExprEditor() {
                    override fun edit(f: FieldAccess) {
                        if (f.isReader && f.fieldName == "ATTACK") {
                            f.replace("\$_ = ${AbstractMonster.Intent::class.qualifiedName}.ATTACK_DEBUFF;")
                        }
                    }
                }
        }
    }

    @SpirePatch(
        clz = Cultist::class,
        method = "takeTurn"
    )
    class AddVoicCard {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class
            )
            fun Insert(__instance: Cultist) {
                if (__instance.nextMove == 1.toByte()) {
                    AbstractDungeon.actionManager.addToBottom(MakeTempCardInDiscardAction(VoidCard(), 1))
                }
            }
        }
    }

    private class Locator : SpireInsertLocator() {
        override fun Locate(ctBehavior: CtBehavior?): IntArray {
            val finalMatcher = Matcher.NewExprMatcher(RollMoveAction::class.java)
            return LineFinder.findInOrder(ctBehavior, finalMatcher)
        }
    }
}
