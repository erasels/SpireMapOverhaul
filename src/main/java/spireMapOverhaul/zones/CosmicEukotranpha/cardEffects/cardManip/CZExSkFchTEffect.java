package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;

import java.util.Iterator;
public class CZExSkFchTEffect extends CosmicZoneAbstractGameAction{
    public CZExSkFchTEffect(AbstractCard c){this.card=c;}public CosmicZoneAbstractGameAction makeCopy(){
        return new CZExSkFchTEffect(this.card);}
    public void update(){if(card!=null){
        if(p.exhaustPile.contains(card)){card.unhover();AbstractDungeon.player.hand.addToHand(card);card.unfadeOut();
            if(AbstractDungeon.player.hasPower("Corruption") && card.type==AbstractCard.CardType.SKILL){card.setCostForTurn(-9);}
            AbstractDungeon.player.exhaustPile.removeCard(card);card.unhover();card.fadingOut=false;AbstractDungeon.player.hand.refreshHandLayout();
            for(Iterator q = AbstractDungeon.player.exhaustPile.group.iterator(); q.hasNext(); card.target_y=0.0F){card=(AbstractCard)q.next();card.unhover();card.target_x=(float) CardGroup.DISCARD_PILE_X;}
        }else if(p.drawPile.contains(card)){AbstractDungeon.player.drawPile.moveToHand(card);
        }else if(p.discardPile.contains(card)){AbstractDungeon.player.discardPile.moveToHand(card);}
        }this.isDone=true;}}