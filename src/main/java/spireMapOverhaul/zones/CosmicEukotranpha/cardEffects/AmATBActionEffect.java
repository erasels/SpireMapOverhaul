package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
public class AmATBActionEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy(){return new AmATBActionEffect(ceevee);}AbstractGameAction ceevee;
    public AmATBActionEffect(AbstractGameAction ceevee){this.ceevee=ceevee;}
    public void update(){if(ceevee==null){this.isDone=true;return;}atbDontUseThisOften(ceevee);this.isDone=true;}}
