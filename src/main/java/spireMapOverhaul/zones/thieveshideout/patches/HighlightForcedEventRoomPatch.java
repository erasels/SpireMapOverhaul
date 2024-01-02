package spireMapOverhaul.zones.thieveshideout.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.vfx.FlameAnimationEffect;
import spireMapOverhaul.zones.thieveshideout.rooms.ForcedEventRoom;

import java.util.ArrayList;

public class HighlightForcedEventRoomPatch {
    @SpirePatch2(clz = MapRoomNode.class, method = SpirePatch.CLASS)
    public static class EffectsField {
        public static final SpireField<ArrayList<FlameAnimationEffect>> effects = new SpireField<>(ArrayList::new);
    }
    @SpirePatch2(clz = MapRoomNode.class, method = SpirePatch.CLASS)
    public static class TimerField {
        public static final SpireField<Float> timer = new SpireField<>(() -> 0.0f);
    }

    @SpirePatch2(clz = MapRoomNode.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class RenderPatch {
        @SpirePrefixPatch
        public static void render(MapRoomNode __instance, SpriteBatch sb) {
            if (__instance.room instanceof ForcedEventRoom) {
                for (FlameAnimationEffect e : EffectsField.effects.get(__instance)) {
                    e.render(sb, ReflectionHacks.getPrivate(__instance, MapRoomNode.class, "scale"));
                }
            }
        }
    }

    @SpirePatch2(clz = MapRoomNode.class, method = "update", paramtypez = {})
    public static class UpdatePatch {
        @SpirePrefixPatch
        public static void update(MapRoomNode __instance) {
            if (__instance.room instanceof ForcedEventRoom) {
                TimerField.timer.set(__instance, TimerField.timer.get(__instance) - Gdx.graphics.getDeltaTime());
                if (TimerField.timer.get(__instance) < 0.0F) {
                    TimerField.timer.set(__instance, MathUtils.random(0.2F, 0.4F));
                    EffectsField.effects.get(__instance).add(new FlameAnimationEffect(__instance.hb));
                }
                for (FlameAnimationEffect e : EffectsField.effects.get(__instance)) {
                    if (e.isDone) {
                        e.dispose();
                    }
                }
                EffectsField.effects.get(__instance).removeIf(e -> e.isDone);
                for (FlameAnimationEffect e : EffectsField.effects.get(__instance)) {
                    e.update();
                }
            }
        }
    }
}
