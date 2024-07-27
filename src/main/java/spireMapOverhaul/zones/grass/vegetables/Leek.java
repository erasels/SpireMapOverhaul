package spireMapOverhaul.zones.grass.vegetables;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Leek extends AbstractVegetable {

    public static final AbstractVegetableData DATA = AbstractVegetableData.register(Leek.class);

    public Leek() {
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
        return new ApplyPowerAction(target, target, new WeakPower(target, getEffectAmount(), false));
    }
}
