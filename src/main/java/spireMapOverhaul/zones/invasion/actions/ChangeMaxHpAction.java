package spireMapOverhaul.zones.invasion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class ChangeMaxHpAction extends AbstractGameAction {
    private boolean showEffect;

    public ChangeMaxHpAction(AbstractCreature m, int amount, boolean showEffect) {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_XFAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_FAST;
        }

        this.duration = this.startDuration;
        this.showEffect = showEffect;
        this.amount = amount;
        this.target = m;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.amount > 0) {
                this.target.increaseMaxHp(this.amount, this.showEffect);
            }
            else if (this.amount < 0) {
                this.target.decreaseMaxHealth(-this.amount);
            }
        }

        this.tickDuration();
    }
}
