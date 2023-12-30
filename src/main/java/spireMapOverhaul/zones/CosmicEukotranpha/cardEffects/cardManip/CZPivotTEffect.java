package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CZPivotTEffect extends CosmicZoneAbstractGameAction{
    public CZPivotTEffect(AbstractCard c){this.card=c;}public CosmicZoneAbstractGameAction makeCopy(){
        return new CZPivotTEffect(this.card);}
    public void update(){if(card!=null){
        if(p.drawPile.contains(card)){p.drawPile.moveToDeck(card,true);}
        if(p.cardInUse==card){if(AbstractDungeon.player.hoveredCard==card){AbstractDungeon.player.releaseCard();}
            AbstractDungeon.actionManager.removeFromQueue(card);
            card.unhover();card.untip();card.stopGlowing();p.cardInUse=null;card.shrink();
            AbstractDungeon.getCurrRoom().souls.onToDeck(card,true);}
        if(p.discardPile.contains(card)){p.discardPile.moveToDeck(card,true);}
        if(p.exhaustPile.contains(card)){p.exhaustPile.moveToDeck(card,true);}
        if(p.limbo.contains(card)){p.limbo.moveToDeck(card,true);}
        if(p.hand.contains(card)){p.hand.moveToDeck(card,true);}
    }this.isDone=true;}}