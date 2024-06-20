package spireMapOverhaul.zones.grass.vegetables;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class Radish extends AbstractVegetable {

    public static final AbstractVegetableData DATA = AbstractVegetableData.register(Radish.class);

    public Radish() {
        super(DATA);
    }

    @Override
    public int getEffectAmount() {
        return 5 * level;
    }

    @Override
    public int getHits() {
        return 2;
    }

    @Override
    public AbstractGameAction getHitAction(AbstractCreature target) {
        return new DamageAction(target, new DamageInfo(target, getEffectAmount()));
    }
}
