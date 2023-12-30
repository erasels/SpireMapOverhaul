package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CosmicZoneFetchEffect extends CosmicZoneAbstractGameAction{public boolean sc=true;public static final String[] TEXT;private AbstractPlayer p;private int numberOfCards;private boolean optional;private boolean optionalT2;//optional: Random. optionalT2: Can choose which to fetch
    public CosmicZoneFetchEffect(int numberOfCards,boolean optional,boolean optionalT2){this.actionType=ActionType.CARD_MANIPULATION;this.duration=this.startDuration=Settings.ACTION_DUR_XFAST;this.p=AbstractDungeon.player;this.numberOfCards=numberOfCards;this.optional=optional;this.optionalT2=optionalT2;}
    public CosmicZoneFetchEffect(int numberOfCards){this(numberOfCards,false,true);}
    public CosmicZoneFetchEffect makeCopy(){
        return new CosmicZoneFetchEffect(amount,optional,optionalT2);}
    public void update(){if(sc){sc=false;numberOfCards=Math.min(numberOfCards,BaseMod.MAX_HAND_SIZE-p.hand.size());if(p.discardPile.isEmpty()||numberOfCards<1){this.isDone=true;return;}
        if(optional){AbstractDungeon.gridSelectScreen.open(p.discardPile,numberOfCards,true,"TODO: Fetch Text"+numberOfCards);
        }else{if(p.discardPile.size()>numberOfCards){AbstractDungeon.gridSelectScreen.open(p.discardPile,numberOfCards,"TODO: Fetch Text"+numberOfCards,false,false,false,false);}else{AbstractDungeon.gridSelectScreen.selectedCards.addAll(p.discardPile.group);}}
    }else{if(!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
        for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){
            if(p.hand.size()<BaseMod.MAX_HAND_SIZE){p.hand.addToHand(c);p.discardPile.removeCard(c);}
            c.lighten(false);c.unhover();c.applyPowers();}}
        for(AbstractCard c:p.discardPile.group){c.unhover();c.target_x=(float)CardGroup.DISCARD_PILE_X;c.target_y=0.0F;}
        AbstractDungeon.gridSelectScreen.selectedCards.clear();AbstractDungeon.player.hand.refreshHandLayout();
        tickDuration();if(isDone){for(AbstractCard c:p.hand.group){c.applyPowers();}}}}
    static{TEXT=CardCrawlGame.languagePack.getUIString("BetterToHandAction").TEXT;}}