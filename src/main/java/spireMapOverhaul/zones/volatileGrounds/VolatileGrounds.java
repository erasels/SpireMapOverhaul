package spireMapOverhaul.zones.volatileGrounds;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.Exploder;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zones.volatileGrounds.monsters.Eruptor;
import spireMapOverhaul.zones.volatileGrounds.monsters.HeatBlister;
import spireMapOverhaul.zones.volatileGrounds.monsters.SunStoneShard;
import spireMapOverhaul.zones.volatileGrounds.monsters.UnstableSunstone;

import java.util.Arrays;
import java.util.List;

public class VolatileGrounds extends AbstractZone implements EncounterModifyingZone {
    private static final String SUNSTONE_ELITE = SpireAnniversary6Mod.makeID("SUNSTONE_ELITE");
    private static final String EXPLOSIVE_SHAPES = SpireAnniversary6Mod.makeID("EXPLOSIVE_SHAPES");
    private static final String HEAT_BLISTER = SpireAnniversary6Mod.makeID("HEAT_BLISTER");
    
    public VolatileGrounds() {
        super("VolatileGrounds", Icons.MONSTER);
        this.width = 1;
        this.height = 4;
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
                new ZoneEncounter(EXPLOSIVE_SHAPES, 3, () -> new MonsterGroup(
                        new AbstractMonster[]{
                                new Eruptor(0.0F, 0.0F),
                                new Eruptor(-320.0F, 0.0F),
                                new Exploder(-160.0F, 0.0F),
                                new Exploder(160.0F, 0.0F)
                        })),
                new ZoneEncounter(HEAT_BLISTER, 3, () -> new MonsterGroup(
                        new AbstractMonster[]{
                                new HeatBlister(0.0F, 0.0F),
                                new Eruptor(-320.0F, 0.0F),
                        }))
        );
    }
    
    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        return Arrays.asList(
                new ZoneEncounter(SUNSTONE_ELITE, 3, () -> new MonsterGroup(
                        new AbstractMonster[]{
                                new SunStoneShard(-300.0F, 0.0f),
                                new UnstableSunstone(0.0F, 0.0F),
                                new SunStoneShard(300.0F, 0.0f),
                        }))
        );
    }
    
    protected boolean canIncludeEarlyRows() {
        return false;
    }
}
