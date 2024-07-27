package spireMapOverhaul.zones.grass.vegetables;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class Carrot extends AbstractVegetable {

    public static final AbstractVegetableData DATA = AbstractVegetableData.register(Carrot.class);

    public Carrot() {
        super(DATA);
    }

    @Override
    public int getEffectAmount() {
        return 4;
    }

    @Override
    public int getHits() {
        return level * 2;
    }

    @Override
    public AbstractGameAction getHitAction(AbstractCreature target) {
        return new DamageAction(target, new DamageInfo(target, getEffectAmount()));
    }
}
