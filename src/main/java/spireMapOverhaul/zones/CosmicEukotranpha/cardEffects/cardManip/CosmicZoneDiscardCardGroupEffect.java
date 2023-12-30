package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CZGetAll;
public class CosmicZoneDiscardCardGroupEffect extends CosmicZoneAbstractGameAction{public boolean sc=true;private static final UIStrings uiStrings;public static final String[] TEXT;private AbstractPlayer p;private boolean isRandom;public static int numDiscarded;private boolean optional;private int amount;public CardGroup cardGroup=new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public CosmicZoneDiscardCardGroupEffect(CardGroup cardGroup,int amount){this(cardGroup,amount,false,false);}public CosmicZoneDiscardCardGroupEffect(CardGroup cardGroup,int amount,boolean isRandom){this(cardGroup,amount,isRandom,false);}
    public CosmicZoneDiscardCardGroupEffect(CardGroup cardGroup,int amount,boolean isRandom,boolean optional){this.p=AbstractDungeon.player;this.isRandom=isRandom;this.actionType=ActionType.DISCARD;this.duration=this.startDuration=Settings.ACTION_DUR_XFAST;this.amount=amount;this.optional=optional;this.cardGroup=cardGroup;}
    public CosmicZoneAbstractGameAction makeCopy(){
        return new CosmicZoneDiscardCardGroupEffect(cardGroup,amount,isRandom,optional);}
    public void update(){if(sc){sc=false;if(cardGroup.isEmpty()||amount==0){this.isDone=true;return;}
        if(optional){AbstractDungeon.gridSelectScreen.open(cardGroup,amount,true,"TODO: Discard CardGroup Text"+amount);
        }else{if(cardGroup.size()>amount){AbstractDungeon.gridSelectScreen.open(cardGroup,amount,"TODO: DiscardOp CardGroup Text"+amount,false,false,false,false);}else{AbstractDungeon.gridSelectScreen.selectedCards.addAll(cardGroup.group);}}
    }else if(!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
        for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){discardT(c);}
        for(AbstractCard c:p.drawPile.group){c.unhover();c.target_x=(float)CardGroup.DISCARD_PILE_X;}
        AbstractDungeon.gridSelectScreen.selectedCards.clear();AbstractDungeon.player.hand.refreshHandLayout();
        tickDuration();if(this.isDone){for(AbstractCard c:p.hand.group){c.applyPowers();}}}else{tickDuration();}}
    public void discardT(AbstractCard card){
        if(CZGetAll.get().contains(card)&&!p.discardPile.contains(card)){
            //if(p.discardPile.contains(card)){p.discardPile.moveToDiscardPile(card);}
            if(p.cardInUse==card){p.cardInUse.moveToDiscardPile();}
            if(p.drawPile.contains(card)){p.drawPile.moveToDiscardPile(card);}
            if(p.exhaustPile.contains(card)){card.unfadeOut();p.discardPile.addToTop(card);if(p.hasPower("Corruption")&&card.type==AbstractCard.CardType.SKILL){card.setCostForTurn(-9);}AbstractDungeon.player.exhaustPile.removeCard(card);card.unhover();card.fadingOut=false;}
            if(p.limbo.contains(card)){p.limbo.moveToDiscardPile(card);}
            if(p.hand.contains(card)){p.hand.moveToDiscardPile(card);}
            card.triggerOnManualDiscard();GameActionManager.incrementDiscard(false);card.lighten(false);card.unhover();card.applyPowers();
            //for(AbstractPower pow:p.powers){if(pow instanceof AmalgamAbstractPower){((AmalgamAbstractPower)pow).onDiscard(card);}}for(AbstractMonster mo:AbstractDungeon.getCurrRoom().monsters.monsters){for(AbstractPower pow:mo.powers){if(pow instanceof AmalgamAbstractPower){((AmalgamAbstractPower)pow).onDiscard(card);}}}
        }}
    static{uiStrings=CardCrawlGame.languagePack.getUIString("DiscardAction");TEXT=uiStrings.TEXT;}}