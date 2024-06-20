package spireMapOverhaul.zones.humility.powers

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.StrengthPower
import spireMapOverhaul.SpireAnniversary6Mod

class DemonFormMonsterPower(
    owner: AbstractCreature,
    amount: Int
) : AbstractHumilityPower(POWER_ID, "demonForm") {
    companion object {
        val POWER_ID = SpireAnniversary6Mod.makeID("DemonFormMonster")
    }

    init {
        this.owner = owner
        this.amount = amount
        updateDescription()
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0].format(amount)
    }

    override fun atStartOfTurn() {
        flash()
        addToBot(ApplyPowerAction(owner, owner, StrengthPower(owner, amount), amount))
    }
}
