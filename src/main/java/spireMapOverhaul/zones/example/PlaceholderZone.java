package spireMapOverhaul.zones.example;

import basemod.AutoAdd;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;

import java.util.ArrayList;

@AutoAdd.Ignore
public class PlaceholderZone extends AbstractZone implements ModifiedEventRateZone {
    public static final String ID = "Placeholder";

    private final int width, height;
    private final Color color;

    @Override
    public AbstractEvent forceEvent() {
        return ModifiedEventRateZone.returnIfUnseen(CoolExampleEvent.ID);
    }

    @Override
    public float zoneSpecificEventRate() {
        return 1;
    }

    public PlaceholderZone() {
        this("Placeholder 0", 2, 4);
        System.out.println("Placeholder Zone " + name + " " + width + "x" + height);
    }

    @Override
    public AbstractZone copy() {
        return new PlaceholderZone(name, width, height);
    }

    private PlaceholderZone(String name, int width, int height) {
        super(ID);

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
}
