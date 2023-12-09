package spireMapOverhaul.abstracts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import spireMapOverhaul.BetterMapGenerator.MapPlanner;
import spireMapOverhaul.BetterMapGenerator.MapPlanner.PlanningNode;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.ZoneShapeMaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public abstract class AbstractZone {
    private static final float OFFSET_X = Settings.isMobile ? 496.0F * Settings.xScale : 560.0F * Settings.xScale;
    private static final float OFFSET_Y = 180.0F * Settings.scale;
    private static final float SPACING_X = Settings.isMobile ? (int)(Settings.xScale * 64.0F) * 2.2F : (int)(Settings.xScale * 64.0F) * 2.0F;
    private static final String[] NO_TEXT = new String[] { };

    public final String id;
    public String[] TEXT = NO_TEXT;
    public String name = "";
    protected Color labelColor = Color.WHITE;

    protected List<MapRoomNode> nodes = new ArrayList<>();
    protected int x = 0, y = 0, width = 1, height = 1;

    //These two should be set during initial generation in generateMapArea
    //They will then be adjusted to their final values in mapGenDone
    protected float labelX = 0, labelY = 0;

    public FrameBuffer zoneFb = null;
    private TextureRegion shapeRegion = null;

    public AbstractZone() {
        this(null);
    }

    //Id given should not be prefixed.
    public AbstractZone(String id) {
        if (id == null) {
            id = getClass().getSimpleName();
        }
        this.id = id;
        loadStrings();
    }

    public abstract AbstractZone copy();

    public abstract Color getColor();

    public boolean canSpawn() {
        System.out.println("ActNum: " + AbstractDungeon.actNum);
        for (int i = 1; i <= 3; ++i)
            System.out.println("IsAct" + i + ": " + isAct(i));
        return true;
    }

    //Utility methods for use in canSpawn
    protected final boolean isAct(int actNum) {
        if (Settings.isEndless) return AbstractDungeon.actNum % 3 == actNum;
        return AbstractDungeon.actNum == actNum;
    }

    protected void loadStrings() {
        if (SpireAnniversary6Mod.initializedStrings) {
            TEXT = CardCrawlGame.languagePack.getUIString(makeID(this.id)).TEXT;
            name = TEXT[0];
        }
        else {
            TEXT = NO_TEXT;
        }
    }

    /*public String modifyRolledEvent(String eventKey) {
        if (AbstractDungeon.eventRng.randomBoolean(eventChance())) {
            List<String> possibleEvents = events();
            return possibleEvents.remove(AbstractDungeon.eventRng.random(events().size()));
        }
        return eventKey;
    }*/



    public void renderOnMap(SpriteBatch sb, float alpha) {
        if (alpha > 0) {
            //do Stuff
            float anchorX = labelX * SPACING_X + OFFSET_X;
            float anchorY = labelY * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY;
            //float arbitraryYOffset = 210f; //need to actually figure this one out
            if (shapeRegion == null || shapeRegion.getTexture() == null) {
                shapeRegion = ZoneShapeMaker.makeShape(this, this.nodes, anchorX, anchorY, sb);
            }
            sb.setColor(getColor().cpy().mul(1, 0.9f, 0.85f, alpha*0.8f)); //just making the random color more palatable
            sb.draw(shapeRegion, anchorX - Gdx.graphics.getWidth()/2f, (anchorY - Gdx.graphics.getHeight()/4f));
            sb.setColor(Color.WHITE);


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

    public void dispose() {
        if (shapeRegion != null && shapeRegion.getTexture() != null) {
            shapeRegion.getTexture().dispose();
        }
        if (zoneFb != null) zoneFb.dispose();
    }


    //-----Map Gen-----

    //Whether the map generator can connect to the zone from points besides the start and end.
    protected boolean allowSideConnections() {
        return true;
    }
    //Whether the map generator can add any additional nodes within the zone.
    protected boolean allowAdditionalPaths() {
        return true;
    }
    /**
     * Whether the map generator can add additional paths entering into the zone.
     * If false, will only be enterable through a single node.
     * If allowAdditionalPaths is false, this will only affect attempts to enter through the side onto active nodes.
    */
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
        labelColor.mul(0.5f,0.5f,0.5f,0.8f);
    }


    /**
     * Occurs before any normal room placement related stuff. For room placement that breaks normal rules, eg. only one room type or single unique nodes.
     */
    public void manualRoomPlacement(Random rng) {
    }

    /**
     * Occurs before any rooms are distributed normally, for guaranteeing the appearance of specific types of rooms at a reasonable rate.
     */
    public void distributeRooms(Random rng, ArrayList<AbstractRoom> roomList) {
    }

    /**
     * Occurs after all rooms have been distributed to replace specific types of rooms.
     * @see spireMapOverhaul.abstracts.AbstractZone#replaceRoomsRandomly
     */
    public void replaceRooms(Random rng) {
    }

    /**
     * Removes first room that fits the filter from the list, or if none are found returns the default room.
     */
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

    /**
     * Replace rooms that match a filter with rooms from a supplier.
     * @param percentage The percentage of valid rooms to replace, from 0-1
     */
    protected final void replaceRoomsRandomly(Random rng, Supplier<AbstractRoom> roomSupplier, Predicate<AbstractRoom> roomFilter, float percentage) {
        replaceRoomsRandomly(rng, roomSupplier, roomFilter, percentage, percentage);
    }

    /**
     * Replace rooms that match a filter with rooms from a supplier.
     * @param minPercentage The minimum percentage of valid rooms to replace, from 0-1
     * @param maxPercentage The maximum percentage of valid rooms to replace, from 0-1
     */
    protected final void replaceRoomsRandomly(Random rng, Supplier<AbstractRoom> roomSupplier, Predicate<AbstractRoom> roomFilter, float minPercentage, float maxPercentage) {
        List<MapRoomNode> possibleNodes = new ArrayList<>();

        for (MapRoomNode node : nodes) {
            if (node.getRoom() != null && roomFilter.test(node.getRoom())) {
                possibleNodes.add(node);
            }
        }
        if (possibleNodes.isEmpty()) return;

        int min = (int) (minPercentage * possibleNodes.size()), max = (int) (maxPercentage * possibleNodes.size());
        if (max > possibleNodes.size()) max = possibleNodes.size();

        int amount;
        if (min >= max) amount = max;
        else amount = rng.random(min, max);

        for (int i = 0; i < amount; ++i) {
            _placeRoomRandomly(possibleNodes, rng, roomSupplier.get(), amount < nodes.size() / 2);
        }
    }

    /**
     * Places a room in a random empty node within the zone
     */
    protected final void placeRoomRandomly(Random rng, AbstractRoom room) {
        List<MapRoomNode> possibleNodes = new ArrayList<>();
        for (MapRoomNode node : nodes) {
            if (node.getRoom() == null) {
                possibleNodes.add(node);
            }
        }
        _placeRoomRandomly(possibleNodes, rng, room, true);
    }

    /**
     * Places a room in a random node in the given list
     * @param tryFollowRules If true, first tries to avoid placing the room with a duplicate parent or sibling.
     */
    private static void _placeRoomRandomly(List<MapRoomNode> nodes, Random rng, AbstractRoom room, boolean tryFollowRules) {
        if (tryFollowRules) {
            List<MapRoomNode> possibleNodes = new ArrayList<>();

            outer:
            for (MapRoomNode node : nodes) {
                for (MapRoomNode parent : node.getParents()) {
                    if (parent.getRoom() != null && room.getClass().equals(parent.getRoom().getClass())) {
                        continue outer;
                    }
                }
                for (MapRoomNode sibling : getSiblingsInZone(nodes, node.getParents(), node)) {
                    if (sibling.getRoom() != null && room.getClass().equals(sibling.getRoom().getClass())) {
                        continue outer;
                    }
                }
                possibleNodes.add(node);
            }

            if (!possibleNodes.isEmpty()) {
                possibleNodes.get(rng.random(possibleNodes.size() - 1)).setRoom(room);
                return;
            }
            //no nodes following the rules :(
        }

        if (nodes.isEmpty()) return;
        nodes.get(rng.random(nodes.size() - 1)).setRoom(room);
    }

    private static ArrayList<MapRoomNode> getSiblingsInZone(List<MapRoomNode> nodes, List<MapRoomNode> parents, MapRoomNode n) {
        ArrayList<MapRoomNode> siblings = new ArrayList<>();

        for (MapRoomNode node : nodes) {
            if (node.equals(n)) continue;

            for (MapRoomNode parent : parents) {
                if (node.getParents().contains(parent)) {
                    siblings.add(node);
                }
            }
        }

        return siblings;
    }
}
