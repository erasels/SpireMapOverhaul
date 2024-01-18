package spireMapOverhaul.zones.humility.patches.exordium

import basemod.ReflectionHacks
import com.evacipated.cardcrawl.modthespire.lib.ByRef
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo
import com.megacrit.cardcrawl.monsters.exordium.SlaverBlue
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = SlaverRed::class,
    method = "getMove"
)
class RedSlaverStopDoubleDebuff {
    companion object {
        @JvmStatic
        fun Prefix(__instance: SlaverRed, @ByRef num: Array<Int>, ___usedEntangle: Boolean) {
            if (HumilityZone.isNotInZone()) return

            if (num[0] >= 75 && !___usedEntangle) {
                val blueSlaverDebuffing = AbstractDungeon.getMonsters().monsters
                    .filterNot { it.isDeadOrEscaped }
                    .filterIsInstance<SlaverBlue>()
                    .map { ReflectionHacks.getPrivate(it, AbstractMonster::class.java, "move") as EnemyMoveInfo }
                    .any { it.nextMove == BlueSlaverNoBlockMove.TRIP }
                if (blueSlaverDebuffing) {
                    num[0] = 74
                }
            }
        }
    }
}
