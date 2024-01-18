package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.helpers.MonsterHelper
import com.megacrit.cardcrawl.monsters.MonsterGroup
import com.megacrit.cardcrawl.monsters.exordium.Sentry
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = MonsterHelper::class,
    method = "getEncounter"
)
class ExtraSentry {
    companion object {
        @JvmStatic
        fun Prefix(key: String): SpireReturn<MonsterGroup> {
            if (HumilityZone.isNotInZone()) return SpireReturn.Continue()

            if (key == MonsterHelper.THREE_SENTRY_ENC) {
                return SpireReturn.Return(
                    MonsterGroup(
                        arrayOf(
                            Sentry(-555f, 5f),
                            Sentry(-330f, 25f),
                            Sentry(-85f, 10f),
                            Sentry(140f, 30f)
                        )
                    )
                )
            }
            return SpireReturn.Continue()
        }
    }
}
