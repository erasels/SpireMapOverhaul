package spireMapOverhaul;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.util.ActUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BetterMapGenerator {
    public static final Logger mapGenLogger = LogManager.getLogger("BetterMapGen");

    public static BetterMapGenerator generator = new BetterMapGenerator();

    private static float ZONE_FALLOFF_RATE = 2f / 3f;

    private static ArrayList<ArrayList<MapRoomNode>> lastMap = null;
    private static List<AbstractZone> activeZones = new ArrayList<>();

    public static List<AbstractZone> queueCommandZones = new ArrayList<>();

    public static List<AbstractZone> getActiveZones(ArrayList<ArrayList<MapRoomNode>> map) {
        if (map != lastMap) {
            lastMap = map;
            activeZones.clear();
        }
        return activeZones;
    }

    public static void clearActiveZones() {
        for (AbstractZone z : activeZones) {
            z.dispose();
        }
        activeZones.clear();
    }

    private BetterMapGenerator() {
    }

    public ArrayList<ArrayList<MapRoomNode>> generate(Random rng, int width, int height, int pathDensity) {
        if (Settings.isEndless && ActUtil.getRealActNum() == 1) {
            //In endless mode, we let you encounter zones again each endless cycle
            SpireAnniversary6Mod.currentRunSeenZones.clear();
        }
        else {
            SpireAnniversary6Mod.currentRunSeenZones.addAll(activeZones.stream().map(z -> z.id).collect(Collectors.toList()));
        }
        mapGenLogger.info("Already seen zones: " + String.join(",", SpireAnniversary6Mod.currentRunSeenZones));
        MapPlanner planner;
        do {
            List<AbstractZone> possibleZones = new ArrayList<>();
            for (AbstractZone zone : SpireAnniversary6Mod.unfilteredAllZones) {
                if (SpireAnniversary6Mod.currentRunAllZones.contains(zone.id) && !(SpireAnniversary6Mod.currentRunNoRepeatZones && SpireAnniversary6Mod.currentRunSeenZones.contains(zone.id)) && zone.canSpawn()) {
                    possibleZones.add(zone);
                }
            }

            planner = new MapPlanner(width, height);

            AbstractZone zone;
            clearActiveZones();
            float zoneRate = 1;

            for (AbstractZone queuedZone : queueCommandZones) {
                if (queuedZone.generateMapArea(planner)) {
                    activeZones.add(queuedZone);
                    possibleZones.removeIf(z -> z.id.equals(queuedZone.id));
                } else {
                    mapGenLogger.info("Failed to place " + queuedZone.id + " zone queued by command.");
                }
            }

            outer:
            while (rng.randomBoolean(zoneRate) && activeZones.size() < pathDensity) {
                int zoneCountIndex = SpireAnniversary6Mod.getZoneCountIndex();
                int minZones = zoneCountIndex;
                int maxZones = zoneCountIndex + 1;
                int targetZoneCount = rng.random(minZones, maxZones);

                while (!possibleZones.isEmpty() && activeZones.size() < targetZoneCount) {
                    zone = possibleZones.remove(rng.random(possibleZones.size() - 1)).copy();
                    if (zone.generateMapArea(planner)) {
                        activeZones.add(zone);
                        continue outer;
                    } else {
                        mapGenLogger.info("Failed to place zone.");
                    }
                }
                break;
            }
            mapGenLogger.info("Generating map with " + activeZones.size() + " zones...");
        } while (!finishMap(rng, planner, pathDensity));
        queueCommandZones.clear();

        lastMap = planner.generate();

        List<AbstractZone> zones = getActiveZones(lastMap);
        for (AbstractZone zone : zones) {
            zone.manualRoomPlacement(rng);
        }

        return lastMap;
    }

    private boolean finishMap(Random rng, MapPlanner planner, int targetPathDensity) {
        //Currently, there are Some Zones with their own individual paths.
        //First, have to ensure no orphans.

        //A normal dungeon has a mapDensity of 6, meaning it generates "6" paths.
        //In practice, there are less due to the nuances of how they generate.

        planner.sort();
        int failed = 0;
        while (failed < planner.orphans.size() && Math.max(planner.orphans.size(), planner.childless.size()) > targetPathDensity) {
            //Try to form direct paths between childless and orphans to reduce predicted density.
            //First, determine which ones *can* be connected.
            //Start from the topmost orphans, which will test childless nodes top to bottom to see if they are reachable.
            //They will connect to a random one from all valid options, weighted towards closer ones?
            //In practice, this code is unlikely to ever run as it would require biomes to generate more than 6 start/endpoints
            //With default generation, that would require at least 7 biomes to generate.
            if (!planner.adopt(rng, planner.orphans.get(failed), false)) {
                //Still an orphan, move on to the Next one.
                ++failed;
            }
        }

        int pathDensity = Math.max(planner.orphans.size(), planner.childless.size());

        //Now, just form arbitrary paths for all remaining orphans and childless nodes.
        //Each of these will count as 1 path. Follow normal map generation logic (avoid crossovers, and quick re-merges if possible)
        planner.sort();
        while (!planner.orphans.isEmpty()) {
            MapPlanner.PlanningNode orphan = planner.orphans.get(0);
            if (!planner.connectUpwards(rng, orphan, true)) {
                //Completely failed to give this orphan a path.
                mapGenLogger.error("Failed to generate map, retrying.");
                return false;
            }
        }

        while (!planner.childless.isEmpty()) {
            MapPlanner.PlanningNode childless = planner.childless.get(0);
            if (!planner.finishPath(rng, childless, true)) {
                //Completely failed to give this parent a path.
                mapGenLogger.error("Failed to generate map, retrying.");
                return false;
            }
        }

        //If not at the desired density, generate additional paths using normal map generation logic.
        //This will be the part that will probably be fiddled with the most.
        while (pathDensity < targetPathDensity || planner.originPoints <= 1) {
            //Do Stuff
            if (!planner.generateRandomPath(rng))
                return false;
            ++pathDensity;
        }

        return true;
    }


    public static class MapPlanner {
        public final PlanningNode[][] area;
        public final int width, height;
        private final List<PlanningNode> availableNodes = new ArrayList<>();

        private final List<PlanningNode> orphans = new ArrayList<>(); //Active nodes with no path to them
        private final List<PlanningNode> childless = new ArrayList<>(); //Active nodes with no path away from them

        public int originPoints = 0;

        public MapPlanner(int width, int height) {
            this.width = width;
            this.height = height;
            area = new PlanningNode[width][height];
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    PlanningNode node = new PlanningNode(this, x, y);
                    availableNodes.add(node);
                    area[x][y] = node;
                }
            }
        }

        public List<PlanningNode> validNodes(int requiredWidth, int requiredHeight, List<Integer> forbiddenRows) {
            List<PlanningNode> valid = new ArrayList<>();
            for (PlanningNode node : availableNodes) {
                if (requiredWidth - 1 >= node.availability.length || requiredWidth < 1) continue;
                if(forbiddenRows.stream().anyMatch(row -> node.y <= row && node.y + requiredHeight - 1 >= row)) continue;
                if (node.availability[requiredWidth - 1] >= requiredHeight) {
                    valid.add(node);
                }
            }
            return valid;
        }

        //Attempt to connect orphan to a parent, preferably a childless one. Returns false if it fails.
        public boolean adopt(Random rng, PlanningNode orphan, boolean tryIgnoreReconnect) {
            List<PlanningNode> possibleAdopters = new ArrayList<>();
            for (PlanningNode node : childless) {
                if (node.y >= orphan.y) continue;

                if (Math.abs(orphan.x - node.x) < orphan.y - node.y) {
                    possibleAdopters.add(node);
                }
            }

            if (possibleAdopters.isEmpty()) {
                //No childless nodes that can be a parent.
                //Then, how about nodes with children?

                int y = orphan.y - 1;
                int minX = orphan.x - 1;
                int maxX = orphan.x + 1;

                for (int x = minX; x <= maxX; ++x) {
                    PlanningNode node = area[x][y];
                    if (node.isActive) {
                        if (!containsChild(node, possibleAdopters)) {
                            possibleAdopters.add(node);
                        }
                    }
                }
            }

            if (possibleAdopters.isEmpty())
                return false;

            return connectUpwards(rng, possibleAdopters, orphan, tryIgnoreReconnect);
        }

        public boolean connectUpwards(Random rng, PlanningNode destNode, boolean tryIgnoreReconnect) {
            List<PlanningNode> possible = new ArrayList<>();
            int minX = Math.max(0, destNode.x - destNode.y), maxX = Math.min(width - 1, destNode.x + destNode.y);
            for (int x = minX; x <= maxX; ++x) {
                possible.add(area[x][0]);
            }

            if (possible.isEmpty())
                return false;

            return connectUpwards(rng, possible, destNode, tryIgnoreReconnect);
        }
        public boolean connectUpwards(Random rng, List<PlanningNode> srcNodes, PlanningNode destNode, boolean tryIgnoreReconnect) {
            //Find all valid paths
            List<PathFinder> pathFinders = new ArrayList<>();
            for (PlanningNode src : srcNodes) {
                PathFinder pathFinder = PathFinder.findPaths(this, src, false, (node)->node.canConnect(destNode));
                pathFinder.removeIf(
                        (finder)->!finder.hasPath() && finder.src != destNode
                );
                if (pathFinder.hasPath())
                    pathFinders.add(pathFinder);
            }

            if (!pathFinders.isEmpty()) {
                PathFinder pathFinder = pathFinders.get(rng.random(pathFinders.size() - 1));
                pathFinder.formPath(rng, false);
                return true;
            }

            if (!tryIgnoreReconnect)
                return false; //No valid paths.

            for (PlanningNode src : srcNodes) {
                PathFinder pathFinder = PathFinder.findPaths(this, src, true, (node)->node.canConnect(destNode));
                pathFinder.removeIf(
                        (finder)->!finder.hasPath() && finder.src != destNode
                );
                if (pathFinder.hasPath())
                    pathFinders.add(pathFinder);
            }

            if (pathFinders.isEmpty())
                return false;

            PathFinder pathFinder = pathFinders.get(rng.random(pathFinders.size() - 1));
            pathFinder.formPath(rng, false);
            return true;
        }

        public boolean finishPath(Random rng, PlanningNode childless, boolean tryIgnoreReconnect) {
            PathFinder pathFinder = PathFinder.findPaths(this, childless, false, (node)->true);
            pathFinder.removeIf(
                    //Path does not reach the top.
                    (finder)->!finder.hasPath() && finder.src.y < height - 1
            );

            if (pathFinder.hasPath()) {
                pathFinder.formPath(rng, true);
                return true;
            }

            if (!tryIgnoreReconnect) return false;

            pathFinder = PathFinder.findPaths(this, childless, true, (node)->true);
            pathFinder.removeIf(
                    //Path does not reach the top.
                    (finder)->!finder.hasPath() && finder.src.y < height - 1
            );

            if (pathFinder.hasPath()) {
                pathFinder.formPath(rng, true);
                return true;
            }
            return false;
        }

        public boolean generateRandomPath(Random rng) {
            for (int attempts = 3; attempts > 0; --attempts) {
                PlanningNode src = area[rng.random(width - 1)][0];
                while (originPoints <= 1 && src.isActive) {
                    src = area[rng.random(width - 1)][0];
                }

                if (finishPath(rng, src, true)) {
                    return true;
                }
            }
            return false;
        }

        public void sort() {
            orphans.sort(Comparator.comparingInt(a -> a.y));
            childless.sort(Comparator.comparingInt(a -> a.y));
        }

        private static boolean containsChild(PlanningNode node, List<PlanningNode> childNodes) {
            if (childNodes.contains(node))
                return true;

            for (PlanningNode child : node.connections) {
                if (containsChild(child, childNodes)) {
                    return true;
                }
            }
            return false;
        }

        public ArrayList<ArrayList<MapRoomNode>> generate() {
            ArrayList<ArrayList<MapRoomNode>> map = createNodes(width, height);

            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height - 1; ++y) {
                    PlanningNode src = area[x][y];
                    MapRoomNode realSrc = map.get(y).get(x);

                    for (PlanningNode dest : src.connections) {
                        MapRoomNode realDest = map.get(dest.y).get(dest.x);
                        MapEdge newEdge = new MapEdge(src.x, src.y, realSrc.offsetX, realSrc.offsetY,
                                dest.x, dest.y, realDest.offsetX, realDest.offsetY, false);
                        realSrc.addEdge(newEdge);
                        realSrc.getEdges().sort(Comparator.naturalOrder());
                        realDest.addParent(realSrc);
                    }
                }
            }

            for (int x = 0; x < width; ++x) {
                PlanningNode src = area[x][height - 1];
                if (src.isActive) {
                    MapRoomNode realSrc = map.get(height - 1).get(x);

                    MapEdge newEdge = new MapEdge(src.x, src.y, realSrc.offsetX, realSrc.offsetY,
                            3, src.y + 2, 0.0F, 0.0F, true);
                    realSrc.addEdge(newEdge);
                    realSrc.getEdges().sort(Comparator.naturalOrder());
                }
            }

            for (AbstractZone zone : activeZones) {
                zone.mapGenDone(map);
            }

            return map;
        }

        private ArrayList<ArrayList<MapRoomNode>> createNodes(int width, int height) {
            ArrayList<ArrayList<MapRoomNode>> nodes = new ArrayList<>();
            AbstractZone zone;

            for(int y = 0; y < height; ++y) {
                ArrayList<MapRoomNode> row = new ArrayList<>();
                for(int x = 0; x < width; ++x) {
                    MapRoomNode node = new MapRoomNode(x, y);
                    zone = area[x][y].zone;
                    ZonePatches.Fields.zone.set(node, zone);
                    row.add(node);
                    if (zone != null && area[x][y].isActive)
                        zone.registerNode(node);
                }

                nodes.add(row);
            }

            return nodes;
        }

        public static class PlanningNode {
            private final MapPlanner planner;
            public final int x, y;

            public int[] availability;
            public AbstractZone zone = null;
            public boolean isTop = false, isBottom = false;

            private boolean isActive = false;
            public List<PlanningNode> connections = new ArrayList<>();

            public PlanningNode(MapPlanner planner, int x, int y) {
                this.planner = planner;
                this.x = x;
                this.y = y;

                availability = new int[planner.width - 1 - x];
                Arrays.fill(availability, planner.height - y);
                //Availability of a n width, m tall area:
                //if n >= availability.length, false
                //availability[n - 1] >= m
            }

            public void claimArea(AbstractZone claimer, int width, int height) {
                int side = this.x + width;
                int top = this.y + height;
                for (int mapX = 0; mapX < side; ++mapX) { //Area on left of claimed area
                    for (int mapY = 0; mapY < top; ++mapY) {
                        PlanningNode node = planner.area[mapX][mapY];

                        for (int i = Math.max(0, this.x - mapX); i < node.availability.length; ++i) {
                            node.availability[i] = Math.min(node.availability[i],
                                    Math.max(0, this.y - node.y));
                        }
                    }
                }
                for (int mapX = this.x; mapX < side; ++mapX) {
                    for (int mapY = this.y; mapY < top; ++mapY) {
                        PlanningNode node = planner.area[mapX][mapY];
                        node.claim(claimer, mapY == this.y, mapY == top - 1);
                    }
                }
            }
            public void claim(AbstractZone claimer, boolean isBottom, boolean isTop) {
                if (this.zone != null && this.zone != claimer) {
                    mapGenLogger.warn("Planning node " + x + "," + y + " claimed by a zone while already claimed");
                }
                planner.availableNodes.remove(this);
                this.zone = claimer;
                this.isBottom = isBottom;
                this.isTop = isTop;
            }

            public PlanningNode connectRandom(Random rng, int min, int max) {
                if (y == planner.height - 1) return null;

                if (min < 0) min = 0;
                if (max > planner.width - 1) max = planner.width - 1;
                int nextX = rng.random(x - 1, x + 1);
                nextX = MathUtils.clamp(nextX, min, max);

                return connect(nextX);
            }

            //All connections should end up going through this method.
            public PlanningNode connect(int destX) {
                activate(false);
                if (y == planner.height - 1) return null;

                PlanningNode next = planner.area[destX][y + 1];
                next.activate(true);
                connections.add(next);
                planner.childless.remove(this);

                return next;
            }
            public void activate(boolean connected) {
                if (!isActive) {
                    isActive = true;
                    if (!connected && y > 0) {
                        planner.orphans.add(this);
                    }
                    else if (y == 0) {
                        ++planner.originPoints;
                    }
                    planner.childless.add(this);
                }
                if (connected) {
                    planner.orphans.remove(this);
                }
            }
            public void endPath() {
                planner.childless.remove(this);
            }

            //Whether this node can theoretically connect to the specified node.
            public boolean canConnect(PlanningNode other) {
                if (other == this) return true;
                if (other.y <= this.y) return false;

                return Math.abs(other.x - x) <= other.y - y;
            }
            //Whether this node *is* connected to the specified node.
            public boolean isConnected(PlanningNode other) {
                if (other == this) return true;
                if (other.y >= this.y) return false;
                for (PlanningNode child : connections) {
                    if (child.isConnected(other))
                        return true;
                }
                return false;
            }

            //Whether the specified node can *legally* connect to this node.
            //It is assumed that the x and y gap is at most 1, and that "from" is below this node.
            public boolean isValidPath(PlanningNode from, boolean ignoreReconnect) {
                if (zone != null && !zone.isValidPath(from, this))
                    return false;
                if (from.zone != null && from.zone != zone && !from.zone.isValidPath(from, this))
                    return false;

                if (!ignoreReconnect && from.y > 0) {
                    //Test for shared parent at short distance
                    for (int x = Math.max(0, from.x - 1); x < Math.min(planner.width, from.x + 1); ++x) {
                        PlanningNode test = planner.area[x][from.y - 1];
                        if (test.isConnected(from) && test.isConnected(this))
                            return false;
                    }
                }
                if (from.x == this.x) return true; //No crossover
                PlanningNode crossoverA = planner.area[this.x][from.y];
                PlanningNode crossoverB = planner.area[from.x][this.y];
                return !crossoverA.connections.contains(crossoverB);
            }

            public boolean isActive() {
                return isActive;
            }
            public boolean isTopOrBottom() {
                return isTop || isBottom;
            }
        }

        public static class PathFinder {
            public final PlanningNode src;
            public final List<PathFinder> paths = new ArrayList<>();

            private PathFinder(PlanningNode src) {
                this.src = src;
            }

            public static PathFinder findPaths(MapPlanner planner, PlanningNode src, boolean ignoreReconnect, Predicate<PlanningNode> isValidPath) {
                PathFinder head = new PathFinder(src);

                head._findPaths(planner, ignoreReconnect, isValidPath);
                return head;
            }

            private void _findPaths(MapPlanner planner, boolean ignoreReconnect, Predicate<PlanningNode> isValidPath) {
                int y = src.y + 1;
                if (y >= planner.height) return;

                int maxX = Math.min(planner.width - 1, src.x + 1);

                for (int x = Math.max(0, src.x - 1); x <= maxX; ++x) {
                    PlanningNode next = planner.area[x][y];
                    if (isValidPath.test(next) && next.isValidPath(src, ignoreReconnect)) {
                        paths.add(findPaths(planner, next, ignoreReconnect, isValidPath));
                    }
                }
            }

            public void formPath(Random rng, boolean topOff) {
                if (paths.isEmpty()) {
                    if (topOff)
                        src.endPath();
                }
                else if (paths.size() == 1) {
                    _formPath(rng, topOff, paths.get(0));
                }
                else {
                    _formPath(rng, topOff, paths.get(rng.random(paths.size() - 1)));
                }
            }
            private void _formPath(Random rng, boolean topOff, PathFinder next) {
                src.connect(next.src.x);
                next.formPath(rng, topOff);
            }

            public boolean hasPath() {
                return !paths.isEmpty();
            }

            public void removeIf(Predicate<PathFinder> filter) {
                paths.removeIf(finder->finder._removeIf(filter));
            }
            private boolean _removeIf(Predicate<PathFinder> filter) {
                paths.removeIf(finder->finder._removeIf(filter));
                return filter.test(this);
            }
        }
    }

}