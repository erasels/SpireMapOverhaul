package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.beyond.WrithingMass
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower

@SpirePatch(
    clz = WrithingMass::class,
    method = "usePreBattleAction"
)
class WrithingMassRegen {
    companion object {
        @JvmStatic
        fun Postfix(__instance: WrithingMass) {
            AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, RegenerateMonsterPower(__instance, 8), 8))
        }
    }
}
