package spireMapOverhaul.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.map.RoomTypeAssigner;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;

import java.util.ArrayList;
import java.util.List;

public class ZonePatches {
    @SpirePatch(
            clz = MapRoomNode.class,
            method = SpirePatch.CLASS
    )
    public static class Fields {
        public static SpireField<AbstractZone> zone = new SpireField<>(()->null);
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "render"
    )
    public static class TempDisplay {
        @SpirePrefixPatch
        public static void setColor(MapRoomNode __instance, SpriteBatch sb) {
            AbstractZone zone = Fields.zone.get(__instance);
            if (zone != null) {
                __instance.color = zone.getColor().cpy();
            }
        }
    }

    @SpirePatch(
            clz = RoomTypeAssigner.class,
            method = "distributeRoomsAcrossMap"
    )
    public static class ManualRoomDistribution {
        @SpirePrefixPatch
        public static void zonesGoFirst(Random rng, ArrayList<ArrayList<MapRoomNode>> map, ArrayList<AbstractRoom> roomList) {
            List<AbstractZone> zones = BetterMapGenerator.getActiveZones(map);
            for (AbstractZone zone : zones) {
                zone.manualRoomPlacement(rng, roomList);
            }
        }
    }
}
