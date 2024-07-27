package spireMapOverhaul.zones.humility.powers

import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.helpers.FontHelper
import spireMapOverhaul.SpireAnniversary6Mod

class DivineProtectionPower(
    owner: AbstractCreature,
    private val linked: AbstractCreature,
    amount: Int
) : AbstractHumilityPower(POWER_ID, "heartDef") {
    companion object {
        val POWER_ID = SpireAnniversary6Mod.makeID("DivineProtection")
    }

    private val maxAmount: Int

    init {
        this.owner = owner
        this.amount = amount
        maxAmount = amount
        updateDescription()
    }

    override fun updateDescription() {
        description = if (amount <= 0) {
            DESCRIPTIONS[1]
        } else {
            DESCRIPTIONS[0].format(amount)
        } + DESCRIPTIONS[2].format(FontHelper.colorString(linked.name, "y"))
    }

    override fun atStartOfTurn() {
        amount = maxAmount
        updateDescription()
    }

    override fun onAttackedToChangeDamage(info: DamageInfo?, damageAmount: Int): Int {
        var returnAmount = damageAmount
        if (returnAmount > amount) {
            returnAmount = amount
        }
        amount -= returnAmount
        if (amount < 0) {
            amount = 0
        }

        updateDescription()
        return returnAmount
    }
}
