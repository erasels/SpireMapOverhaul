package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CZRotateTEffect extends CosmicZoneAbstractGameAction{
    public CZRotateTEffect(AbstractCard c){this.card=c;}public CosmicZoneAbstractGameAction makeCopy(){
        return new CZRotateTEffect(this.card);}
    public void update(){if(card!=null){
        if(p.drawPile.contains(card)){p.drawPile.moveToBottomOfDeck(card);}
        if(p.cardInUse==card){if(AbstractDungeon.player.hoveredCard==card){AbstractDungeon.player.releaseCard();}
            AbstractDungeon.actionManager.removeFromQueue(card);
            card.unhover();card.untip();card.stopGlowing();p.cardInUse=null;card.shrink();
            AbstractDungeon.getCurrRoom().souls.onToBottomOfDeck(card);}
        if(p.discardPile.contains(card)){p.discardPile.moveToBottomOfDeck(card);}
        if(p.exhaustPile.contains(card)){p.exhaustPile.moveToBottomOfDeck(card);}
        if(p.limbo.contains(card)){p.limbo.moveToBottomOfDeck(card);}
        if(p.hand.contains(card)){p.hand.moveToBottomOfDeck(card);}
    }this.isDone=true;}}