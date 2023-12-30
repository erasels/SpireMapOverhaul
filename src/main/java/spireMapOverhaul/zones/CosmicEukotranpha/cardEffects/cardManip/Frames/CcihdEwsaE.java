package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.Frames;/*ChooseCardsInGroupToTargetWithEffect*/
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CcihdEwsaE extends AbstractGameAction{public String[]txt;public boolean sc=true;private static final UIStrings uiStrings;public static final String[] TEXT;private AbstractPlayer p;private boolean carvana=false;private CosmicZoneAbstractGameAction action;private AbstractGameAction action2;private int key;
    public CcihdEwsaE(int amount,CosmicZoneAbstractGameAction action,AbstractGameAction action2, int key){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.action2=action2;this.key=key;this.action=action;this.txt=new String[]{""};}
    public CcihdEwsaE(int amount,CosmicZoneAbstractGameAction action,AbstractGameAction action2, int key,String[]txt){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.action2=action2;this.key=key;this.action=action;this.txt=txt;}
    public void update(){AbstractCard card;if(sc){sc=false;
        if(this.amount==0 || AbstractDungeon.player.hand.size()==0){this.isDone=true;return;}
        if(key==0){AbstractDungeon.handCardSelectScreen.open(txt[0],amount,true);
        }else{if(AbstractDungeon.player.hand.size()<=amount){AbstractDungeon.handCardSelectScreen.selectedCards.group.addAll(AbstractDungeon.player.hand.group);AbstractDungeon.handCardSelectScreen.wereCardsRetrieved=false;
        }else{AbstractDungeon.handCardSelectScreen.open(txt[0],amount,false);}}
    }else{if(AbstractDungeon.handCardSelectScreen.selectedCards.size()!=0){
        for(AbstractCard c:AbstractDungeon.handCardSelectScreen.selectedCards.group){CosmicZoneAbstractGameAction ac=action.makeCopy();ac.card=c;addToTop(ac);p.hand.addToTop(c);}
        action2.amount=AbstractDungeon.handCardSelectScreen.selectedCards.size();addToTop(action2);
        AbstractDungeon.handCardSelectScreen.selectedCards.clear();this.p.hand.refreshHandLayout();
    }tickDuration();}}
    static{uiStrings=CardCrawlGame.languagePack.getUIString("AnyCardFromDeckToHandAction");TEXT=uiStrings.TEXT;}}
