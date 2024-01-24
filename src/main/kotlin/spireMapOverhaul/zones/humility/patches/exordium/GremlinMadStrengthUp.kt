package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.monsters.exordium.GremlinWarrior
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.powers.AngryPower
import com.megacrit.cardcrawl.powers.GenericStrengthUpPower
import javassist.expr.ExprEditor
import javassist.expr.NewExpr
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = GremlinWarrior::class,
    method = "usePreBattleAction"
)
class GremlinMadStrengthUp {
    companion object {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(e: NewExpr) {
                    if (HumilityZone.hasHumilityMod()) return

                    if (e.className == AngryPower::class.qualifiedName) {
                        e.replace(
                            "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                    "\$_ = new ${GenericStrengthUpPower::class.qualifiedName}($1, ${AngryPower::class.qualifiedName}.NAME, $2);" +
                                    "\$_.region48 = ${AbstractPower::class.qualifiedName}.atlas.findRegion(\"48/anger\");" +
                                    "\$_.region128 = ${AbstractPower::class.qualifiedName}.atlas.findRegion(\"128/anger\");" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }
}
