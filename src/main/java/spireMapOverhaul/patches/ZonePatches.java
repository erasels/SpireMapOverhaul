package spireMapOverhaul.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.map.MapRoomNode;
import spireMapOverhaul.abstracts.AbstractZone;

//For basically all zone hooks.
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
}
