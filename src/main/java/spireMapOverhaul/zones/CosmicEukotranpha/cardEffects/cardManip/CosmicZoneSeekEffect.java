package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CosmicZoneSeekEffect extends CosmicZoneAbstractGameAction{public boolean sc=true;boolean restToDp=false;private static final UIStrings uiStrings;public static final String[] TEXT;private AbstractPlayer p;private boolean carvana=false;
    public CosmicZoneSeekEffect(int amount){this.amount=amount;this.p=AbstractDungeon.player;this.actionType=ActionType.CARD_MANIPULATION;this.duration=Settings.ACTION_DUR_XFAST;}
    public CosmicZoneSeekEffect makeCopy(){
        return new CosmicZoneSeekEffect(amount);}
    public void update(){if(sc){sc=false;if(amount<1||p.drawPile.isEmpty()){this.isDone=true;return;}CardGroup tmp=new CardGroup(CardGroupType.UNSPECIFIED);
        for(AbstractCard c:p.drawPile.group){tmp.addToRandomSpot(c);}
        if(BaseMod.MAX_HAND_SIZE-p.hand.size()<amount){if(tmp.size()>amount){AbstractDungeon.gridSelectScreen.open(tmp,amount,"TODO: Seek Text"+amount+(BaseMod.MAX_HAND_SIZE-p.hand.size()),false,false,false,false);
        }else{restToDp=true;AbstractDungeon.gridSelectScreen.open(tmp,BaseMod.MAX_HAND_SIZE-p.hand.size(),"TODO: Seek Text"+amount,false,false,false,false);}
        }else{if(tmp.size()>amount){AbstractDungeon.gridSelectScreen.open(tmp,amount,"TODO: Seek Text"+amount,false,false,false,false);}else{AbstractDungeon.gridSelectScreen.selectedCards.addAll(tmp.group);}}
    }else{if(AbstractDungeon.gridSelectScreen.selectedCards.size()!=0){
        for(AbstractCard card:AbstractDungeon.gridSelectScreen.selectedCards){card.unhover();
            if(p.hand.size()==BaseMod.MAX_HAND_SIZE){p.drawPile.moveToDiscardPile(card);p.createHandIsFullDialog();}else{p.drawPile.removeCard(card);p.hand.addToTop(card);}
            p.hand.refreshHandLayout();p.hand.applyPowers();}
        if(restToDp){AbstractDungeon.gridSelectScreen.selectedCards.clear();AbstractDungeon.gridSelectScreen.selectedCards.addAll(p.drawPile.group);
            for(AbstractCard card:AbstractDungeon.gridSelectScreen.selectedCards){card.unhover();
                if(p.hand.size()==BaseMod.MAX_HAND_SIZE){p.drawPile.moveToDiscardPile(card);p.createHandIsFullDialog();}else{p.drawPile.removeCard(card);p.hand.addToTop(card);}
                p.hand.refreshHandLayout();p.hand.applyPowers();}}
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        this.p.hand.refreshHandLayout();}tickDuration();}}
    static{uiStrings=CardCrawlGame.languagePack.getUIString("AnyCardFromDeckToHandAction");TEXT=uiStrings.TEXT;}}