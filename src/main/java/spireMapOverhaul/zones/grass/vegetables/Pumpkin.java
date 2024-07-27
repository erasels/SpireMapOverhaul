package spireMapOverhaul.zones.grass.vegetables;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Pumpkin extends AbstractVegetable {

    public static final AbstractVegetableData DATA = AbstractVegetableData.register(Pumpkin.class);

    public Pumpkin() {
        super(DATA);
    }

    @Override
    public int getEffectAmount() {
        return level * 7;
    }

    @Override
    public AbstractCreature getTarget() {
        return AbstractDungeon.player;
    }

    @Override
    public AbstractGameAction getHitAction(AbstractCreature target) {
        return new GainBlockAction(target, getEffectAmount());
    }
}
