package spireMapOverhaul.zones.Junkyard.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.Junkyard.monsters.Junkbot;
import spireMapOverhaul.zones.Junkyard.powers.JunkGrabPower;

public class GrabCardAction extends AbstractGameAction {
    private boolean grabSpecificCard = false;
    private AbstractCard cardToGrab;
    private AbstractCreature owner;
    private float startingDuration;
    private AbstractCard card = null;
    public Junkbot junkOwner;

    public GrabCardAction(AbstractCreature owner) {
        this.owner = owner;
        this.duration = Settings.ACTION_DUR_MED;
        this.startingDuration = Settings.ACTION_DUR_MED;
        this.actionType = ActionType.WAIT;
        junkOwner = (Junkbot)owner;
        grabSpecificCard = false;
    }

    public GrabCardAction(AbstractCreature owner, AbstractCard card) {
        this.owner = owner;
        this.duration = Settings.ACTION_DUR_LONG;
        this.startingDuration = Settings.ACTION_DUR_LONG;
        this.actionType = ActionType.WAIT;
        junkOwner = (Junkbot)owner;
        grabSpecificCard = true;
        cardToGrab = card;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            if (grabSpecificCard) {
                this.card = cardToGrab;
            }
            else {
                if (AbstractDungeon.player.drawPile.isEmpty() && AbstractDungeon.player.discardPile.isEmpty()) {
                    this.isDone = true;
                } else {
                    if (AbstractDungeon.player.drawPile.isEmpty()) {
                        this.card = AbstractDungeon.player.discardPile.getRandomCard(AbstractDungeon.cardRandomRng);
                        AbstractDungeon.player.discardPile.removeCard(this.card);
                    } else {
                        this.card = AbstractDungeon.player.drawPile.getRandomCard(AbstractDungeon.cardRandomRng);
                        AbstractDungeon.player.drawPile.removeCard(this.card);
                    }
                }
            }


            this.card.setAngle(0.0F);
            this.card.targetDrawScale = 0.75F;
            this.card.target_x = (float) Settings.WIDTH / 2.0F;
            this.card.target_y = (float) Settings.HEIGHT / 2.0F;
            this.card.lighten(false);
            this.card.unfadeOut();
            this.card.unhover();
            this.card.untip();
            this.card.stopGlowing();

            junkOwner.cardsToPreview.add(card);
        }

        this.tickDuration();
        if (this.isDone && this.card != null) {
            this.addToTop(new ApplyPowerAction(this.owner, this.owner, new JunkGrabPower(this.owner, this.card)));
        }
    }
}
