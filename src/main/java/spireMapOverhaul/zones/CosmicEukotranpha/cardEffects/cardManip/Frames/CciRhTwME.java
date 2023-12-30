package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.Frames;/*ChooseCardsInGroupToTargetWithEffect*/
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;

import java.util.ArrayList;
public class CciRhTwME extends AbstractGameAction{public boolean sc=true;private static final UIStrings uiStrings;public static final String[] TEXT;private AbstractPlayer p;private boolean carvana=false;private ArrayList<CosmicZoneAbstractGameAction>action;private int key;public CardGroup toRem;
    public CciRhTwME(int amount, ArrayList<CosmicZoneAbstractGameAction>action,int key,CardGroup toRem){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.key=key;this.action=action;this.toRem=toRem;}
    public void update(){AbstractCard card;if(sc){sc=false;this.p.hand.group.removeAll(this.toRem.group);
        if(this.amount<1||AbstractDungeon.player.hand.size()<1){for(AbstractCard c:toRem.group){p.hand.addToTop(c);}this.isDone=true;return;}
        if(key==0){AbstractDungeon.handCardSelectScreen.open("CciRhTwME",amount,true);
        }else{if(AbstractDungeon.player.hand.size()<=amount){AbstractDungeon.handCardSelectScreen.selectedCards.group.addAll(AbstractDungeon.player.hand.group);AbstractDungeon.handCardSelectScreen.wereCardsRetrieved=false;
        }else{AbstractDungeon.handCardSelectScreen.open("CciRhTwME",amount,false);}}
    }else{if(AbstractDungeon.handCardSelectScreen.selectedCards.size()!=0){
        for(AbstractCard c:AbstractDungeon.handCardSelectScreen.selectedCards.group){for(CosmicZoneAbstractGameAction ac:action){CosmicZoneAbstractGameAction aC=ac.makeCopy();aC.card=c;addToTop(aC);p.hand.addToTop(c);}}
        for(AbstractCard c:toRem.group){p.hand.addToTop(c);}
        AbstractDungeon.handCardSelectScreen.selectedCards.clear();this.p.hand.refreshHandLayout();
    }tickDuration();}}
    static{uiStrings=CardCrawlGame.languagePack.getUIString("AnyCardFromDeckToHandAction");TEXT=uiStrings.TEXT;}}
//Add Strings Later