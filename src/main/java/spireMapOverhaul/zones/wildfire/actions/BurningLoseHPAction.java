package spireMapOverhaul.zones.wildfire.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BurningLoseHPAction extends AbstractGameAction {
    private static final float DURATION = 0.33F;

    public BurningLoseHPAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.setValues(target, source, amount);
        this.actionType = ActionType.DAMAGE;
        this.duration = DURATION;
    }// 28

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
        } else {
            if (this.duration == DURATION && this.target.currentHealth > 0) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.FIRE));
            }

            this.tickDuration();
            if (this.isDone) {
                //Only set tint if the monster has > 0 HP, preventing exploding Hexaghost bug
                if (this.target.currentHealth > 0) {
                    this.target.tint.color.set(Color.RED);
                    this.target.tint.changeColor(Color.WHITE.cpy());
                    this.target.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.HP_LOSS));
                }
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
                this.addToTop(new WaitAction(0.1F));
            }
        }
    }
}
