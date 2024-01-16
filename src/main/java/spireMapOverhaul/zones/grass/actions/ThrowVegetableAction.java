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
        if (this.target == null) {
            this.isDone = true;
        } else if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.isDone = true;
        } else {
            if (this.amount > 1 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractCreature randomMonster = vegetable.getTarget();
                this.addToTop(new ThrowVegetableAction(vegetable, randomMonster, this.amount - 1));
                this.addToTop(new VFXAction(new ThrowVegetableEffect(vegetable, this.target.hb.cX, this.target.hb.cY, randomMonster.hb.cX, randomMonster.hb.cY), 0.4F));
            }

            if (this.target.currentHealth > 0) {
                this.addToTop(vegetable.getHitAction(target));
                this.addToTop(new WaitAction(0.1F));
            }

            this.isDone = true;
        }
    }
}
