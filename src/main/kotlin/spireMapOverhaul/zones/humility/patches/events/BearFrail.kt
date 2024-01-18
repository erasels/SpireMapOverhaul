package spireMapOverhaul.zones.humility.patches.events

import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.city.BanditBear
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.FrailPower
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = BanditBear::class,
    method = "takeTurn"
)
class BearFrail {
    companion object {
        private const val AMT = 2

        @JvmStatic
        @SpireInsertPatch(
            locator = Locator::class
        )
        fun Insert(__instance: BanditBear) {
            if (HumilityZone.isNotInZone()) return

            AbstractDungeon.actionManager.addToBottom(
                VFXAction(
                    object : ViceCrushEffect(
                        AbstractDungeon.player.hb.cX,
                        AbstractDungeon.player.hb.cY
                    ) {
                        init {
                            color = Color.CHARTREUSE.cpy()
                            color.a = 0f
                        }
                    },
                    0.5f
                )
            )
            AbstractDungeon.actionManager.addToBottom(
                ApplyPowerAction(
                    AbstractDungeon.player,
                    __instance,
                    FrailPower(AbstractDungeon.player, AMT, true),
                    AMT
                )
            )
        }
    }

    private class Locator : SpireInsertLocator() {
        override fun Locate(ctBehavior: CtBehavior?): IntArray {
            val finalMatcher = Matcher.NewExprMatcher(DexterityPower::class.java)
            return LineFinder.findInOrder(ctBehavior, finalMatcher)
        }
    }
}
