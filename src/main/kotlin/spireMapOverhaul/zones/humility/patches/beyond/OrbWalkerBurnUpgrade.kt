package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.monsters.beyond.OrbWalker
import javassist.expr.ExprEditor
import javassist.expr.NewExpr
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = OrbWalker::class,
    method = "takeTurn"
)
class OrbWalkerBurnUpgrade {
    companion object {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(e: NewExpr) {
                    if (HumilityZone.hasHumilityMod()) return

                    if (e.className == Burn::class.qualifiedName) {
                        e.replace(
                            "\$_ = \$proceed(\$\$);" +
                                    "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                    "\$_.upgrade();" +
                                    "}"
                        )
                    }
                }
            }
    }
}
