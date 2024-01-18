package spireMapOverhaul.zones.humility.patches.city

import basemod.ReflectionHacks
import com.esotericsoftware.spine.Skeleton
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.city.SphericGuardian
import com.megacrit.cardcrawl.powers.BufferPower
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone

class SphericGuardianBuffer {
    @SpirePatch(
        clz = SphericGuardian::class,
        method = "usePreBattleAction"
    )
    class AddBuffer {
        companion object {
            @JvmStatic
            fun Postfix(__instance: SphericGuardian) {
                if (HumilityZone.isNotInZone()) return

                AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, BufferPower(__instance, 3), 3))
            }
        }
    }

    @SpirePatch(
        clz = BufferPower::class,
        method = "onAttackedToChangeDamage"
    )
    class RemoveSpheresOnLoseBuffer {
        companion object {
            private val slots = arrayOf("orb (right)", "orb (left)", "orb (back)")

            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class
            )
            fun Insert(__instance: BufferPower, info: DamageInfo, damageAmount: Int) {
                if (HumilityZone.isNotInZone()) return

                if (__instance.owner is SphericGuardian) {
                    val skeleton = ReflectionHacks.getPrivate(__instance.owner, AbstractCreature::class.java, "skeleton") as Skeleton?

                    if (__instance.amount in 1..3) {
                        skeleton?.findSlot(slots[__instance.amount - 1])?.let { slot ->
                            slot.attachment = null
                        }
                    }
                }
            }
        }

        private class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior?): IntArray {
                val finalMatcher = Matcher.NewExprMatcher(ReducePowerAction::class.java)
                return LineFinder.findInOrder(ctBehavior, finalMatcher)
            }
        }
    }
}
