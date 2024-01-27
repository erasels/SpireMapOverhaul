package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.monsters.beyond.GiantHead
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = GiantHead::class,
    method = "usePreBattleAction"
)
class GiantHeadNotSlow {
    companion object {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (HumilityZone.hasHumilityMod()) return

                    if (m.methodName == "addToBottom") {
                        m.replace(
                            "if (${HumilityZone::class.qualifiedName}.isNotInZone()) {" +
                                    "\$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }
}
