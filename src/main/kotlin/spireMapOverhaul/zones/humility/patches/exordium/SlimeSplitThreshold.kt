package spireMapOverhaul.zones.humility.patches.exordium

import basemod.ReflectionHacks
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.actions.common.SetMoveAction
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_L
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_L
import com.megacrit.cardcrawl.powers.SplitPower
import spireMapOverhaul.SpireAnniversary6Mod
import spireMapOverhaul.zones.humility.HumilityZone

class SlimeSplitThreshold {
    companion object {
        private val strings: PowerStrings by lazy { CardCrawlGame.languagePack.getPowerStrings(SpireAnniversary6Mod.makeID(SplitPower.POWER_ID)) }
    }

    @SpirePatch2(
        clz = SplitPower::class,
        method = "updateDescription"
    )
    class SplitPowerName {
        companion object {
            @JvmStatic
            fun Postfix(__instance: SplitPower) {
                if (HumilityZone.isNotInZone()) return

                __instance.description = strings.DESCRIPTIONS[0] + FontHelper.colorString(__instance.owner.name, "y") + strings.DESCRIPTIONS[1]
            }
        }
    }

    @SpirePatches(
        SpirePatch(
            clz = SlimeBoss::class,
            method = "damage"
        ),
        SpirePatch(
            clz = SpikeSlime_L::class,
            method = "damage"
        ),
        SpirePatch(
            clz = AcidSlime_L::class,
            method = "damage"
        )
    )
    class SlimeBossSplit {
        companion object {
            @JvmStatic
            fun Postfix(__instance: AbstractMonster, info: DamageInfo, ___SPLIT_NAME: String) {
                if (HumilityZone.isNotInZone()) return

                if (!__instance.isDying && __instance.nextMove != 3.toByte()
                    && __instance.currentHealth > __instance.maxHealth / 2f // don't trigger if less than 50% HP, base game covers that
                    && __instance.currentHealth <= __instance.maxHealth * 0.75f // trigger if less than 75% HP
                    && !getSplitTriggered(__instance)
                ) {
                    __instance.setMove(
                        ___SPLIT_NAME,
                        3.toByte(),
                        AbstractMonster.Intent.UNKNOWN
                    )
                    __instance.createIntent()
                    AbstractDungeon.actionManager.addToBottom(TextAboveCreatureAction(__instance, TextAboveCreatureAction.TextType.INTERRUPTED))
                    AbstractDungeon.actionManager.addToBottom(SetMoveAction(__instance, ___SPLIT_NAME, 3.toByte(), AbstractMonster.Intent.UNKNOWN))
                    setSplitTriggered(__instance, true)
                }
            }

            private fun getSplitTriggered(__instance: AbstractMonster): Boolean {
                if (__instance is SpikeSlime_L) {
                    return ReflectionHacks.getPrivate(__instance, SpikeSlime_L::class.java, "splitTriggered") as Boolean
                }
                if (__instance is AcidSlime_L) {
                    return ReflectionHacks.getPrivate(__instance, AcidSlime_L::class.java, "splitTriggered") as Boolean
                }
                return false
            }

            private fun setSplitTriggered(__instance: AbstractMonster, value: Boolean) {
                if (__instance is SpikeSlime_L) {
                    ReflectionHacks.setPrivate(__instance, SpikeSlime_L::class.java, "splitTriggered", value)
                }
                if (__instance is AcidSlime_L) {
                    ReflectionHacks.setPrivate(__instance, AcidSlime_L::class.java, "splitTriggered", value)
                }
            }
        }
    }

    @SpirePatch(
        cls = "mintySpire.patches.monsters.HalfwayHealthbarTextPatch\$TextRender",
        method = "getHPTextAddition",
        requiredModId = "mintyspire"
    )
    class MintySpireCompat {
        companion object {
            @JvmStatic
            fun Prefix(c: AbstractCreature): SpireReturn<String> {
                if (HumilityZone.isNotInZone()) return SpireReturn.Continue()

                if (!(c.isDead || c.isDying)) {
                    if (SlimeBoss.ID == c.id) {
                        return SpireReturn.Return(" (${(c.maxHealth * 0.75f).toInt()})")
                    }
                }
                return SpireReturn.Continue()
            }
        }
    }
}
