package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.monsters.beyond.Spiker
import javassist.expr.ExprEditor
import javassist.expr.NewExpr
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.actions.ApplyPowerAllMonstersAction

@SpirePatch(
    clz = Spiker::class,
    method = "takeTurn"
)
class SpikerBuffAll {
    companion object {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(e: NewExpr) {
                    if (HumilityZone.hasHumilityMod()) return

                    if (e.className == ApplyPowerAction::class.qualifiedName) {
                        e.replace(
                            "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                    "\$_ = new ${ApplyPowerAllMonstersAction::class.qualifiedName}(\$2, \$3, \$4);" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }
}
