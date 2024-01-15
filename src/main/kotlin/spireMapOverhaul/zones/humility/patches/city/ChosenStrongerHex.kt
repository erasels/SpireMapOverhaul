package spireMapOverhaul.zones.humility.patches.city

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.powers.HexPower
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess

@SpirePatch(
    clz = HexPower::class,
    method = "onUseCard"
)
class ChosenStrongerHex {
    companion object {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(f: FieldAccess) {
                    if (f.isReader && f.className == AbstractCard::class.qualifiedName && f.fieldName == "type") {
                        f.replace("\$_ = ${AbstractCard.CardType::class.qualifiedName}.SKILL;")
                    }
                }
            }
    }
}
