package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.exordium.FungiBeast
import com.megacrit.cardcrawl.powers.SporeCloudPower
import javassist.expr.ExprEditor
import javassist.expr.NewExpr
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.powers.ExplodingSporeCloudPower

@SpirePatch(
    clz = FungiBeast::class,
    method = "usePreBattleAction"
)
class FungiBeastExplodingSpores {
    companion object {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(e: NewExpr) {
                    if (HumilityZone.hasHumilityMod()) return

                    if (e.className == SporeCloudPower::class.qualifiedName) {
                        e.replace(
                            "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                    "\$_ = new ${ExplodingSporeCloudPower::class.qualifiedName}($$, ${AbstractDungeon::class.qualifiedName}.monsterHpRng.random(2, 4));" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }
}
