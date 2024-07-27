package spireMapOverhaul.zones.humility.powers

import com.megacrit.cardcrawl.actions.animations.TalkAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.StrengthPower
import spireMapOverhaul.SpireAnniversary6Mod

class SneckoBotPower(
    owner: AbstractCreature,
    amount: Int
) : AbstractHumilityPower(POWER_ID, "confusion") {
    companion object {
        val POWER_ID = SpireAnniversary6Mod.makeID("SneckoBot")
    }

    init {
        this.owner = owner
        this.amount = amount
        updateDescription()
    }

    override fun updateDescription() {
        description = "${DESCRIPTIONS[0]}$amount${DESCRIPTIONS[1]}"
    }

    override fun onAfterUseCard(card: AbstractCard, action: UseCardAction) {
        val guess = AbstractDungeon.cardRandomRng.random(3)
        addToBot(TalkAction(owner, guess.toString()))
        if (guess == card.costForTurn) {
            flashWithoutSound()
            addToBot(ApplyPowerAction(owner, owner, StrengthPower(owner, amount), amount))
        }
    }
}
