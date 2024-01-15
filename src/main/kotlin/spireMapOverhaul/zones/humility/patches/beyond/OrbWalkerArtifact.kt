package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.beyond.OrbWalker
import com.megacrit.cardcrawl.powers.ArtifactPower

@SpirePatch(
    clz = OrbWalker::class,
    method = "usePreBattleAction"
)
class OrbWalkerArtifact {
    companion object {
        @JvmStatic
        fun Postfix(__instance: OrbWalker) {
            AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, ArtifactPower(__instance, 2), 2))
        }
    }
}
