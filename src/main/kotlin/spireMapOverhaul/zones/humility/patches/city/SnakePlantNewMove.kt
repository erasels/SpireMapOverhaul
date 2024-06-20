package spireMapOverhaul.zones.humility.patches.city

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.ChangeStateAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.utility.WaitAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.city.SnakePlant
import com.megacrit.cardcrawl.powers.ArtifactPower
import com.megacrit.cardcrawl.powers.GainStrengthPower
import com.megacrit.cardcrawl.powers.StrengthPower
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.patches.utils.isFirstMove
import spireMapOverhaul.zones.humility.patches.utils.lastMove
import spireMapOverhaul.zones.humility.patches.utils.lastTwoMoves

class SnakePlantNewMove {
    companion object {
        private const val POISON_STRIKE: Byte = 3
        private const val DAMAGE = 17
        private const val STR_DOWN = 3
    }

    @SpirePatch(
        clz = SnakePlant::class,
        method = SpirePatch.CONSTRUCTOR
    )
    class AddDamage {
        companion object {
            @JvmStatic
            fun Postfix(__instance: SnakePlant, x: Float, y: Float) {
                if (HumilityZone.isNotInZone()) return

                __instance.damage.add(DamageInfo(__instance, DAMAGE))
            }
        }
    }

    @SpirePatch(
        clz = SnakePlant::class,
        method = "getMove"
    )
    class GetMove {
        companion object {
            @JvmStatic
            fun Prefix(__instance: SnakePlant, num: Int): SpireReturn<Unit?> {
                if (HumilityZone.isNotInZone()) return SpireReturn.Continue()

                if (AbstractDungeon.ascensionLevel >= 17) {
                    if (__instance.isFirstMove()) {
                        if (num < 65) {
                            __instance.setMove(1, AbstractMonster.Intent.ATTACK, __instance.damage[0].base, 3, true)
                        } else {
                            __instance.setMove(SnakePlant.MOVES[0], 2, AbstractMonster.Intent.STRONG_DEBUFF)
                        }
                    } else if (__instance.lastMove(1)) { // was Chomp
                        doPoisonStrike(__instance)
                    } else if (__instance.lastMove(POISON_STRIKE)) { // was Poison Strike (new)
                        __instance.setMove(SnakePlant.MOVES[0], 2, AbstractMonster.Intent.STRONG_DEBUFF)
                    } else if (__instance.lastMove(2)) { // was Enfeebling Spores
                        __instance.setMove(1, AbstractMonster.Intent.ATTACK, __instance.damage[0].base, 3, true)
                    }
                    return SpireReturn.Return(null)
                } else {
                    if (num < 30 && !__instance.lastTwoMoves(POISON_STRIKE)) {
                        doPoisonStrike(__instance)
                        return SpireReturn.Return(null)
                    }
                }
                return SpireReturn.Continue()
            }

            private fun doPoisonStrike(__instance: SnakePlant) {
                __instance.setMove(
                    POISON_STRIKE,
                    AbstractMonster.Intent.ATTACK_DEBUFF,
                    __instance.damage[1].base
                )
            }
        }
    }

    @SpirePatch(
        clz = SnakePlant::class,
        method = "takeTurn"
    )
    class TakeTurn {
        companion object {
            @JvmStatic
            fun Prefix(__instance: SnakePlant) {
                if (__instance.nextMove == POISON_STRIKE) {
                    AbstractDungeon.actionManager.addToBottom(ChangeStateAction(__instance, "ATTACK"))
                    AbstractDungeon.actionManager.addToBottom(WaitAction(0.5f))
                    AbstractDungeon.actionManager.addToBottom(DamageAction(
                        AbstractDungeon.player,
                        __instance.damage[1],
                        AbstractGameAction.AttackEffect.POISON,
                        true
                    ))
                    AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(
                        AbstractDungeon.player,
                        __instance,
                        StrengthPower(AbstractDungeon.player, -STR_DOWN),
                        -STR_DOWN,
                        true,
                        AbstractGameAction.AttackEffect.NONE
                    ))
                    if (!AbstractDungeon.player.hasPower(ArtifactPower.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(
                            ApplyPowerAction(
                                AbstractDungeon.player,
                                __instance,
                                GainStrengthPower(AbstractDungeon.player, STR_DOWN - 1),
                                STR_DOWN - 1,
                                true,
                                AbstractGameAction.AttackEffect.NONE
                            )
                        )
                    }
                }
            }
        }
    }
}
