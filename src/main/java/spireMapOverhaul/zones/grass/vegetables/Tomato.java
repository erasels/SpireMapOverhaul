package spireMapOverhaul.zones.grass.vegetables;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Tomato extends AbstractVegetable {

    public static final AbstractVegetableData DATA = AbstractVegetableData.register(Tomato.class);

    public Tomato() {
        super(DATA);
    }

    @Override
    public int getEffectAmount() {
        return level * 2;
    }

    @Override
    public AbstractCreature getTarget() {
        return AbstractDungeon.player;
    }

    @Override
    public AbstractGameAction getHitAction(AbstractCreature target) {
        return new ApplyPowerAction(target, target, new StrengthPower(target, getEffectAmount()));
    }
}
