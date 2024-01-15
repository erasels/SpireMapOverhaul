package spireMapOverhaul.zones.humility.patches.events

import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.SetMoveAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.city.BanditLeader
import com.megacrit.cardcrawl.powers.WeakPower
import javassist.CtBehavior

class RomeoDebuff {
    companion object {
        private const val AMT = 5
    }

    @SpirePatch(
        clz = BanditLeader::class,
        method = "getMove"
    )
    class GetMove {
        companion object {
            @JvmStatic
            fun Prefix(__instance: BanditLeader, num: Int, ___MOCK: Byte): SpireReturn<Unit?> {
                __instance.setMove(___MOCK, AbstractMonster.Intent.DEBUFF)
                return SpireReturn.Return(null)
            }
        }
    }

    @SpirePatch(
        clz = BanditLeader::class,
        method = "takeTurn"
    )
    class TakeTurn {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class
            )
            fun Insert(__instance: BanditLeader) {
                AbstractDungeon.actionManager.addToBottom(
                    ApplyPowerAction(
                        AbstractDungeon.player,
                        __instance,
                        WeakPower(AbstractDungeon.player, AMT, true),
                        AMT
                    )
                )
            }
        }

        private class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior?): IntArray {
                val finalMatcher = Matcher.NewExprMatcher(SetMoveAction::class.java)
                return LineFinder.findInOrder(ctBehavior, finalMatcher)
            }
        }
    }
}
