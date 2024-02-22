package spireMapOverhaul.zones.mycelium;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.Exploder;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zones.mycelium.monsters.FungalFriend;
import spireMapOverhaul.zones.mycelium.monsters.MutatedWhaleSpawn;
import spireMapOverhaul.zones.volatileGrounds.monsters.Eruptor;
import spireMapOverhaul.zones.volatileGrounds.monsters.GremlinArchmage;
import spireMapOverhaul.zones.volatileGrounds.monsters.HeatBlister;

import java.util.Arrays;
import java.util.List;

public class Mycelium extends AbstractZone implements EncounterModifyingZone {
    public static final String ID = "Mycelium";
    private static final String MUTATED_WHALE_SPAWN = SpireAnniversary6Mod.makeID("MUTATED_WHALE_SPAWN");
    private static final String INFECTED_AUTOMATA = SpireAnniversary6Mod.makeID("INFECTED_AUTOMATA");
    
    public Mycelium() {
        super(ID, Icons.MONSTER, Icons.EVENT);
        this.width = 2;
        this.height = 4;
    }
    
    @Override
    public AbstractZone copy() {
        return new Mycelium();
    }
    
    @Override
    public Color getColor() {
        return new Color(0.043f, 0.855f, 0.522f, 1f);
    }
    
    @Override
    public boolean canSpawn() {
        return isAct(1);
    }
    
    protected boolean canIncludeEarlyRows() {
        return false;
    }
    
    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        return Arrays.asList(
                new ZoneEncounter(MUTATED_WHALE_SPAWN, 1, () -> new MonsterGroup(
                        new AbstractMonster[]{
                               new MutatedWhaleSpawn()
                        })),
                new ZoneEncounter(INFECTED_AUTOMATA, 1, () -> new MonsterGroup(
                        new AbstractMonster[]{
                                new Sentry(0,0),
                                new FungalFriend(-280, 0)
                        }))
        );
    }
}
