package spireMapOverhaul.zones.brokenSpace.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.TextCenteredEffect;

public class BetterTextCenteredAction extends AbstractGameAction {
    private boolean used = false;
    private String msg;
    private static final float DURATION = 2.0F;

    public BetterTextCenteredAction(AbstractCreature source, String text, float dur) {
        this.setValues(source, source);
        this.msg = text;
        this.duration = dur;
        this.actionType = ActionType.TEXT;
    }

    public void update() {
        if (!this.used) {
            AbstractDungeon.effectList.add(new TextCenteredEffect(this.msg));
            this.used = true;
        }

        this.tickDuration();
    }
}

