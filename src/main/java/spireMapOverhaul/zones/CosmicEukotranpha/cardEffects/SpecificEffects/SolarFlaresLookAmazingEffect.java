package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.SaventStars;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.monsterList;
public class SolarFlaresLookAmazingEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy()
{return new SolarFlaresLookAmazingEffect();}
    public SolarFlaresLookAmazingEffect(){}
    public void update(){for(AbstractMonster mo:monsterList()){if(mo instanceof SaventStars){((SaventStars)mo).seeSolarFlares();}}this.isDone=true;}}
//TODO: SOLAR FLARES