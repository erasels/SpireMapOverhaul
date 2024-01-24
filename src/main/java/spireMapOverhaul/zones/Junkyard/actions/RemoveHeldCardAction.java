package spireMapOverhaul.zones.Junkyard.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import spireMapOverhaul.zones.Junkyard.monsters.Junkbot;
import spireMapOverhaul.zones.Junkyard.powers.JunkGrabPower;

public class RemoveHeldCardAction extends AbstractGameAction {
    private AbstractCreature owner;
    private AbstractCard cardToRemove;
    private float startingDuration;
    private Junkbot junkOwner;

    public RemoveHeldCardAction(AbstractCreature owner, AbstractCard card) {
        this.owner = owner;
        this.duration = Settings.ACTION_DUR_FAST;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        junkOwner = (Junkbot) owner;
        cardToRemove = card;
    }

    public void update() {
        for (AbstractPower p : junkOwner.powers){
            if (p instanceof JunkGrabPower){
                JunkGrabPower pow = (JunkGrabPower) p;
                if (pow.card.cardID.equals(cardToRemove.cardID)){
                    addToTop(new RemoveSpecificPowerAction(junkOwner, junkOwner, pow));
                    addToBot(new VFXAction(new ExhaustCardEffect(cardToRemove)));
                    break;
                }
            }
        }
        junkOwner.removeHeldCard(cardToRemove);

        this.isDone = true;
    }
}



