package spireMapOverhaul.zones.humility.powers

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.beyond.Nemesis
import spireMapOverhaul.SpireAnniversary6Mod
import kotlin.math.max

class SemiIntangiblePower(
    owner: AbstractCreature,
    amount: Int
) : AbstractHumilityPower(POWER_ID, "intangible") {
    companion object {
        val POWER_ID = SpireAnniversary6Mod.makeID("SemiIntangible")
    }

    private var justApplied = true

    init {
        this.owner = owner
        this.amount = amount
        priority = 70
        updateDescription()
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0].format(amount)
    }

    /*override fun atDamageFinalReceive(damage: Float, type: DamageInfo.DamageType): Float {
        return max(0f, damage - amount)
    }*/

    override fun atEndOfTurn(isPlayer: Boolean) {
        if (justApplied) {
            justApplied = false
            return
        }

        flash()
        addToBot(RemoveSpecificPowerAction(owner, owner, this))
    }

    @SpirePatch(
        clz = Nemesis::class,
        method = "damage"
    )
    object Patch {
        @JvmStatic
        fun Prefix(__instance: AbstractMonster, info: DamageInfo) {
            __instance.getPower(POWER_ID)?.let {
                info.output -= it.amount
                info.output = max(info.output, 0)
            }
        }
    }
}
