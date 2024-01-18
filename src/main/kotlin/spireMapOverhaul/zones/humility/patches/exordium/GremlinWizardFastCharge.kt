package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.ByRef
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.monsters.exordium.GremlinWizard
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = GremlinWizard::class,
    method = "takeTurn"
)
class GremlinWizardFastCharge {
    companion object {
        @JvmStatic
        fun Prefix(__instance: GremlinWizard, @ByRef ___currentCharge: Array<Int>) {
            if (HumilityZone.isNotInZone()) return

            if (__instance.nextMove == 2.toByte() && ___currentCharge[0] == 1) {
                ___currentCharge[0] = 2
            }
        }
    }
}
