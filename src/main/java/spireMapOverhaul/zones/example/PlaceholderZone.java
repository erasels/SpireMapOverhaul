package spireMapOverhaul.zones.example;

import basemod.AutoAdd;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;

import java.util.ArrayList;

@AutoAdd.Ignore
public class PlaceholderZone extends AbstractZone implements ModifiedEventRateZone, RenderableZone {
    public static final String ID = "Placeholder";

    @Override
    public String forceEvent() {
        return ModifiedEventRateZone.returnIfUnseen(CoolExampleEvent.ID);
    }

    @Override
    public float zoneSpecificEventRate() {
        return 1;
    }

    public PlaceholderZone() {
        super(ID, Icons.MONSTER, Icons.CHEST, Icons.EVENT, Icons.REST);
        this.width = 3;
        this.maxWidth = 4;
        this.height = 3;
        this.maxHeight = 4;
    }

    @Override
    public AbstractZone copy() {
        return new PlaceholderZone();
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
    public Color getColor() {
        return Color.BLACK.cpy();
    }

    @Override
    public void manualRoomPlacement(Random rng) {
        //set all nodes to a specific room.
        /*for (MapRoomNode node : nodes) {
            node.setRoom(new EventRoom());
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
}
