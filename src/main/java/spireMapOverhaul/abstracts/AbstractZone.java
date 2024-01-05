package spireMapOverhaul.abstracts;

import basemod.Pair;
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
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import spireMapOverhaul.BetterMapGenerator.MapPlanner;
import spireMapOverhaul.BetterMapGenerator.MapPlanner.PlanningNode;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.ActUtil;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.ZoneShapeMaker;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public abstract class AbstractZone {
    private static final float OFFSET_X = Settings.isMobile ? 496.0F * Settings.xScale : 560.0F * Settings.xScale;
    private static final float OFFSET_Y = 180.0F * Settings.scale;
    private static final float SPACING_X = Settings.isMobile ? (int)(Settings.xScale * 64.0F) * 2.2F : (int)(Settings.xScale * 64.0F) * 2.0F;
    private static final String[] NO_TEXT = new String[] { };
    private static final int NO_ELITES_BOUNDARY_ROW = 4;
    private static final int TREASURE_ROW = 8;
    private static final int FINAL_CAMPFIRE_ROW = 15;

    private static final HashMap<Icons, String> iconsMap;
    static {
        iconsMap = new HashMap<>();
        iconsMap.put(Icons.MONSTER, "[" +SpireAnniversary6Mod.modID+":MonsterIcon]");
        iconsMap.put(Icons.CHEST, "[" +SpireAnniversary6Mod.modID+":ChestIcon]");
        iconsMap.put(Icons.EVENT, "[" +SpireAnniversary6Mod.modID+":EventIcon]");
        iconsMap.put(Icons.SHOP, "[" +SpireAnniversary6Mod.modID+":ShopIcon]");
        iconsMap.put(Icons.REST, "[" +SpireAnniversary6Mod.modID+":RestIcon]");
    }

    public enum Icons {
        MONSTER, CHEST, EVENT, REST, SHOP
    }

    public final String id;
    public String[] TEXT = NO_TEXT;
    public String name = "";
    public String tooltipBody = "";
    protected Color labelColor = Color.WHITE;

    protected List<MapRoomNode> nodes = new ArrayList<>();
    protected int x = 0, y = 0, width = 1, height = 1;

    //These two should be set during initial generation in generateMapArea
    //They will then be adjusted to their final values in mapGenDone
    protected float labelX = 0, labelY = 0;

    public FrameBuffer zoneFb = null;
    private TextureRegion shapeRegion = null;
    public Texture iconTexture;

    public List<Hitbox> hitboxes;
    public HashMap<Hitbox, Pair<Float, Float>> hitboxRelativePositions;

    protected Icons[] icons;

    public AbstractZone() {
        this(null);
    }

    //Id given should not be prefixed.
    public AbstractZone(String id, Icons... icons) {
        if (id == null) {
            id = getClass().getSimpleName();
        }
        this.id = id;
        this.icons = icons;
        loadStrings();
        if (Gdx.files.internal(SpireAnniversary6Mod.makeImagePath("ui/zoneIcons/" + id + ".png")).exists()) {
            iconTexture = TexLoader.getTexture(SpireAnniversary6Mod.makeImagePath("ui/zoneIcons/" + id + ".png"));
        } else {
            iconTexture = TexLoader.getTexture(SpireAnniversary6Mod.makeImagePath("ui/zoneIcons/zoneIconTemplate.png"));
        }
    }

    public abstract AbstractZone copy();

    public abstract Color getColor();

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public boolean canSpawn() {
        System.out.println("ActNum: " + AbstractDungeon.actNum);
        for (int i = 1; i <= 3; ++i)
            System.out.println("IsAct" + i + ": " + isAct(i));
        return true;
    }

    //Utility methods for use in canSpawn
    protected final boolean isAct(int actNum) {
        return ActUtil.getRealActNum() == actNum;
    }

    public void loadStrings() {
        if (SpireAnniversary6Mod.initializedStrings) {
            TEXT = CardCrawlGame.languagePack.getUIString(makeID(this.id)).TEXT;
            name = TEXT[0];
            updateDescription();
        }
        else {
            TEXT = NO_TEXT;
        }
    }

    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        for(Icons icon : icons) {
            sb.append(iconsMap.get(icon)).append(" ");
        }
        if (icons.length > 0)
            sb.append(" NL ");
        sb.append(TEXT[1]);
        tooltipBody = sb.toString();
    }

    public void renderOnMap(SpriteBatch sb, float alpha) {
        if (alpha > 0) {
            if (shapeRegion == null) {
                shapeRegion = ZoneShapeMaker.makeShape(this, AbstractDungeon.map, this.nodes, sb);
            }
            float anchorX = x * SPACING_X + OFFSET_X - ZoneShapeMaker.FB_OFFSET;
            float anchorY = y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY - ZoneShapeMaker.FB_OFFSET;
            sb.setColor(getColor().cpy().mul(1f, 1f, 1f, alpha*0.5f));
            sb.draw(shapeRegion, anchorX, anchorY);
            boolean showTooltip = false;
            for (Hitbox hb : hitboxes) {
                hb.move(hitboxRelativePositions.get(hb).getKey() + anchorX, hitboxRelativePositions.get(hb).getValue() + anchorY);
                hb.update();
                hb.render(sb);
                if (hb.hovered) {
                    showTooltip = true;
                }
            }
            if (showTooltip) {
                TipHelper.renderGenericTip(InputHelper.mX + 40f*Settings.scale, InputHelper.mY - 65f*Settings.scale, name, tooltipBody);
            }

            FontHelper.renderFontCentered(sb, FontHelper.menuBannerFont, name,
                    labelX * SPACING_X + OFFSET_X, labelY * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY,
                    labelColor.cpy().mul(1, 1, 1, alpha), 0.8f
            );

        }
    }

    public void dispose() {

        if (shapeRegion != null && shapeRegion.getTexture() != null) {
            shapeRegion.getTexture().dispose();
        }
        if (zoneFb != null) zoneFb.dispose();


        shapeRegion = null;
        zoneFb = null;
    }


    //-----Map Gen-----

    //Whether the zone can include early rows (rows 0-4).
    //This should be overridden to return false for zones that always have an elite room.
    //Zones that substantially increase encounter difficulty may also want to override this to false.
    protected boolean canIncludeEarlyRows() {
        return true;
    }
    //Whether the zone can include the row that always has treasure nodes (row 8).
    //This should be overridden to return false for zones that might replace the treasure node with something else.
    //(We consider it important for play experience, game balance, and compatibility that the treasure nodes stay).
    protected boolean canIncludeTreasureRow() {
        return true;
    }
    //Whether the zone can include the row that always has the campfire node before the boss (row 15).
    //This should be overridden to return false for zones that might replace the campfire node with something else.
    //(We consider it important for play experience, game balance, and compatibility that the campfire nodes stay).
    protected boolean canIncludeFinalCampfireRow() {
        return true;
    }

    protected List<Integer> forbiddenRows() {
        ArrayList<Integer> list = new ArrayList<>();
        if (!canIncludeEarlyRows()) {
            for (int i = 0; i <= NO_ELITES_BOUNDARY_ROW; i++) {
                list.add(i);
            }
        }
        if (!canIncludeTreasureRow()) {
            list.add(TREASURE_ROW);
        }
        if (!canIncludeFinalCampfireRow()) {
            list.add(FINAL_CAMPFIRE_ROW);
        }
        return list;
    }

    //By default, the rows for treasure nodes and the final campfire before the boss are protected, meaning that random
    //(re)placement with the built-in AbstractZone methods won't affect them. Zones with manual placement logic should
    //either replicate these checks or override canIncludeTreasureRow/canIncludeFinalCampfireRow to return false.
    protected final boolean isProtectedRow(int row) {
        return row == TREASURE_ROW || row == FINAL_CAMPFIRE_ROW;
    }

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
        return generateNormalArea(planner, width, height);
    }

    public final boolean generateNormalArea(MapPlanner planner, int width, int height) {
        //Generates an area of the specified size with a single path going through it.
        //Additional paths/intersections may be generated later by map generation depending on other methods provided.
        Random rng = AbstractDungeon.mapRng;

        List<PlanningNode> validNodes = planner.validNodes(width, height, this.forbiddenRows());
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
    public void mapGenDone(ArrayList<ArrayList<MapRoomNode>> map) {
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
     * Replace rooms that match a filter (and aren't in protected rows) with rooms from a supplier.
     * @param percentage The percentage of valid rooms to replace, from 0-1
     */
    protected final void replaceRoomsRandomly(Random rng, Supplier<AbstractRoom> roomSupplier, Predicate<AbstractRoom> roomFilter, float percentage) {
        replaceRoomsRandomly(rng, roomSupplier, roomFilter, percentage, percentage);
    }

    /**
     * Replace rooms that match a filter (and aren't in protected rows) with rooms from a supplier.
     * @param minPercentage The minimum percentage of valid rooms to replace, from 0-1
     * @param maxPercentage The maximum percentage of valid rooms to replace, from 0-1
     */
    protected final void replaceRoomsRandomly(Random rng, Supplier<AbstractRoom> roomSupplier, Predicate<AbstractRoom> roomFilter, float minPercentage, float maxPercentage) {
        List<MapRoomNode> possibleNodes = new ArrayList<>();

        for (MapRoomNode node : nodes) {
            if (node.getRoom() != null && roomFilter.test(node.getRoom()) && !isProtectedRow(node.y)) {
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
     * Places a room in a random empty node within the zone that is not in a protected row
     */
    protected final void placeRoomRandomly(Random rng, AbstractRoom room) {
        List<MapRoomNode> possibleNodes = new ArrayList<>();
        for (MapRoomNode node : nodes) {
            if (node.getRoom() == null && !isProtectedRow(node.y)) {
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
