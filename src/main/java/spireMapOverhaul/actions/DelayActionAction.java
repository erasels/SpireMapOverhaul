package spireMapOverhaul.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import spireMapOverhaul.util.Wiz;

public class DelayActionAction extends AbstractGameAction {
    private AbstractGameAction action;

    public DelayActionAction(AbstractGameAction action) {
        this.action = action;
    }

    @Override
    public void update() {
        Wiz.atb(action);
        isDone = true;
    }
}
