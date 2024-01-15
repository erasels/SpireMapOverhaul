package spireMapOverhaul.zones.humility.patches.exordium

import spireMapOverhaul.zones.humility.powers.ExplodingSporeCloudPower
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.exordium.FungiBeast
import com.megacrit.cardcrawl.powers.SporeCloudPower
import javassist.expr.ExprEditor
import javassist.expr.NewExpr

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
                    if (e.className == SporeCloudPower::class.qualifiedName) {
                        e.replace("\$_ = new ${ExplodingSporeCloudPower::class.qualifiedName}($$, ${AbstractDungeon::class.qualifiedName}.monsterHpRng.random(2, 4));")
                    }
                }
            }
    }
}
