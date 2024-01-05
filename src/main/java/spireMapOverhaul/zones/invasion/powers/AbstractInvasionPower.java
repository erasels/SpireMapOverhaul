package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import spireMapOverhaul.abstracts.AbstractSMOPower;

public class AbstractInvasionPower extends AbstractSMOPower {
    public AbstractInvasionPower(String ID, String NAME, PowerType powerType, boolean isTurnBased, AbstractCreature owner, int amount) {
        super(ID, NAME, "Invasion", powerType, isTurnBased, owner, amount);
    }
}
