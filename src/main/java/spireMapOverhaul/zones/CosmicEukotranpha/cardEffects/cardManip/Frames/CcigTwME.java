package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.Frames;/*ChooseCardsInGroupToTargetWithMultipleEffect*/
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CZGetAll;

import java.util.ArrayList;
public class CcigTwME extends AbstractGameAction{public boolean sc=true;private static final UIStrings uiStrings;public static final String[] TEXT;private AbstractPlayer p;private boolean carvana=false;CardGroup tmp=new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);int rr=0;private ArrayList<CosmicZoneAbstractGameAction>action;private int key;
    public CcigTwME(int amount, CardGroup tmp,ArrayList<CosmicZoneAbstractGameAction>action, int key){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.tmp=tmp;this.key=key;this.action=action;rr=0;}
    public CcigTwME(int amount, CardGroup tmp,ArrayList<CosmicZoneAbstractGameAction>action, int key, int rr){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.tmp=tmp;this.key=key;this.action=action;this.rr=rr;}
    public void update(){AbstractCard card;if(sc){sc=false;
            CardGroup gon=new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);if(rr==0){for(AbstractCard c:tmp.group){gon.addToBottom(c);}}else{gon.group.addAll(CZGetAll.getCG().group);}
            if(this.amount==0 || gon.size()==0){this.isDone=true;return;}
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            if(key==0){AbstractDungeon.gridSelectScreen.open(gon,Math.min(amount,gon.size()),"Add Des Later",false,false,true,false);
            }else{if(gon.size()<=amount){AbstractDungeon.gridSelectScreen.selectedCards.addAll(gon.group);
            }else{AbstractDungeon.gridSelectScreen.open(gon,amount,"Add Des Later",false);}}
            for(AbstractCard c:AbstractDungeon.gridSelectScreen.targetGroup.group){c.stopGlowing();}//Some of these seem unnecessary but I like rr and this line but for some reason I need them in this mod and I don't know why
        }else{if(AbstractDungeon.gridSelectScreen.selectedCards.size()!=0){
            for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){for(CosmicZoneAbstractGameAction ac:action){CosmicZoneAbstractGameAction aC=ac.makeCopy();aC.card=c;addToTop(aC);}}
            AbstractDungeon.gridSelectScreen.selectedCards.clear();this.p.hand.refreshHandLayout();
        }tickDuration();}}
    static{uiStrings=CardCrawlGame.languagePack.getUIString("AnyCardFromDeckToHandAction");TEXT=uiStrings.TEXT;}}