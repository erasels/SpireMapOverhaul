package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatches(
    SpirePatch(
        clz = LouseNormal::class,
        method = "changeState"
    ),
    SpirePatch(
        clz = LouseDefensive::class,
        method = "changeState"
    )
)
class LouseReCurlUp {
    companion object {
        @JvmStatic
        fun Postfix(__instance: AbstractMonster, stateName: String, ___isOpen: Boolean) {
            if (HumilityZone.isNotInZone()) return

            if( ___isOpen) {
                __instance.usePreBattleAction()
            }
        }
    }
}
