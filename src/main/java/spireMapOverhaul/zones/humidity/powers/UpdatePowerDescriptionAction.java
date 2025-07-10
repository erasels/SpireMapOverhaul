package spireMapOverhaul.zones.humidity.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class UpdatePowerDescriptionAction extends AbstractGameAction {

    private final AbstractPower power;

    public UpdatePowerDescriptionAction(AbstractPower power) {
        this.power = power;
    }

    @Override
    public void update() {
        power.updateDescription();
        isDone = true;
    }
}
