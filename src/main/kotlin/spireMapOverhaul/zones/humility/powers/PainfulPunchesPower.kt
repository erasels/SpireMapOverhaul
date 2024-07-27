package spireMapOverhaul.zones.humility.powers

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.status.Wound
import com.megacrit.cardcrawl.core.AbstractCreature
import spireMapOverhaul.SpireAnniversary6Mod

class PainfulPunchesPower(
    owner: AbstractCreature
) : AbstractHumilityPower(POWER_ID, "master_smite") {
    companion object {
        val POWER_ID = SpireAnniversary6Mod.makeID("PainfulPunches")
    }
    private var particleTimer: Float = 0f

    init {
        this.owner = owner
        this.amount = -1
        updateDescription()
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0]
    }

    override fun onInflictDamage(info: DamageInfo?, damageAmount: Int, target: AbstractCreature?) {
        if (damageAmount > 0 && info?.type != DamageInfo.DamageType.THORNS) {
            addToTop(MakeTempCardInDrawPileAction(Wound(), 1, false, true))
        }
    }
}
