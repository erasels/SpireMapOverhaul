package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;

import java.util.ArrayList;
import java.util.Objects;
public class RandomSelectionAddCardToHandAndOthersToDeckEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy()
{return new RandomSelectionAddCardToHandAndOthersToDeckEffect(amount);}public boolean retrieveCard=false;public ArrayList<AbstractCard>gennedArrayList;
    public RandomSelectionAddCardToHandAndOthersToDeckEffect(int a){this.amount=a;}
    public void update(){if(AbstractDungeon.getMonsters().areMonstersBasicallyDead()||amount<1){this.isDone=true;
    }else if(sc){sc=false;gennedArrayList=this.generateCardChoices();
        AbstractDungeon.cardRewardScreen.customCombatOpen(gennedArrayList,"TODO: TEXT",false);
    }else{if(!this.retrieveCard){
        if(AbstractDungeon.cardRewardScreen.discoveryCard!=null){
            AbstractCard codexCard=AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
            codexCard.current_x=-1000.0F*Settings.xScale;
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(codexCard,(float)Settings.WIDTH/2.0F,(float)Settings.HEIGHT/2.0F));
            for(AbstractCard c:gennedArrayList){if(!Objects.equals(c.originalName,codexCard.originalName)){
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(c,(float)Settings.WIDTH/2.0F,(float)Settings.HEIGHT/2.0F,true));}}
            AbstractDungeon.cardRewardScreen.discoveryCard=null;
        }this.retrieveCard=true;}
        this.tickDuration();}}
    private ArrayList<AbstractCard>generateCardChoices(){ArrayList<AbstractCard>derp=new ArrayList();
        while(derp.size()!=amount){boolean dupe=false;
            AbstractCard tmp=AbstractDungeon.returnTrulyRandomCardInCombat();
            for(AbstractCard c:derp){if(c.cardID.equals(tmp.cardID)){dupe=true;break;}}
            if(!dupe){derp.add(tmp.makeCopy());}}
        return derp;}}