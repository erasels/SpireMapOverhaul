package spireMapOverhaul.zones.humility.powers

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction
import com.megacrit.cardcrawl.cards.status.Slimed
import com.megacrit.cardcrawl.core.AbstractCreature
import spireMapOverhaul.SpireAnniversary6Mod

class SlimeCoatingPower(
    owner: AbstractCreature,
    amount: Int
) : AbstractHumilityPower(POWER_ID, "split") {
    companion object {
        val POWER_ID = SpireAnniversary6Mod.makeID("SlimeCoating")
    }

    init {
        this.owner = owner
        this.amount = amount
        updateDescription()
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0].format(amount)
    }

    override fun duringTurn() {
        addToBot(object : AbstractGameAction() {
            override fun update() {
                flashWithoutSound()
                isDone = true
            }
        })
        addToBot(MakeTempCardInDiscardAction(Slimed(), amount))
    }
}
