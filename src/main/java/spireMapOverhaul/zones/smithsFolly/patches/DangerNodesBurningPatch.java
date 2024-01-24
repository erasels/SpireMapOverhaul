package spireMapOverhaul.zones.smithsFolly.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.vfx.FlameAnimationEffect;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.zones.smithsFolly.SmithsFolly;
import spireMapOverhaul.zones.thieveshideout.patches.HighlightForcedEventRoomPatch;

public class DangerNodesBurningPatch {

    @SpirePatch2(clz = MapRoomNode.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class RenderPatch {
        @SpirePrefixPatch
        public static void render(MapRoomNode __instance, SpriteBatch sb) {
            AbstractZone zone = ZonePatches.Fields.zone.get(__instance);
            if ((zone instanceof SmithsFolly) && (__instance.room instanceof MonsterRoom || __instance.room instanceof MonsterRoomElite)) {
                for (FlameAnimationEffect e : HighlightForcedEventRoomPatch.EffectsField.effects.get(__instance)) {
                    e.render(sb, ReflectionHacks.getPrivate(__instance, MapRoomNode.class, "scale"));
                }
            }
        }
    }

    @SpirePatch2(clz = MapRoomNode.class, method = "update", paramtypez = {})
    public static class UpdatePatch {
        @SpirePrefixPatch
        public static void update(MapRoomNode __instance) {
            AbstractZone zone = ZonePatches.Fields.zone.get(__instance);
            if ((zone instanceof SmithsFolly) && (__instance.room instanceof MonsterRoom || __instance.room instanceof MonsterRoomElite)) {
                HighlightForcedEventRoomPatch.TimerField.timer.set(__instance, HighlightForcedEventRoomPatch.TimerField.timer.get(__instance) - Gdx.graphics.getDeltaTime());
                if (HighlightForcedEventRoomPatch.TimerField.timer.get(__instance) < 0.0F) {
                    HighlightForcedEventRoomPatch.TimerField.timer.set(__instance, MathUtils.random(0.2F, 0.4F));
                    HighlightForcedEventRoomPatch.EffectsField.effects.get(__instance).add(new FlameAnimationEffect(__instance.hb));
                }
                for (FlameAnimationEffect e : HighlightForcedEventRoomPatch.EffectsField.effects.get(__instance)) {
                    if (e.isDone) {
                        e.dispose();
                    }
                }
                HighlightForcedEventRoomPatch.EffectsField.effects.get(__instance).removeIf(e -> e.isDone);
                for (FlameAnimationEffect e : HighlightForcedEventRoomPatch.EffectsField.effects.get(__instance)) {
                    e.update();
                }
            }
        }
    }

}
