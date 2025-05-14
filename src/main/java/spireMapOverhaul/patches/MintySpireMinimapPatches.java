package spireMapOverhaul.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;

public class MintySpireMinimapPatches {
    private static boolean needsReset = false;
    private static int resetFrameDelay = 0;
    private static final int FRAME_DELAY = 2;  // Wait a couple frames before resetting

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "dungeonTransitionSetup"
    )
    public static class TransitionPatch {
        @SpirePostfixPatch
        public static void Postfix() {
            needsReset = true;
            resetFrameDelay = FRAME_DELAY;
        }
    }

    @SpirePatch(
            optional = true,
            cls = "mintySpire.patches.map.MiniMapDisplay",
            method = "renderMinimap"
    )
    public static class MinimapPatch {
        @SpirePostfixPatch
        public static void Postfix(SpriteBatch sb, float x, float y, Object camera) {
            if (needsReset) {
                if (resetFrameDelay > 0) {
                    resetFrameDelay--;
                } else {
                    for (AbstractZone zone : BetterMapGenerator.getActiveZones(AbstractDungeon.map)) {
                        if (zone != null) {
                            zone.shapeRegion = null;
                        }
                    }
                    needsReset = false;
                }
            }
        }
    }
}