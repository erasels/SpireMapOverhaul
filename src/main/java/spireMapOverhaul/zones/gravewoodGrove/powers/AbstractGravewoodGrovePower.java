package spireMapOverhaul.zones.gravewoodGrove.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.gravewoodGrove.GravewoodGroveZone;

public abstract class AbstractGravewoodGrovePower extends AbstractSMOPower {
    public AbstractGravewoodGrovePower(String ID, String NAME, PowerType powerType, boolean isTurnBased, AbstractCreature owner, int amount) {
        super(ID, NAME, GravewoodGroveZone.ID, powerType, isTurnBased, owner, amount);
    }
}
