package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.Frames;/*ChooseCardsInGroupToTargetWithEffect*/
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CcihTwEtdE extends AbstractGameAction{public boolean sc=true;public String[]txt;private AbstractPlayer p;private boolean carvana=false;private CosmicZoneAbstractGameAction action;private int key;private AbstractGameAction action2;int mag;
    public CcihTwEtdE(int amount,CosmicZoneAbstractGameAction action,AbstractGameAction action2,int key,int mag,String[] txt){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.key=key;this.action=action;this.action2=action2;this.mag=mag;this.txt=txt;}
    public CcihTwEtdE(int amount,CosmicZoneAbstractGameAction action,AbstractGameAction action2,int key,int mag){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;this.key=key;this.action=action;this.action2=action2;this.mag=mag;this.txt=new String[]{""};}
    public void update(){if(sc){sc=false;if(this.amount==0||AbstractDungeon.player.hand.size()==0){this.isDone=true;return;}
        if(key==0){AbstractDungeon.handCardSelectScreen.open(txt[0],amount,true,true);//Note to self, anyNumber seems to have little to no purpose
        }else{if(AbstractDungeon.player.hand.size()<=amount){AbstractDungeon.handCardSelectScreen.selectedCards.group.addAll(AbstractDungeon.player.hand.group);AbstractDungeon.handCardSelectScreen.wereCardsRetrieved=false;
        }else{AbstractDungeon.handCardSelectScreen.open(txt[0],amount,false);}}
    }else{if(AbstractDungeon.handCardSelectScreen.selectedCards.size()!=0){
        action2.amount=AbstractDungeon.handCardSelectScreen.selectedCards.size()*mag;addToTop(action2);
        for(AbstractCard c:AbstractDungeon.handCardSelectScreen.selectedCards.group){action.card=c;CosmicZoneAbstractGameAction ac=action.makeCopy();ac.card=c;addToTop(ac);p.hand.addToTop(c);}
        AbstractDungeon.handCardSelectScreen.selectedCards.clear();this.p.hand.refreshHandLayout();
    }tickDuration();}}}
