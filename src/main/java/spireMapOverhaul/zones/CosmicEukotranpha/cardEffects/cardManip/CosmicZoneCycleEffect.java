package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CosmicZoneCycleEffect extends CosmicZoneAbstractGameAction{public boolean sc=true;private static final UIStrings uiStrings;public static final String[] TEXT;private AbstractPlayer p;private boolean isRandom;public static int numDiscarded;private boolean optional;private static int amount;
    public CosmicZoneCycleEffect(int amount){this(amount,false,false);}public CosmicZoneCycleEffect(int amount, boolean isRandom){this(amount,isRandom,false);}
    public CosmicZoneCycleEffect(int amount,boolean isRandom,boolean optional){this.p=AbstractDungeon.player;this.isRandom=isRandom;this.actionType=ActionType.DISCARD;this.duration=this.startDuration=Settings.ACTION_DUR_XFAST;this.amount=amount;this.optional=optional;}
    public CosmicZoneCycleEffect makeCopy(){
        return new CosmicZoneCycleEffect(amount,isRandom,optional);}
    public void update(){AbstractCard c;if(sc){sc=false;
        if(AbstractDungeon.getMonsters().areMonstersBasicallyDead()||amount==0){this.isDone=true;return;}
        if(isRandom){for(int i=0;i<amount;++i){c=p.hand.getRandomCard(AbstractDungeon.cardRandomRng);p.hand.moveToDiscardPile(c);c.triggerOnManualDiscard();
            GameActionManager.incrementDiscard(false);addToTop(new DrawCardAction(1));}this.isDone=true;return;}
        if(optional){AbstractDungeon.handCardSelectScreen.open("TODO: Cycle Text",amount,true,true,false,false,true);
        }else{if(amount>=p.hand.size()){for(AbstractCard co:p.hand.group){AbstractDungeon.handCardSelectScreen.selectedCards.addToBottom(co);}}else{AbstractDungeon.handCardSelectScreen.open("TODO: Cycle Text",amount,false,false,false,false,false);}}
    }else if(!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved){
        for(AbstractCard co:AbstractDungeon.handCardSelectScreen.selectedCards.group){p.hand.moveToDiscardPile(co);co.triggerOnManualDiscard();
            GameActionManager.incrementDiscard(false);addToTop(new DrawCardAction(1));}
        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved=true;tickDuration();}else{tickDuration();}}
    static{uiStrings=CardCrawlGame.languagePack.getUIString("DiscardAction");TEXT=uiStrings.TEXT;}}