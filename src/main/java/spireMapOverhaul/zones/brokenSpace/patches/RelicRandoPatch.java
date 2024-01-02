package spireMapOverhaul.zones.brokenSpace.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.zones.brokenSpace.BrokenSpaceZone;

import java.util.Objects;

public class RelicRandoPatch {
//    @SpirePatch2(clz = AbstractDungeon.class, method = "returnRandomRelicKey")
//    @SpirePatch2(clz = AbstractDungeon.class, method = "returnEndRandomRelicKey")
//    public static class RelicRandoPatch1 {
//        public static SpireReturn<String> Prefix() {
//            if (ZonePatches.currentZone() != null && ZonePatches.currentZone().id.equals("BrokenSpace")) {
//                return SpireReturn.Return(BrokenSpaceZone.GetTrulyRandomRelic(AbstractDungeon.relicRng).relicId);
//            }
//            return SpireReturn.Continue();
//        }
//    }
//
//    @SpirePatch2(clz = AbstractDungeon.class, method = "returnRandomRelic")
//    @SpirePatch2(clz = AbstractDungeon.class, method = "returnRandomRelicEnd")
//    public static class RelicRandoPatch2 {
//        public static void Postfix(AbstractRelic __result) {
//            if (ZonePatches.currentZone() != null && ZonePatches.currentZone().id.equals("BrokenSpace")) {
//                BrokenSpaceZone.UnnaturalRelicField.unnatural.set(__result, true);
//            }
//
//        }
//    }
}
