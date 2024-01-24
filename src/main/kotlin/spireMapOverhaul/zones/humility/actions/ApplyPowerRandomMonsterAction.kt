package spireMapOverhaul.zones.humility.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower

class ApplyPowerRandomMonsterAction(
    source: AbstractCreature,
    private val powerToApply: AbstractPower,
    stackAmount: Int
) : AbstractGameAction() {
    init {
        setValues(null, source, stackAmount)
    }

    override fun update() {
        target = AbstractDungeon.getMonsters().getRandomMonster(source as AbstractMonster?, true, AbstractDungeon.aiRng)
        powerToApply.owner = target
        if (target != null) {
            addToTop(ApplyPowerAction(target, source, powerToApply, amount, true, AttackEffect.NONE))
        }
        isDone = true
    }
}
