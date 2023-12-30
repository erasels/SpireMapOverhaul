package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.*;
import spireMapOverhaul.zones.CosmicEukotranpha.orbs.*;
public class LunaFantasiaChannelOrbEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy()
{return new LunaFantasiaChannelOrbEffect(monster);}public AbstractMonster monster;
    public LunaFantasiaChannelOrbEffect(AbstractMonster mon){this.monster=mon;}
    public void update(){if(AbstractDungeon.getMonsters().areMonstersBasicallyDead()||monster==null){return;}
        if(monster instanceof InsectHydoail){CosmicZoneMod.logger.info("Hydoail");att(new ChannelAction(new MercuryOrb()));
            //TODO: Orbs
        }else if(monster instanceof InsectGoldire){CosmicZoneMod.logger.info("Goldire");att(new ChannelAction(new VenusOrb()));
        }else if(monster instanceof InsectTerniff){CosmicZoneMod.logger.info("Terniff");att(new ChannelAction(new EarthOrb()));
        }else if(monster instanceof InsectPyergy){CosmicZoneMod.logger.info("Pyergy");att(new ChannelAction(new MarsOrb()));
        }else if(monster instanceof InsectBarceq){CosmicZoneMod.logger.info("Barceq");att(new ChannelAction(new JupiterOrb()));
        }else if(monster instanceof InsectDrittu){CosmicZoneMod.logger.info("Drittu");att(new ChannelAction(new SaturnOrb()));
        }else if(monster instanceof InsectHeazath){CosmicZoneMod.logger.info("Heazath");att(new ChannelAction(new GeorgeOrb()));
        }else if(monster instanceof InsectOcianyvy){CosmicZoneMod.logger.info("Ocianyvy");att(new ChannelAction(new NeptuneOrb()));
        }else if(monster instanceof Queen){CosmicZoneMod.logger.info("Queen");att(new ChannelAction(new QueenOrb()));
        }else{CosmicZoneMod.logger.info("Other");att(new ChannelAction(new MirageOrb()));
        }
        this.isDone=true;}}