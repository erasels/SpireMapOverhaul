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
public class CcigdEwsaE extends AbstractGameAction{public boolean sc=true;private static final UIStrings uiStrings;public static final String[] TEXT;private AbstractPlayer p;private boolean carvana=false;CardGroup tmp;private CosmicZoneAbstractGameAction action;private AbstractGameAction action2;private int key;
    public CcigdEwsaE(int amount, CardGroup tmp, CosmicZoneAbstractGameAction action, AbstractGameAction action2, int key){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.action2=action2;this.tmp=tmp;this.key=key;this.action=action;}
    public void update(){
        AbstractCard card;
        if(sc){sc=false;
            if(this.amount==0 || tmp.size()==0){this.isDone=true;return;}
            if(key==0){AbstractDungeon.gridSelectScreen.open(tmp,Math.min(amount,tmp.size()),true,"Add Des Later");
            }else{if(tmp.size()<=amount){AbstractDungeon.gridSelectScreen.selectedCards.addAll(tmp.group);
            }else{AbstractDungeon.gridSelectScreen.open(tmp,amount,"Add Des Later",false);}}
        }else{if(AbstractDungeon.gridSelectScreen.selectedCards.size()!=0){
            for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){action.card=c;CosmicZoneAbstractGameAction ac=action.makeCopy();ac.card=c;addToTop(ac);}
            action2.amount=AbstractDungeon.handCardSelectScreen.selectedCards.size();addToTop(action2);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();this.p.hand.refreshHandLayout();
        }tickDuration();}}
    static{uiStrings=CardCrawlGame.languagePack.getUIString("AnyCardFromDeckToHandAction");TEXT=uiStrings.TEXT;}}
