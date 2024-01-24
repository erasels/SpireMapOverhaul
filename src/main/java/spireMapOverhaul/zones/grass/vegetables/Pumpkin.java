package spireMapOverhaul.zones.grass.vegetables;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

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
