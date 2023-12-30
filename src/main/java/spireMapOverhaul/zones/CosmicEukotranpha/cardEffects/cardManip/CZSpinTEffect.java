package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CZSpinTEffect extends CosmicZoneAbstractGameAction{
    public CZSpinTEffect(AbstractCard c){this.card=c;}public CosmicZoneAbstractGameAction makeCopy(){
        return new CZSpinTEffect(this.card);}
    public void update(){if(card!=null){
        if(p.drawPile.contains(card)){p.drawPile.moveToDeck(card,false);}
        if(p.cardInUse==card){if(AbstractDungeon.player.hoveredCard==card){AbstractDungeon.player.releaseCard();}
            AbstractDungeon.actionManager.removeFromQueue(card);
            card.unhover();card.untip();card.stopGlowing();p.cardInUse=null;card.shrink();
            AbstractDungeon.getCurrRoom().souls.onToDeck(card,false);}
        if(p.discardPile.contains(card)){p.discardPile.moveToDeck(card,false);}
        if(p.exhaustPile.contains(card)){p.exhaustPile.moveToDeck(card,false);}
        if(p.limbo.contains(card)){p.limbo.moveToDeck(card,false);}
        if(p.hand.contains(card)){p.hand.moveToDeck(card,false);}
    }this.isDone=true;}}