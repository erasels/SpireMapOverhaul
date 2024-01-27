package spireMapOverhaul.zones.humility.patches.city

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.monsters.city.ShelledParasite
import com.megacrit.cardcrawl.powers.MetallicizePower
import com.megacrit.cardcrawl.powers.PlatedArmorPower
import javassist.expr.ExprEditor
import javassist.expr.NewExpr
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = ShelledParasite::class,
    method = "usePreBattleAction"
)
class ShelledParasiteMetallicize {
    companion object {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(e: NewExpr) {
                    if (HumilityZone.hasHumilityMod()) return

                    if (e.className == PlatedArmorPower::class.qualifiedName) {
                        e.replace(
                            "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                    "\$_ = new ${MetallicizePower::class.qualifiedName}(\$\$);" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }
}
