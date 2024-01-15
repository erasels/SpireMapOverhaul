package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.events.beyond.MysteriousSphere
import com.megacrit.cardcrawl.helpers.MonsterHelper
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

@SpirePatch(
    clz = MysteriousSphere::class,
    method = SpirePatch.CONSTRUCTOR
)
class SphereEventAltFight {
    companion object {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.className == MonsterHelper::class.qualifiedName && m.methodName == "getEncounter") {
                        m.replace(
                            "if (${AbstractDungeon::class.qualifiedName}.miscRng.randomBoolean()) {" +
                                    "\$1 = ${MonsterHelper::class.qualifiedName}.SNECKO_WITH_MYSTICS;" +
                                    "}" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    }
                }
            }
    }
}
