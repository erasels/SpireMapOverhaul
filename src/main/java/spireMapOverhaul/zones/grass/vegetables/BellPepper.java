package spireMapOverhaul.zones.grass.vegetables;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class BellPepper extends AbstractVegetable {

    public static final AbstractVegetableData DATA = AbstractVegetableData.register(BellPepper.class);

    public BellPepper() {
        super(DATA);
    }

    @Override
    public int getEffectAmount() {
        return level;
    }

    @Override
    public int getHits() {
        return 3;
    }

    @Override
    public AbstractGameAction getHitAction(AbstractCreature target) {
        return new ApplyPowerAction(target, target, new PoisonPower(target, target, getEffectAmount()));
    }
}
