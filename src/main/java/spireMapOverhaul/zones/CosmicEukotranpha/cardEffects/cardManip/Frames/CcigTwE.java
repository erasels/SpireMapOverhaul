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
public class CcigTwE extends AbstractGameAction{public String[]txt;private static final UIStrings uiStrings;public static final String[] TEXT;private AbstractPlayer p;private boolean carvana=false;CardGroup tmp=new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);int rr=0;private CosmicZoneAbstractGameAction action;private int key;private boolean started=true;
    public CcigTwE(int amount,CardGroup tmp,CosmicZoneAbstractGameAction action,int key){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.tmp=tmp;this.key=key;this.action=action;rr=0;this.txt=new String[]{""};}
    public CcigTwE(int amount,CardGroup tmp,CosmicZoneAbstractGameAction action,int key,int rr){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.tmp=tmp;this.key=key;this.action=action;this.rr=rr;this.txt=new String[]{""};}
    public CcigTwE(int amount,CardGroup tmp,CosmicZoneAbstractGameAction action,int key,String[]txt){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.tmp=tmp;this.key=key;this.action=action;this.rr=0;this.txt=txt;}
    public CcigTwE(int amount,CardGroup tmp,CosmicZoneAbstractGameAction action,int key,int rr,String[]txt){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.tmp=tmp;this.key=key;this.action=action;this.rr=rr;this.txt=txt;}
    public void update(){if(started){started=false;CardGroup gon=new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            if(rr==0){for(AbstractCard c:tmp.group){gon.addToBottom(c);}}else{
                if(p.cardInUse!=null){gon.group.add(AbstractDungeon.player.cardInUse);}
                gon.group.addAll(AbstractDungeon.player.drawPile.group);gon.group.addAll(AbstractDungeon.player.discardPile.group);gon.group.addAll(AbstractDungeon.player.exhaustPile.group);
                gon.group.addAll(AbstractDungeon.player.limbo.group);gon.group.addAll(AbstractDungeon.player.hand.group);}
            if(this.amount==0 || gon.size()==0){this.isDone=true;return;}
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            if(key==0){AbstractDungeon.gridSelectScreen.open(gon,Math.min(amount,gon.size()),true,txt[0]);
            }else{if(gon.size()<=amount){AbstractDungeon.gridSelectScreen.selectedCards.addAll(gon.group);
                }else{AbstractDungeon.gridSelectScreen.open(gon,amount,txt[0],false);}}
            for(AbstractCard c:AbstractDungeon.gridSelectScreen.targetGroup.group){if(c!=null){c.stopGlowing();}}
        }else{if(AbstractDungeon.gridSelectScreen.selectedCards.size()!=0){
            for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){CosmicZoneAbstractGameAction ac=action.makeCopy();ac.card=c;addToTop(ac);}
                AbstractDungeon.gridSelectScreen.selectedCards.clear();this.p.hand.refreshHandLayout();
            }this.tickDuration();}}
    static{uiStrings=CardCrawlGame.languagePack.getUIString("AnyCardFromDeckToHandAction");TEXT=uiStrings.TEXT;}}

