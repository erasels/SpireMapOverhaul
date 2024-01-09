package spireMapOverhaul.zones.volatileGrounds;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.*;
import spireMapOverhaul.zones.invasion.monsters.Hexasnake;
import spireMapOverhaul.zones.invasion.monsters.StygianBoar;
import spireMapOverhaul.zones.invasion.monsters.WhisperingWraith;
import spireMapOverhaul.zones.volatileGrounds.monsters.Eruptor;

import java.util.Arrays;
import java.util.List;

public class VolatileGrounds extends AbstractZone implements EncounterModifyingZone {
    private static final String VOL_TEST = SpireAnniversary6Mod.makeID("BLASTER_TEST");
    public VolatileGrounds() {
        super("VolatileGrounds", Icons.ELITE, Icons.MONSTER);
        this.width = 1;
        this.height = 4;
        System.out.println("Volatile Grounds" + name + " " + width + "x" + height);
    }
    
    @Override
    public AbstractZone copy() {
        return new VolatileGrounds();
    }
    
    @Override
    public Color getColor() {
        return Color.ORANGE.cpy();
    }
    
    @Override
    public boolean canSpawn() {
        return isAct(3);
    }
    
    @Override
    public List<ZoneEncounter> getNormalEncounters() {
            return Arrays.asList(
                    new ZoneEncounter(VOL_TEST, 1, () -> new MonsterGroup(
                            new AbstractMonster[] {
                                    new Eruptor(0.0F, 0.0F)
                            }))
            );
    }
    
    protected boolean canIncludeEarlyRows() {
        return false;
    }
}
