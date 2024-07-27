package spireMapOverhaul.zones.humility.powers

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction
import com.megacrit.cardcrawl.cards.status.Dazed
import com.megacrit.cardcrawl.core.AbstractCreature
import spireMapOverhaul.SpireAnniversary6Mod

class DeathRattlePower(
    owner: AbstractCreature,
    amount: Int
) : AbstractHumilityPower(POWER_ID, "hex") {
    companion object {
        val POWER_ID = SpireAnniversary6Mod.makeID("DeathRattle")
    }

    init {
        this.owner = owner
        this.amount = amount
        updateDescription()
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0].format(amount)
    }

    override fun onDeath() {
        flashWithoutSound()
        addToTop(MakeTempCardInDrawPileAction(Dazed(), amount, false, true))
    }
}
