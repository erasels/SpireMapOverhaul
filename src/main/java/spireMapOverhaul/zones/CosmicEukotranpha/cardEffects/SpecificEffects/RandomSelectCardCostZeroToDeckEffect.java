package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;

import java.util.ArrayList;
public class RandomSelectCardCostZeroToDeckEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy()
{return new RandomSelectCardCostZeroToDeckEffect(amount);}public boolean retrieveCard=false;
    public RandomSelectCardCostZeroToDeckEffect(int a){this.amount=a;}
    public void update(){if(AbstractDungeon.getMonsters().areMonstersBasicallyDead()||amount<1){this.isDone=true;
        }else if(sc){sc=false;
            AbstractDungeon.cardRewardScreen.customCombatOpen(this.generateCardChoices(),"TODO: TEXT",false);
        }else{if(!this.retrieveCard){
                if(AbstractDungeon.cardRewardScreen.discoveryCard!=null){
                    AbstractCard codexCard=AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    codexCard.current_x=-1000.0F*Settings.xScale;if(codexCard.cost>0){codexCard.isCostModified=true;}codexCard.cost=0;codexCard.costForTurn=0;codexCard.chargeCost=0;
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(codexCard,(float)Settings.WIDTH/2.0F,(float)Settings.HEIGHT/2.0F,true));
                    AbstractDungeon.cardRewardScreen.discoveryCard=null;
                }this.retrieveCard=true;}
            this.tickDuration();}}
    private ArrayList<AbstractCard>generateCardChoices(){ArrayList<AbstractCard>derp=new ArrayList();
        while(derp.size()!=amount){boolean dupe=false;
            AbstractCard tmp=AbstractDungeon.returnTrulyRandomCardInCombat();
            for(AbstractCard c:derp){if(c.cardID.equals(tmp.cardID)){dupe=true;break;}}
            if(!dupe){derp.add(tmp.makeCopy());}}
        return derp;}}