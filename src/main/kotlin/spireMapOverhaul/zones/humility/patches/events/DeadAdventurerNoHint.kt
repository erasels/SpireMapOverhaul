package spireMapOverhaul.zones.humility.patches.events

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.events.exordium.DeadAdventurer
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = DeadAdventurer::class,
    method = SpirePatch.STATICINITIALIZER
)
class DeadAdventurerNoHint {
    companion object {
        @JvmStatic
        fun Postfix() {
            if (HumilityZone.isNotInZone()) return

            arrayOf(3, 4, 5).forEach { i ->
                DeadAdventurer.DESCRIPTIONS[i] = "... "
            }
        }
    }
}
