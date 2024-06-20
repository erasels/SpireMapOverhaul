package spireMapOverhaul.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.DungeonMap;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;

import static spireMapOverhaul.SpireAnniversary6Mod.hasDarkmap;

public class BetterMapRenderPatch {
    @SpirePatch(
            clz = DungeonMap.class,
            method = "renderMapBlender"
    )
    public static class NormalMapPatch {
        @SpirePostfixPatch
        public static void render(DungeonMap __instance, SpriteBatch sb) {
            if (!hasDarkmap) {
                renderZones(sb);
            }
        }
    }

    @SpirePatch(
            clz = DungeonMap.class,
            method = "renderNormalMap",
            requiredModId = "ojb_DarkMap"
    )
    public static class DarkMapPatch {
        @SpirePostfixPatch
        public static void render(DungeonMap __instance, SpriteBatch sb) {
            renderZones(sb);
        }
    }

    private static void renderZones(SpriteBatch sb) {
        Color old = sb.getColor();

        for (AbstractZone zone : BetterMapGenerator.getActiveZones(AbstractDungeon.map)) {
            zone.renderOnMap(sb, old.a);
        }

        sb.setColor(old);
    }
}
