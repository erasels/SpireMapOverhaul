package spireMapOverhaul.zones.grass.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import spireMapOverhaul.actions.CallbackAction;
import spireMapOverhaul.util.Wiz;
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
                    this.addToTop(new ThrowVegetableAction(vegetable, randomMonster, this.amount - 1));
                    this.addToTop(new VFXAction(new ThrowVegetableEffect(vegetable, this.target.hb.cX, this.target.hb.cY, randomMonster.hb.cX, randomMonster.hb.cY), 0.4F));
                }
            }));

        }
        this.isDone = true;
    }
}
