package spireMapOverhaul.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.map.RoomTypeAssigner;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;

import java.util.ArrayList;

public class RoomDistributionPatches {
    /// Room Distribution Patches
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "generateMap"
    )
    public static class RoomDistribution {
        @SpireInstrumentPatch
        public static ExprEditor OnlyCountEmptyRooms() {
            return new ExprEditor() {
                boolean done = false;
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (!done && m.getMethodName().equals("hasEdges") && m.getClassName().equals(MapRoomNode.class.getName())) {
                        m.replace(
                                "$_ = $proceed($$) && ($0.getRoom() == null)" +
                                        "&& ($0.y != 0) && ($0.y != 8) && ($0.y != (" + AbstractDungeon.class.getName() + ".map.size() - 1));"
                        );
                        done = true;
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz = RoomTypeAssigner.class,
            method = "assignRoomsToNodes"
    )
    public static class DistributeRoomsInZones {
        @SpirePrefixPatch
        public static void PreDistribute(ArrayList<ArrayList<MapRoomNode>> map, ArrayList<AbstractRoom> roomList) {
            for (AbstractZone zone : BetterMapGenerator.getActiveZones(map)) {
                zone.distributeRooms(AbstractDungeon.mapRng, roomList);
            }
        }

        @SpirePostfixPatch
        public static void PostDistribute(ArrayList<ArrayList<MapRoomNode>> map, ArrayList<AbstractRoom> roomList) {
            for (AbstractZone zone : BetterMapGenerator.getActiveZones(map)) {
                zone.replaceRooms(AbstractDungeon.mapRng);
            }
        }
    }
}
