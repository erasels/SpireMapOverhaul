package spireMapOverhaul.zones.humility.powers

import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import spireMapOverhaul.SpireAnniversary6Mod
import kotlin.math.max

class VenomPower(
    owner: AbstractCreature,
    private val source: AbstractCreature,
    amount: Int
) : AbstractHumilityPower(POWER_ID, "poison"), HealthBarRenderPower {
    companion object {
        val POWER_ID = SpireAnniversary6Mod.makeID("Venom")
        private val COLOR = Color.GREEN.cpy()
    }

    init {
        this.owner = owner
        this.amount = amount
        type = PowerType.DEBUFF
        updateDescription()
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0].format(amount, 1)
    }

    override fun getColor(): Color =
        COLOR

    override fun getHealthBarAmount(): Int {
        return max(0, amount - owner.currentBlock)
    }

    override fun atEndOfTurn(isPlayer: Boolean) {
        flashWithoutSound()
        addToBot(DamageAction(owner, DamageInfo(source, amount, DamageInfo.DamageType.THORNS)))
        addToBot(ReducePowerAction(owner, owner, this, 1))
    }
}
