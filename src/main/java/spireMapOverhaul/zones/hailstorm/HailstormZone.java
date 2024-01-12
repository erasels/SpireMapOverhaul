package spireMapOverhaul.zones.hailstorm;

import basemod.AutoAdd;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.city.Taskmaster;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zones.example.CoolExampleEvent;
import spireMapOverhaul.zones.hailstorm.monsters.FrostSlime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AutoAdd.Ignore
public class HailstormZone extends AbstractZone implements ModifiedEventRateZone, RenderableZone, EncounterModifyingZone {
    public static final String ID = "Hailstorm";
    private final int width, height;
    private final Color color;

    public static final String Frost_Slime = SpireAnniversary6Mod.makeID("Frost_Slime");

    @Override
    public AbstractEvent forceEvent() {
        return ModifiedEventRateZone.returnIfUnseen(CoolExampleEvent.ID);
    }

    @Override
    public float zoneSpecificEventRate() {
        return 1;
    }

    public HailstormZone() {
        this("Hailstorm", 2, 4);
        System.out.println("Placeholder Zone " + name + " " + width + "x" + height);
    }

    @Override
    public AbstractZone copy() {
        return new HailstormZone(name, width, height);
    }

    private HailstormZone(String name, int width, int height) {
        super(ID, Icons.MONSTER, Icons.EVENT, Icons.REST);

        this.width = width;
        this.height = height;

        color = new Color(MathUtils.random(1f), MathUtils.random(1f), MathUtils.random(1f), 1);
        this.name = name;
    }

    @Override
    protected boolean allowAdditionalPaths() {
        return false;
    }

    @Override
    public boolean generateMapArea(BetterMapGenerator.MapPlanner planner) {
        return generateNormalArea(planner, width, height);
    }

    @Override
    public Color getColor() { //I considered changing this to a variable, but a method lets you do funky stuff like a rainbow zone that changes colors or something.
        return color;
    }

    @Override
    public void manualRoomPlacement(Random rng) {
        //set all nodes to a specific room.
        /*for (MapRoomNode node : nodes) {
            node.setRoom(new EventRoom());//new MonsterRoomElite());
        }*/
    }

    @Override
    public void distributeRooms(Random rng, ArrayList<AbstractRoom> roomList) {
        //Guarantee at least One Elite Room in zone. This method will do nothing if the zone is already full.
        //placeRoomRandomly(rng, roomOrDefault(roomList, (room)->room instanceof MonsterRoomElite, MonsterRoomElite::new));
    }

    @Override
    public void replaceRooms(Random rng) {
        //Replace 100% of event rooms with treasure rooms.
        //replaceRoomsRandomly(rng, TreasureRoom::new, (room)->room instanceof EventRoom, 1);
    }

    @Override
    public void renderBackground(SpriteBatch sb) {
        // Render things in the background when this zone is active.
    }

    @Override
    public void renderForeground(SpriteBatch sb) {
        // Render things in the foreground when this zone is active.
    }

    @Override
    public void update() {
        // Update things when this zone is active.
    }

    @Override
    public boolean canSpawn() {
        return this.isAct(1);
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
    }

//    @Override
//    public void registerEncounters() {
//        EncounterModifyingZone.super.registerEncounters();
//        BaseMod.addMonster(Frost_Slime, () -> new MonsterGroup(
//                new AbstractMonster[] {
//                        new FrostSlime(0.0f, 0.0f),
//                }
//        ));
//    }

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        return Collections.singletonList(
                new ZoneEncounter(Frost_Slime, 1, () -> new MonsterGroup(
                        new AbstractMonster[]{
                                new FrostSlime(0.0f, 0.0f),
                        }))
        );
    }
}
