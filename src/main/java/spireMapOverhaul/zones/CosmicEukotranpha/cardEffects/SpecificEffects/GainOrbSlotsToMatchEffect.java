package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class GainOrbSlotsToMatchEffect extends CosmicZoneAbstractGameAction{
    public GainOrbSlotsToMatchEffect(int amount){this.amount=amount;}public CosmicZoneAbstractGameAction makeCopy(){
        return new GainOrbSlotsToMatchEffect(amount);}
    public void update(){for(int i=p.maxOrbs;i<amount;i++){AbstractDungeon.player.increaseMaxOrbSlots(1,true);}this.isDone=true;}}