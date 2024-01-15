package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.ByRef
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.monsters.beyond.Exploder
import com.megacrit.cardcrawl.powers.ExplosivePower
import javassist.expr.ExprEditor
import javassist.expr.NewExpr

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
                    if (e.className == ExplosivePower::class.qualifiedName) {
                        e.replace("\$_ = \$proceed(\$1, \$2 - 1);")
                    }
                }
            }

        @JvmStatic
        fun Postfix(__instance: Exploder, @ByRef ___turnCount: Array<Int>) {
            ___turnCount[0] = 1
        }
    }
}
