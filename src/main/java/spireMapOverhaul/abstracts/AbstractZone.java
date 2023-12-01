package spireMapOverhaul.abstracts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import spireMapOverhaul.BetterMapGenerator.MapPlanner;
import spireMapOverhaul.BetterMapGenerator.MapPlanner.PlanningNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class AbstractZone {
    private static final float OFFSET_X = Settings.isMobile ? 496.0F * Settings.xScale : 560.0F * Settings.xScale;
    private static final float OFFSET_Y = 180.0F * Settings.scale;
    private static final float SPACING_X = Settings.isMobile ? (int)(Settings.xScale * 64.0F) * 2.2F : (int)(Settings.xScale * 64.0F) * 2.0F;

    public final String ID;
    public String name;
    protected Color labelColor = Color.WHITE;

    protected List<MapRoomNode> nodes = new ArrayList<>();
    protected int x = 0, y = 0, width = 1, height = 1;

    //These two should be set during initial generation in generateMapArea
    //They will then be adjusted to their final values in mapGenDone
    protected float labelX = 0, labelY = 0;

    public AbstractZone(String ID) {
        this.ID = ID;
    }

    public abstract AbstractZone copy();

    public String modifyRolledEvent(String eventKey) {
        if (AbstractDungeon.eventRng.randomBoolean(eventChance())) {
            List<String> possibleEvents = events();
            return possibleEvents.remove(AbstractDungeon.eventRng.random(events().size()));
        }
        return eventKey;
    }


    /**
     * @return The chance of an event specifically for this zone appearing, from 0 to 1.
     * Will also be effectively 0 if all events for the zone have been seen, even if set to 1.
     */
    public float eventChance() {
        return 0.4f;
    }
    public List<String> events() {
        return Collections.emptyList();
    }

    public void renderOnMap(SpriteBatch sb, float alpha) {
        if (alpha > 0) {
            //do Stuff
            Texture backgroundTexture = backgroundTexture();
            if (backgroundTexture != null) {

            }
            FontHelper.renderFontCentered(sb, FontHelper.menuBannerFont, name,
                    labelX * SPACING_X + OFFSET_X, labelY * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY,
                    labelColor.cpy().mul(1, 1, 1, alpha), 0.8f
            );
        }
    }

    //used by default renderOnMap method
    public List<Texture> mapMarks() {
        return Collections.emptyList();
    }
    public Texture backgroundTexture() {
        return null;
    }




    //-----Map Gen-----

    //Whether the map generator can connect to the zone from points besides the start and end.
    protected boolean allowSideConnections() {
        return true;
    }
    //Whether the map generator can add additional paths within the zone
    protected boolean allowAdditionalPaths() {
        return true;
    }
    //Whether the map generator can add additional paths entering into the zone.
    //If false, will only be enterable through a single node.
    //Basically, more specific version of allowAdditionalPaths.
    //If allowAdditionalPaths is false, this will only affect attempts to enter through the side onto active nodes.
    protected boolean allowAdditionalEntrances() {
        return false;
    }

    //Note: isValidPath can be overridden to completely ignore these three methods.
    public boolean isValidPath(PlanningNode src, PlanningNode dest) {
        if (this == src.zone && this == dest.zone) { //Both nodes within zone
            if (allowAdditionalPaths()) return true;
            else return src.isActive() && dest.isActive();
        }

        PlanningNode internal = src.zone == this ? src : dest;
        PlanningNode external = src.zone == this ? dest : src;
        boolean isEntering = external.y < internal.y;

        if (!internal.isActive()) {
            //To connect to an inactive node:
            if (!allowAdditionalPaths()) return false; //Must allow additional paths
            if (isEntering && !allowAdditionalEntrances()) return false; //Must allow additional entrances if entering
            if (allowSideConnections()) return true; //Must allow side connections if on the side
            //Side connections not allowed, it has to be above top or below bottom
            if (internal.isBottom && external.y < internal.y) return true;
            if (internal.isTop && external.y > internal.y) return true;
            return false;
        }
        else {
            //Connecting to an active node:
            if (internal.isBottom && external.y < internal.y) return true; //Entering from bottom.
            if (internal.isTop && external.y > internal.y) return true; //Exiting from top. These two cannot be disabled.
            //Not entering bottom or exiting top, this is a side connection.
            if (isEntering && !allowAdditionalEntrances()) return false;
            return allowSideConnections();
        }
    }


    /**
     * Claims an area in the planner and generates an initial path through it.
     * By default, utilizes the generateNormalArea to claim a rectangular area and generate a single random path through it.
     * Areas may claim more unusual shapes or manually define their path/multiple paths if they wish.
     */
    public boolean generateMapArea(MapPlanner planner) {
        return generateNormalArea(planner, 3, 4);
    }

    public final boolean generateNormalArea(MapPlanner planner, int width, int height) {
        //Generates an area of the specified size with a single path going through it.
        //Additional paths/intersections may be generated later by map generation depending on other methods provided.
        Random rng = AbstractDungeon.mapRng;

        List<PlanningNode> validNodes = planner.validNodes(width, height);
        if (validNodes.isEmpty())
            return false;

        PlanningNode origin = validNodes.get(rng.random(validNodes.size() - 1));
        origin.claimArea(this, width, height);

        //Set properties
        this.x = origin.x;
        this.y = origin.y;
        this.width = width;
        this.height = height;

        this.labelY = origin.y + 0.5f;
        this.labelX = origin.x + (width / 2f);

        //Generate path
        generateRandomPath(rng, planner);

        return true;
    }

    protected final void generateRandomPath(Random rng, MapPlanner planner) {
        int startX = x + (width == 1 ? 0 : rng.random(width - 1));
        PlanningNode pathNode = planner.area[startX][y];
        if (height == 1) {
            pathNode.activate(false);
        }
        else {
            while (pathNode != null && pathNode.y < y + height - 1) {
                pathNode = pathNode.connectRandom(rng, x, x + width - 1);
            }
        }
    }

    public final void registerNode(MapRoomNode node) {
        nodes.add(node);
    }

    //Called when map generation (nodes and paths) is complete
    public void mapGenDone() {
        int count = 1;
        for (MapRoomNode node : nodes) {
            if (node.y == y) {
                labelX += node.x;
                ++count;
            }
        }
        labelX /= count;

        labelColor = getColor().cpy();
        labelColor.mul(0.75f);
    }

    //Room placement
    public void manualRoomPlacement(Random rng, ArrayList<AbstractRoom> roomList) {
    }

    //Removes first room that fits the filter from the list, or if none are found returns the default room.
    protected final AbstractRoom roomOrDefault(ArrayList<AbstractRoom> roomList, Predicate<AbstractRoom> filter, Supplier<AbstractRoom> defaultRoom) {
        Iterator<AbstractRoom> roomIterator = roomList.iterator();
        while (roomIterator.hasNext()) {
            AbstractRoom room = roomIterator.next();
            if (filter.test(room)) {
                roomIterator.remove();
                return room;
            }
        }
        return defaultRoom.get();
    }

    public abstract Color getColor();
}
