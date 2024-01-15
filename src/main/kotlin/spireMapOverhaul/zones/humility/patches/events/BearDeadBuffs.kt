package spireMapOverhaul.zones.humility.patches.events

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.city.BanditLeader
import com.megacrit.cardcrawl.monsters.city.BanditPointy
import com.megacrit.cardcrawl.powers.AngerPower
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatches(
    SpirePatch(
        clz = BanditLeader::class,
        method = "deathReact"
    ),
    SpirePatch(
        clz = BanditPointy::class,
        method = "deathReact"
    )
)
class BearDeadBuffs {
    companion object {
        @JvmStatic
        fun Postfix(__instance: AbstractMonster) {
            if (HumilityZone.isNotInZone()) return

            if (!__instance.isDeadOrEscaped) {
                AbstractDungeon.actionManager.addToBottom(
                    ApplyPowerAction(
                        __instance,
                        __instance,
                        AngerPower(
                            __instance,
                            1
                        ),
                        1
                    )
                )
            }
        }
    }
}
