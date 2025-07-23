package spireMapOverhaul.zones.humidity.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.EntanglePower;

public class FakeEntangledPower extends EntanglePower {

    public FakeEntangledPower(AbstractCreature owner) {
        super(owner);
    }

    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

}
