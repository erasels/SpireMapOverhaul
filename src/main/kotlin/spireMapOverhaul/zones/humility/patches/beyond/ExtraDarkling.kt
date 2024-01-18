package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.helpers.MonsterHelper
import com.megacrit.cardcrawl.monsters.MonsterGroup
import com.megacrit.cardcrawl.monsters.beyond.Darkling
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = MonsterHelper::class,
    method = "getEncounter"
)
class ExtraDarkling {
    companion object {
        @JvmStatic
        fun Prefix(key: String): SpireReturn<MonsterGroup> {
            if (HumilityZone.isNotInZone()) return SpireReturn.Continue()

            if (key == MonsterHelper.THREE_DARKLINGS_ENC) {
                return SpireReturn.Return(
                    MonsterGroup(
                        arrayOf(
                            Darkling(-660f, 30f),
                            Darkling(-360f, 10f),
                            Darkling(-60f, 35f),
                            Darkling(240f, -5f)
                        )
                    )
                )
            }
            return SpireReturn.Continue()
        }
    }
}
