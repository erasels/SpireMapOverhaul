package spireMapOverhaul.zones.humility.patches.events

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.helpers.MonsterHelper
import com.megacrit.cardcrawl.monsters.MonsterGroup
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = MonsterHelper::class,
    method = "getEncounter"
)
class ColosseumDoubleNobs {
    companion object {
        @JvmStatic
        fun Prefix(key: String): SpireReturn<MonsterGroup> {
            if (HumilityZone.isNotInZone()) return SpireReturn.Continue()

            if (key == MonsterHelper.COLOSSEUM_NOB_ENC) {
                return SpireReturn.Return(
                    MonsterGroup(
                        arrayOf(
                            GremlinNob(-270f, 15f),
                            GremlinNob(130f, 0f)
                        )
                    )
                )
            }
            return SpireReturn.Continue()
        }
    }
}
