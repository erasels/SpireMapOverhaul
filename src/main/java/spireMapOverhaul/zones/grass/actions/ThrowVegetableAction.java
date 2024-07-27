package spireMapOverhaul.zones.grass.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.actions.CallbackAction;
import spireMapOverhaul.zones.grass.vegetables.AbstractVegetable;

public class ThrowVegetableAction extends AbstractGameAction {

    private final AbstractVegetable vegetable;

    public ThrowVegetableAction(AbstractVegetable vegetable, AbstractCreature target, int times) {
        this.vegetable = vegetable;
        this.target = target;
        this.amount = times;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
        } else if (this.target != null && !this.target.isDeadOrEscaped()) {
            this.addToTop(new CallbackAction<AbstractGameAction>(vegetable.getHitAction(target), __ -> {
                if (this.amount > 1) {
                    AbstractCreature randomMonster = vegetable.getTarget();
                    if (randomMonster != null) {
                        this.addToTop(new ThrowVegetableAction(vegetable, randomMonster, this.amount - 1));
                        this.addToTop(new VFXAction(new ThrowVegetableEffect(vegetable, this.target.hb.cX, this.target.hb.cY, randomMonster.hb.cX, randomMonster.hb.cY), 0.4F));
                    }
                }
            }));

        }
        this.isDone = true;
    }
}
