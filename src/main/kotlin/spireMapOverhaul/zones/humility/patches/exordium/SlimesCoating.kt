package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_M
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_M
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.patches.utils.addPreBattleAction
import spireMapOverhaul.zones.humility.powers.SlimeCoatingPower

class SlimesCoating {
    @SpirePatches(
        SpirePatch(
            clz = AcidSlime_S::class,
            method = "takeTurn"
        ),
        SpirePatch(
            clz = AcidSlime_M::class,
            method = "takeTurn"
        ),
        SpirePatch(
            clz = SpikeSlime_S::class,
            method = "takeTurn"
        ),
        SpirePatch(
            clz = SpikeSlime_M::class,
            method = "takeTurn"
        )
    )
    class PreBattle {
        companion object {
            @JvmStatic
            fun Raw(ctBehavior: CtBehavior) {
                ctBehavior.addPreBattleAction(Companion::doPreBattleAction)
            }

            @JvmStatic
            fun doPreBattleAction(__instance: AbstractMonster) {
                if (HumilityZone.isNotInZone()) return

                val amount = if (__instance is AcidSlime_M || __instance is SpikeSlime_M) {
                    2
                } else {
                    1
                }
                AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, SlimeCoatingPower(__instance, amount)))
            }
        }
    }

    @SpirePatch2(
        clz = SpawnMonsterAction::class,
        method = "update"
    )
    class AddSlimeCoatingToSplit {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class
            )
            fun Insert(___m: AbstractMonster) {
                if (___m is AcidSlime_S || ___m is AcidSlime_M || ___m is SpikeSlime_S || ___m is SpikeSlime_M) {
                    PreBattle.doPreBattleAction(___m)
                }
            }
        }

        private class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior?): IntArray {
                val finalMatcher = Matcher.FieldAccessMatcher(SpawnMonsterAction::class.java, "minion")
                return LineFinder.findInOrder(ctBehavior, finalMatcher)
            }
        }
    }
}
