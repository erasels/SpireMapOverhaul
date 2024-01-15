package spireMapOverhaul.zones.humility.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.DrawPower

class DrawDownPower(
    owner: AbstractCreature,
    amount: Int
) : DrawPower(owner, amount) {
    init {
        canGoNegative = true
    }

    override fun updateDescription() {
        if (amount < 0) {
            if (amount == -1) {
                description = "${DESCRIPTIONS[0]}${-amount}${DESCRIPTIONS[2]}"
            } else {
                description = "${DESCRIPTIONS[0]}${-amount}${DESCRIPTIONS[4]}"
            }
            type = PowerType.DEBUFF
        } else {
            super.updateDescription()
        }
    }
}
