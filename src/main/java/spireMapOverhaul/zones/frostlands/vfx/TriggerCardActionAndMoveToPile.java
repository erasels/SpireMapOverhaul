package spireMapOverhaul.zones.frostlands.vfx;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import spireMapOverhaul.util.Wiz;

public class TriggerCardActionAndMoveToPile extends AbstractGameEffect {

    public static final float DURATION = Settings.ACTION_DUR_MED;

    private AbstractCard card;
    private CardGroup groupToGoTo;
    private CardGroup groupFrom;
    boolean running = false, actioned = false;
    AbstractGameAction action;

    public TriggerCardActionAndMoveToPile(AbstractCard card, CardGroup groupFrom, CardGroup groupToGoTo, AbstractGameAction action) {
        duration = startingDuration = DURATION;
        this.card = card;
        this.groupToGoTo = groupToGoTo;
        this.groupFrom = groupFrom;
        this.action = action;
        actioned = false;
        card.drawScale = 0.1f;
        card.targetDrawScale = 0.6f;
        card.lighten(true);
        card.unfadeOut();
        card.unhover();
    }

    @Override
    public void update() {
        if(!running)
            duration = startingDuration;
        if (duration == startingDuration) {
            boolean run = true;
            if (groupFrom == AbstractDungeon.player.discardPile)
            {
                if(card.current_x < (float)Settings.WIDTH)
                    run = false;
                else
                    card.target_x = Settings.scale * Settings.WIDTH - 100 - (int) (Math.random() * 200) - card.hb.width;
            }
            else
                card.target_x = Settings.WIDTH / 2.0f - 100 + (int) (Math.random() * 200);
            if(run) {
                card.target_y = Settings.HEIGHT / 2.0f - 100 + (int) (Math.random() * 200);
                running = true;
            }
        }
        if(duration <= startingDuration / 2 && !actioned)
        {
            actioned = true;
            Wiz.att(action);
        }
        if(running)
            Run();
    }

    private void Run()
    {
        duration -= Gdx.graphics.getDeltaTime();
        card.update();
        if (duration < 0.0F) {
            isDone = true;

            if(groupToGoTo == AbstractDungeon.player.hand)
            {
                if(AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE)
                {
                    groupToGoTo = AbstractDungeon.player.discardPile;
                    AbstractDungeon.player.discardPile.addToTop(card);
                }
                else
                {
                    AbstractDungeon.player.drawPile.getTopCard().moveToDiscardPile();
                    AbstractDungeon.player.hand.addToTop(card);
                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.player.hand.applyPowers();
                }
            }
            if(groupToGoTo == AbstractDungeon.player.discardPile)
            {
                card.shrink();
                AbstractDungeon.getCurrRoom().souls.discard(card, true);
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!isDone)
            card.render(sb);
    }

    @Override
    public void dispose() {

    }
}
