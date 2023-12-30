package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CZPurgeTEffect extends CosmicZoneAbstractGameAction{
    public CZPurgeTEffect(AbstractCard c){this.card=c;}public CosmicZoneAbstractGameAction makeCopy(){
        return new CZPurgeTEffect(this.card);}
    public void update(){if(card!=null){
        if(p.cardInUse==card){p.cardInUse=null;}
        if(p.drawPile.contains(card)){p.drawPile.removeCard(card);}
        if(p.discardPile.contains(card)){p.discardPile.removeCard(card);}
        if(p.exhaustPile.contains(card)){p.exhaustPile.removeCard(card);}
        if(p.limbo.contains(card)){p.limbo.removeCard(card);}
        if(p.hand.contains(card)){p.hand.removeCard(card);}
    }this.isDone=true;}}