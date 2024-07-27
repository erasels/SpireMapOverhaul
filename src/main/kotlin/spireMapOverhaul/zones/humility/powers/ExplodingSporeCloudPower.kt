package spireMapOverhaul.zones.humility.powers

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.powers.VulnerablePower
import spireMapOverhaul.SpireAnniversary6Mod

class ExplodingSporeCloudPower(
    owner: AbstractCreature,
    vulnAmount: Int,
    turns: Int
) : AbstractHumilityPower(POWER_ID, "sporeCloud") {
    companion object {
        val POWER_ID = SpireAnniversary6Mod.makeID("ExplodingSporeCloud")
    }
    private var amount2: Int

    init {
        this.owner = owner
        this.amount = turns
        this.amount2 = vulnAmount
        isTurnBased = true
        updateDescription()
    }

    override fun updateDescription() {
        if (amount <= 0) {
            amount = amount2
            amount2 = -1
            isTurnBased = false
            onDeath()
        }

        description = if (amount2 > 0) {
            DESCRIPTIONS[0].format(amount2, amount)
        } else {
            DESCRIPTIONS[1].format(amount)
        }
    }

    override fun renderAmount(sb: SpriteBatch, x: Float, y: Float, c: Color) {
        super.renderAmount(sb, x, y, c)

        if (amount2 > 0) {
            val color = Color.GREEN.cpy()
            color.a = c.a
            FontHelper.renderFontRightTopAligned(
                sb,
                FontHelper.powerAmountFont,
                amount2.toString(),
                x, y + 15f * Settings.scale,
                fontScale,
                color
            )
        }
    }

    override fun onDeath() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding) {
            return
        }
        CardCrawlGame.sound.play("SPORE_CLOUD_RELEASE")
        flashWithoutSound()
        val amt = if (amount2 == -1) {
            amount
        } else {
            amount2
        }
        addToTop(ApplyPowerAction(AbstractDungeon.player, null, VulnerablePower(AbstractDungeon.player, amt, true), amt))
    }

    override fun atEndOfRound() {
        if (amount2 > 0) {
            addToBot(
                object : AbstractGameAction() {
                    init {
                        setValues(owner, owner, 1)
                        duration = if (Settings.FAST_MODE) {
                            Settings.ACTION_DUR_XFAST
                        } else {
                            Settings.ACTION_DUR_FAST
                        }
                        startDuration = duration
                        actionType = ActionType.REDUCE_POWER
                    }

                    override fun update() {
                        if (duration == startDuration) {
                            this@ExplodingSporeCloudPower.amount -= amount
                            this@ExplodingSporeCloudPower.updateDescription()
                            AbstractDungeon.onModifyPower()
                        }
                        tickDuration()
                    }
                }
            )
        }
    }
}
