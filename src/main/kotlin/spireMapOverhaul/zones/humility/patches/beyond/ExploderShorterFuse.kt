package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.ByRef
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.monsters.beyond.Exploder
import com.megacrit.cardcrawl.powers.ExplosivePower
import javassist.expr.ExprEditor
import javassist.expr.NewExpr
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = Exploder::class,
    method = "usePreBattleAction"
)
class ExploderShorterFuse {
    companion object {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(e: NewExpr) {
                    if (HumilityZone.hasHumilityMod()) return

                    if (e.className == ExplosivePower::class.qualifiedName) {
                        e.replace(
                            "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                    "\$_ = \$proceed(\$1, \$2 - 1);" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }

        @JvmStatic
        fun Postfix(__instance: Exploder, @ByRef ___turnCount: Array<Int>) {
            if (HumilityZone.isNotInZone()) return

            ___turnCount[0] = 1
        }
    }
}
