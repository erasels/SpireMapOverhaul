package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class IncreaseCostOfTopOfDeckWithCostNextTurnOnlyEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy()
{return new IncreaseCostOfTopOfDeckWithCostNextTurnOnlyEffect(amount);}public boolean retrieveCard=false;
    public IncreaseCostOfTopOfDeckWithCostNextTurnOnlyEffect(int a){this.amount=a;}
    public void update(){if(AbstractDungeon.getMonsters().areMonstersBasicallyDead()||amount<1||p.drawPile.isEmpty()){this.isDone=true;return;
    }else{for(int i=0;i<p.drawPile.size()-1;i++){if(amount<1){break;}if(p.drawPile.group.get(i).cost>-1){att(new ReduceCostForTurnAction(p.drawPile.group.get(i),-1));amount--;}}}this.isDone=true;}}