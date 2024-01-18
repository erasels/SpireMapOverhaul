package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone

class RedSlaverReuseEntangle {
    @SpirePatch(
        clz = SlaverRed::class,
        method = "takeTurn"
    )
    class ResetUsedEntangle {
        companion object {
            @JvmStatic
            fun Postfix(__instance: SlaverRed, @ByRef ___usedEntangle: Array<Boolean>, ___ENTANGLE: Byte) {
                if (HumilityZone.isNotInZone()) return

                if (__instance.nextMove != ___ENTANGLE) {
                    ___usedEntangle[0] = false
                }
            }
        }
    }

    @SpirePatch(
        clz = SlaverRed::class,
        method = "getMove"
    )
    class ResetNetAnimation {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class
            )
            fun Insert(__instance: SlaverRed, num: Int) {
                if (HumilityZone.isNotInZone()) return

                val tmp = __instance.state.getCurrent(0).time
                __instance.state.setAnimation(0, "idle", true)
                    .also { it.time = tmp }
            }
        }

        private class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior?): IntArray {
                val finalMatcher = Matcher.FieldAccessMatcher(SlaverRed::class.java, "ENTANGLE_NAME")
                return LineFinder.findInOrder(ctBehavior, finalMatcher)
            }
        }
    }
}
