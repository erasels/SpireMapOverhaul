package spireMapOverhaul.zones.humility.actions

import basemod.interfaces.CloneablePowerInterface
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower

class ApplyPowerAllMonstersAction(
    source: AbstractCreature,
    private val powerToApply: AbstractPower,
    stackAmount: Int
) : AbstractGameAction() {
    init {
        setValues(null, source, stackAmount)
    }

    override fun update() {
        if (powerToApply is CloneablePowerInterface) {
            AbstractDungeon.getMonsters().monsters
                .filterNot { it.isDeadOrEscaped }
                .asReversed()
                .forEach { m ->
                    val power = powerToApply.makeCopy().also { it.owner = m }
                    addToTop(ApplyPowerAction(m, source, power, amount, true, AttackEffect.NONE))
                }
        }
        isDone = true
    }
}
