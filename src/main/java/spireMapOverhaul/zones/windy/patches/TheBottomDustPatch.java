package spireMapOverhaul.zones.windy.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.scenes.TheBottomScene;
import spireMapOverhaul.zones.windy.WindyZone;

import static spireMapOverhaul.util.Wiz.getCurZone;

//windy dust is played throughout the zone, but exordium has non-windy dust, so it's disabled while in zone
public class TheBottomDustPatch {
    public static boolean inWindyZone() {
            return getCurZone() instanceof WindyZone;
        }
    @SpirePatch(clz = TheBottomScene.class, method = "updateDust")
    public static class StopParticlesPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix() {
            if (inWindyZone()) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
