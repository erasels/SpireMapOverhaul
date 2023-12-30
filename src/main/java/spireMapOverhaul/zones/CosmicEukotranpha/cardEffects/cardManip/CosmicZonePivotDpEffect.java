package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CosmicZonePivotDpEffect extends CosmicZoneAbstractGameAction{public boolean sc=true;public static final String[] TEXT;private AbstractPlayer p;private int numberOfCards;private boolean optional;boolean isRandom=false;
    public CosmicZonePivotDpEffect(int numberOfCards,boolean optional,boolean isRandom){this.actionType=ActionType.CARD_MANIPULATION;this.duration=this.startDuration=Settings.ACTION_DUR_XFAST;this.p=AbstractDungeon.player;this.numberOfCards=numberOfCards;this.optional=optional;this.isRandom=isRandom;}
    public CosmicZonePivotDpEffect(int numberOfCards,boolean optional){this.actionType=ActionType.CARD_MANIPULATION;this.duration=this.startDuration=Settings.ACTION_DUR_XFAST;this.p=AbstractDungeon.player;this.numberOfCards=numberOfCards;this.optional=optional;isRandom=false;}
    public CosmicZoneAbstractGameAction makeCopy(){
        return new CosmicZonePivotDpEffect(amount,optional,isRandom);}
    public CosmicZonePivotDpEffect(int numberOfCards){this(numberOfCards,false);}
    public void update(){if(sc){sc=false;if(p.discardPile.isEmpty()||numberOfCards<1){this.isDone=true;return;}
        if(isRandom){for(int i=0;i<numberOfCards;i++){AbstractCard cee=p.discardPile.getRandomCard(AbstractDungeon.cardRandomRng);p.discardPile.moveToDeck(cee,true);
            cee.lighten(false);cee.unhover();cee.applyPowers();}for(AbstractCard c:p.discardPile.group){c.unhover();c.target_x=(float)CardGroup.DISCARD_PILE_X;c.target_y=0.0F;}return;}
        if(optional){AbstractDungeon.gridSelectScreen.open(p.discardPile,numberOfCards,true,"TODO: PivotDp Text"+numberOfCards);
        }else{if(p.discardPile.size()>numberOfCards){AbstractDungeon.gridSelectScreen.open(p.discardPile,numberOfCards,"TODO: PivotDp Text"+numberOfCards,false,false,false,false);}else{AbstractDungeon.gridSelectScreen.selectedCards.addAll(p.discardPile.group);}}
    }else{if(!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
        for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){p.discardPile.moveToDeck(c,true);
            c.lighten(false);c.unhover();c.applyPowers();}
        for(AbstractCard c:p.discardPile.group){c.unhover();c.target_x=(float) CardGroup.DISCARD_PILE_X;c.target_y=0.0F;}
        AbstractDungeon.gridSelectScreen.selectedCards.clear();AbstractDungeon.player.hand.refreshHandLayout();}
        tickDuration();if(isDone){for(AbstractCard c:p.hand.group){c.applyPowers();}}}}
    static{TEXT=CardCrawlGame.languagePack.getUIString("BetterToHandAction").TEXT;}}
