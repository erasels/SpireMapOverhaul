package spireMapOverhaul.zones.invasion;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zones.invasion.monsters.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvasionZone extends AbstractZone implements EncounterModifyingZone {
    public static final String ID = "Invasion";
    private static final String STYGIAN_BOAR_AND_WHISPERING_WRAITH = SpireAnniversary6Mod.makeID("STYGIAN_BOAR_AND_WHISPERING_WRAITH");
    private static final String DREAD_MOTH_AND_GRAFTED_WORMS = SpireAnniversary6Mod.makeID("DREAD_MOTH_AND_GRAFTED_WORMS");
    private static final String THREE_ELEMENTALS = SpireAnniversary6Mod.makeID("THREE_ELEMENTALS");
    private static final String VOID_CORRUPTION_AND_ORB_OF_FIRE = SpireAnniversary6Mod.makeID("VOID_CORRUPTION_AND_ORB_OF_FIRE");
    private static final String THREE_HATCHLINGS = SpireAnniversary6Mod.makeID("THREE_HATCHLINGS");

    public InvasionZone() {
        super(ID, Icons.MONSTER, Icons.ELITE, Icons.SHOP);
        this.width = 3;
        this.height = 3;
    }

    @Override
    public AbstractZone copy() {
        return new InvasionZone();
    }

    @Override
    public Color getColor() {
        return Color.BLUE.cpy();
    }

    @Override
    public boolean canSpawn() {
        //The enemies in this zone are invaders from these acts, so it doesn't make sense to spawn the zone in them
        return !Arrays.asList("Menagerie:Menagerie", "Elementarium:Elementarium", "Abyss:Abyss").contains(AbstractDungeon.id);
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        //Since this zone always has an elite, we don't want it to show up in the rows of the act that never have elites
        //It also has normal fights that are tuned as hard pool fights, so that's another reason
        return false;
    }

    @Override
    public void distributeRooms(Random rng, ArrayList<AbstractRoom> roomList) {
        //Guarantee at least one elite
        placeRoomRandomly(rng, roomOrDefault(roomList, (room)->room instanceof MonsterRoomElite, MonsterRoomElite::new));
    }

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        return Arrays.asList(
            new ZoneEncounter(Hexasnake.ID, 1, () -> new Hexasnake(0.0f, 0.0f)),
            new ZoneEncounter(STYGIAN_BOAR_AND_WHISPERING_WRAITH, 1, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new WhisperingWraith(-350.0F, 0.0F),
                        new StygianBoar(0.0F, 0.0F)
                })),
            new ZoneEncounter(DREAD_MOTH_AND_GRAFTED_WORMS, 1, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new GraftedWorm(-550.0F, 0.0F),
                        new GraftedWorm(-300.0F, 0.0F),
                        new GraftedWorm(-50.0F, 0.0F),
                        new DreadMoth(200.0F, 125.0F),
                })),
            new ZoneEncounter(THREE_ELEMENTALS, 2, () -> new MonsterGroup(generateElementalGroup())),
            new ZoneEncounter(VOID_CORRUPTION_AND_ORB_OF_FIRE, 2,  () -> new MonsterGroup(
                new AbstractMonster[] {
                        new OrbOfFire(-350.0F, 125.0F),
                        new VoidCorruption(0.0F, 0.0F)
                })),
            new ZoneEncounter(THREE_HATCHLINGS, 3, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Hatchling(-450.0F, 10.0F, false),
                        new Hatchling(-150.0F, -30.0F, false),
                        new Hatchling(150.0F, 20.0F, false)
                }))
        );
    }

    private static AbstractMonster[] generateElementalGroup() {
        float[] groupPositions = {-450.0F, -200.0F, 50.0F};
        ArrayList<String> monstersList = new ArrayList<>();
        monstersList.add(OrbOfFire.ID);
        monstersList.add(LivingStormcloud.ID);
        monstersList.add(OpulentOffering.ID);
        monstersList.add(ShimmeringMirage.ID);
        Collections.shuffle(monstersList, new java.util.Random(AbstractDungeon.monsterRng.randomLong()));

        AbstractMonster[] monsters = new AbstractMonster[3];
        for (int i = 0; i < 3; i++) {
            monsters[i] = getElemental(monstersList.get(i), groupPositions[i], 125.0F);
        }

        return monsters;
    }

    public static AbstractMonster getElemental(String elementalID, float x, float y) {
        if (elementalID.equals(OrbOfFire.ID)) {
            return new OrbOfFire(x, y);
        }
        if (elementalID.equals(LivingStormcloud.ID)) {
            return new LivingStormcloud(x, y);
        }
        if (elementalID.equals(OpulentOffering.ID)) {
            return new OpulentOffering(x, y);
        }
        if (elementalID.equals(ShimmeringMirage.ID)) {
            return new ShimmeringMirage(x, y);
        }
        SpireAnniversary6Mod.logger.warn("Didn't match any elemental. ElementalID:" + elementalID);
        return new OrbOfFire(x, y);
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        return Arrays.asList(
            new ZoneEncounter(Hydra.ID, 1, () -> new Hydra(0.0f, 0.0f)),
            new ZoneEncounter(VoidReaper.ID, 1, () -> new VoidReaper(0.0f, 0.0f))
        );
    }

    @Override
    public List<AbstractMonster> getAdditionalMonsters() {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
            // It's best to stick to adding only one additional enemy like this example, because the positioning
            // logic isn't perfect and there's only so much space on the screen.
            return Collections.singletonList(new LouseNormal(0.0f, 0.0f));
        }
        return null;
    }
}
