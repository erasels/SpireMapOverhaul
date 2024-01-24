package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.unique.GainBlockRandomMonsterAction
import com.megacrit.cardcrawl.monsters.exordium.GremlinTsundere
import com.megacrit.cardcrawl.powers.BufferPower
import javassist.expr.ExprEditor
import javassist.expr.NewExpr
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.actions.ApplyPowerRandomMonsterAction

@SpirePatch(
    clz = GremlinTsundere::class,
    method = "takeTurn"
)
class GremlinTsundereBuffer {
    companion object {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(e: NewExpr) {
                    if (HumilityZone.hasHumilityMod()) return

                    if (e.className == GainBlockRandomMonsterAction::class.qualifiedName) {
                        e.replace(
                            "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                    "\$_ = new ${ApplyPowerRandomMonsterAction::class.qualifiedName}(this, new ${BufferPower::class.qualifiedName}(null, 1), 1);" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }
}
