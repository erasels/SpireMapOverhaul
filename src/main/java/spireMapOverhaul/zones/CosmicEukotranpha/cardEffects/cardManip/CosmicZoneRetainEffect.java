package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;

import java.util.ArrayList;
public class CosmicZoneRetainEffect extends CosmicZoneAbstractGameAction{public boolean sc=true;public AbstractPlayer p=AbstractDungeon.player;private static final UIStrings uiStrings;public static final String[] TEXT;private static int amount;ArrayList<AbstractCard> co=new ArrayList<>();
    public CosmicZoneRetainEffect(int amount){this.amount=amount;this.actionType=ActionType.CARD_MANIPULATION;this.duration=this.startDuration=Settings.ACTION_DUR_XFAST;}
    public CosmicZoneRetainEffect makeCopy(){
        return new CosmicZoneRetainEffect(amount);}
    public void update(){if(sc){sc=false;if(amount<1||p.hand.size()==0){this.isDone=true;return;}
        for(AbstractCard c:AbstractDungeon.player.hand.group){if(c.retain || c.selfRetain){co.add(c);}}
        if(co.size()==AbstractDungeon.player.hand.size()){this.isDone=true;return;}
        AbstractDungeon.player.hand.group.removeAll(co);
        AbstractDungeon.handCardSelectScreen.open(TEXT[0],amount,true,true);
    }else{if(!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved){
        for(AbstractCard c:co){AbstractDungeon.player.hand.addToTop(c);}for(AbstractCard c:AbstractDungeon.handCardSelectScreen.selectedCards.group){c.retain=true;p.hand.addToTop(c);}
        AbstractDungeon.handCardSelectScreen.selectedCards.clear();AbstractDungeon.handCardSelectScreen.wereCardsRetrieved=true;}tickDuration();}}
    static{uiStrings=CardCrawlGame.languagePack.getUIString("RetainCardsAction");TEXT=uiStrings.TEXT;}}