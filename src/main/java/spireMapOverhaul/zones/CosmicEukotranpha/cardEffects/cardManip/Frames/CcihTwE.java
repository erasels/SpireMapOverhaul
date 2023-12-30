package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.Frames;/*ChooseCardsInHandToTargetWithEffect*/
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CcihTwE extends CosmicZoneAbstractGameAction{private static final UIStrings uiStrings;public static final String[] TEXT;private AbstractPlayer p;private boolean carvana=false;private CosmicZoneAbstractGameAction action;private int key;public String[]kaph;
    public CcihTwE(int amount,CosmicZoneAbstractGameAction action,int key){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.key=key;this.action=action;kaph=new String[]{""};}
    public CcihTwE(int amount,CosmicZoneAbstractGameAction action,int key,String[]kaph){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.key=key;this.action=action;this.kaph=kaph;}
    public void update(){if(sc){sc=false;if(this.amount==0||AbstractDungeon.player.hand.size()==0){this.isDone=true;return;}
            if(key==0){AbstractDungeon.handCardSelectScreen.open(kaph[0],amount,true,true);
            }else{if(AbstractDungeon.player.hand.size()<=amount){AbstractDungeon.handCardSelectScreen.selectedCards.group.addAll(AbstractDungeon.player.hand.group);AbstractDungeon.handCardSelectScreen.wereCardsRetrieved=false;
            }else{AbstractDungeon.handCardSelectScreen.open(kaph[0],amount,false);}}
        }else{if(!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved){
            for(AbstractCard c:AbstractDungeon.handCardSelectScreen.selectedCards.group){CosmicZoneAbstractGameAction ac=action.makeCopy();ac.card=c;addToTop(ac);p.hand.addToTop(c);}
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();this.p.hand.refreshHandLayout();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved=true;
        }tickDuration();}}
    static{uiStrings=CardCrawlGame.languagePack.getUIString("AnyCardFromDeckToHandAction");TEXT=uiStrings.TEXT;}public CosmicZoneAbstractGameAction makeCopy(){return new CcihTwE(amount,action,key,kaph);}}
