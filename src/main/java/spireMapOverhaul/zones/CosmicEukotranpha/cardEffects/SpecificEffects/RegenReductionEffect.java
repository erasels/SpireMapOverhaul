package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.CosmicZoneMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicZoneGameActionHistory;
public class RegenReductionEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy()
{return new RegenReductionEffect(target);}public RegenReductionEffect(AbstractCreature owner){target=owner;}
public void update(){if(CosmicZoneGameActionHistory.modRegenInteractions==0&&target instanceof CosmicZoneMonster&&target.hasPower("Regeneration")){AbstractPower pow=target.getPower("Regeneration");--pow.amount;if(pow.amount==0){pow.owner.powers.remove(pow);}else{pow.updateDescription();}}this.isDone=true;}}
