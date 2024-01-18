package spireMapOverhaul.zones.humility.patches.city

import basemod.ReflectionHacks
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo
import com.megacrit.cardcrawl.monsters.city.Mugger
import com.megacrit.cardcrawl.monsters.exordium.Looter
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatches(
    SpirePatch(
        clz = Looter::class,
        method = "takeTurn"
    ),
    SpirePatch(
        clz = Mugger::class,
        method = "takeTurn"
    )
)
class ThievesSkipBlockMove {
    companion object {
        @JvmStatic
        fun Postfix(__instance: AbstractMonster, ___SMOKE_BOMB: Byte, ___ESCAPE: Byte) {
            if (HumilityZone.isNotInZone()) return

            val move = ReflectionHacks.getPrivate(__instance, AbstractMonster::class.java, "move") as EnemyMoveInfo
            if (move.nextMove == ___SMOKE_BOMB) {
                __instance.setMove(___ESCAPE, AbstractMonster.Intent.ESCAPE)
            }
        }
    }
}
