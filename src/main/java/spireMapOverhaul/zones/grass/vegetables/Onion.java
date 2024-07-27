package spireMapOverhaul.zones.grass.vegetables;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class Onion extends AbstractVegetable {

    public static final AbstractVegetableData DATA = AbstractVegetableData.register(Onion.class);

    public Onion() {
        super(DATA);
    }

    @Override
    public int getEffectAmount() {
        return 1;
    }

    @Override
    public int getHits() {
        return level * 2;
    }

    @Override
    public AbstractGameAction getHitAction(AbstractCreature target) {
        return new ApplyPowerAction(target, target, new VulnerablePower(target, getEffectAmount(), false));
    }
}
