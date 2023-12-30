package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CosmicZoneExhumeEffect extends CosmicZoneAbstractGameAction{public boolean sc=true;public AbstractPlayer p=AbstractDungeon.player;private boolean optional;public CosmicZoneExhumeEffect(int amount, boolean optional){this.amount=amount;this.duration=0.01F;this.optional=optional;}
    public CosmicZoneExhumeEffect makeCopy(){
        return new CosmicZoneExhumeEffect(amount,optional);}
    public void update(){
        if(sc){sc=false;amount=Math.min(BaseMod.MAX_HAND_SIZE-p.hand.size(),amount);
            if(p.hand.size()==BaseMod.MAX_HAND_SIZE||p.exhaustPile.isEmpty()||amount==0){this.isDone=true;return;}
            if(optional){AbstractDungeon.gridSelectScreen.open(p.exhaustPile,amount,true,"TODO: Exhume Text"+amount);
            }else{if(p.exhaustPile.size()>amount){AbstractDungeon.gridSelectScreen.open(p.exhaustPile,amount,"TODO: Exhume Text"+amount,false,false,false,false);}else{AbstractDungeon.gridSelectScreen.selectedCards.addAll(p.exhaustPile.group);}}
        }else{if(!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
            for(AbstractCard co:AbstractDungeon.gridSelectScreen.selectedCards){p.hand.addToHand(co);
                if(p.hasPower("Corruption")&&co.type==AbstractCard.CardType.SKILL){co.setCostForTurn(-9);}
                p.exhaustPile.removeCard(co);co.unhover();}
            AbstractDungeon.gridSelectScreen.selectedCards.clear();p.hand.refreshHandLayout();
            for(AbstractCard c:p.exhaustPile.group){c.unhover();c.target_x=(float)CardGroup.DISCARD_PILE_X;c.target_y=0.0F;}
        }tickDuration();}}}