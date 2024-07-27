package spireMapOverhaul.zones.humility.patches.exordium

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.exordium.SlaverBlue
import com.megacrit.cardcrawl.powers.NoBlockPower
import com.megacrit.cardcrawl.vfx.combat.EntangleEffect
import spireMapOverhaul.SpireAnniversary6Mod
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.patches.utils.lastMove

class BlueSlaverNoBlockMove {
    companion object {
        const val TRIP: Byte = 8
    }

    @SpirePatch(
        clz = SlaverBlue::class,
        method = "getMove"
    )
    class GetMove {
        companion object {
            private var TRIP_NAME: String? = null

            @JvmStatic
            fun Prefix(__instance: SlaverBlue, num: Int): SpireReturn<Unit?> {
                if (HumilityZone.isNotInZone()) return SpireReturn.Continue()

                if (num >= 75 && !__instance.lastMove(TRIP)) {
                    if (TRIP_NAME == null) {
                        val strings = CardCrawlGame.languagePack.getMonsterStrings(SpireAnniversary6Mod.makeID(SlaverBlue.ID))
                        TRIP_NAME = strings?.MOVES?.get(0)
                    }
                    __instance.setMove(TRIP_NAME ?: "[MISSING]", TRIP, AbstractMonster.Intent.STRONG_DEBUFF)
                    return SpireReturn.Return(null)
                }
                return SpireReturn.Continue()
            }
        }
    }

    @SpirePatch(
        clz = SlaverBlue::class,
        method = "takeTurn"
    )
    class TakeTurn {
        companion object {
            @JvmStatic
            fun Prefix(__instance: SlaverBlue) {
                if (__instance.nextMove == TRIP) {
                    if (!Settings.FAST_MODE) {
                        AbstractDungeon.actionManager.addToBottom(VFXAction(EntangleEffect(
                            __instance.hb.cX - 70f * Settings.scale,
                            __instance.hb.cY + 10f * Settings.scale,
                            AbstractDungeon.player.hb.cX,
                            AbstractDungeon.player.hb.cY
                            ), 0.5f
                        ))
                    }
                    AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(AbstractDungeon.player, __instance, NoBlockPower(AbstractDungeon.player, 1, true), 1))
                }
            }
        }
    }
}
