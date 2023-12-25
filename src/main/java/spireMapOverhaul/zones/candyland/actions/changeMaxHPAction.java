package spireMapOverhaul.zones.candyland.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class changeMaxHPAction extends AbstractGameAction {
    public changeMaxHPAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.setValues(target, source, amount);
        this.actionType = AbstractGameAction.ActionType.HEAL;
    }

    @Override
    public void update() {
        if (this.duration == 0.5f) {
            target.increaseMaxHp(this.amount, false);
        }
        this.tickDuration();
    }
}