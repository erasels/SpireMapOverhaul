package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CZGetAll;
public class CZDiscardTEffect extends CosmicZoneAbstractGameAction{
    public CZDiscardTEffect(AbstractCard c){this.card=c;}public CosmicZoneAbstractGameAction makeCopy(){return new CZDiscardTEffect(this.card);}
    public void update(){if(card==null){this.isDone=true;return;}
        if(CZGetAll.get().contains(card)){
            if(p.discardPile.contains(card)){p.discardPile.moveToDiscardPile(card);}
            if(p.cardInUse==card){p.cardInUse.moveToDiscardPile();}
            if(p.drawPile.contains(card)){p.drawPile.moveToDiscardPile(card);}
            if(p.exhaustPile.contains(card)){card.unfadeOut();p.discardPile.addToTop(card);if(p.hasPower("Corruption")&&card.type==AbstractCard.CardType.SKILL){card.setCostForTurn(-9);}AbstractDungeon.player.exhaustPile.removeCard(card);card.unhover();card.fadingOut=false;}
            if(p.limbo.contains(card)){p.limbo.moveToDiscardPile(card);}
            if(p.hand.contains(card)){p.hand.moveToDiscardPile(card);}
            card.triggerOnManualDiscard();GameActionManager.incrementDiscard(false);
            //for(AbstractPower pow:p.powers){if(pow instanceof AmalgamAbstractPower){((AmalgamAbstractPower)pow).onDiscard(card);}}for(AbstractMonster mo:AbstractDungeon.getCurrRoom().monsters.monsters){for(AbstractPower pow:mo.powers){if(pow instanceof AmalgamAbstractPower){((AmalgamAbstractPower)pow).onDiscard(card);}}}
        }this.isDone=true;}}