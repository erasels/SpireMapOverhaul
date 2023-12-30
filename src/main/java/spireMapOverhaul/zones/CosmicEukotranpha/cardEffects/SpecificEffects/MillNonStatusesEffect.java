package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.CZDiscardTEffect;
public class MillNonStatusesEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy()
{return new MillNonStatusesEffect(amount,countStatuses);}public boolean countStatuses=false;
    public MillNonStatusesEffect(int a,boolean countStatuses){this.amount=a;this.countStatuses=countStatuses;}
    public void update(){if(AbstractDungeon.getMonsters().areMonstersBasicallyDead()||amount<1||p.drawPile.isEmpty()){this.isDone=true;return;
    }else{for(int i=0;i<p.drawPile.size()-1;i++){if(amount<1){break;}if(p.drawPile.group.get(i).type!=AbstractCard.CardType.STATUS){att(new CZDiscardTEffect(p.drawPile.group.get(i)));if(!countStatuses){amount++;}}amount--;}}this.isDone=true;}}