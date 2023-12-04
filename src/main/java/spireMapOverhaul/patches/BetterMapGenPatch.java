package spireMapOverhaul.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import spireMapOverhaul.BetterMapGenerator;

import java.util.ArrayList;

@SpirePatch(
        clz = MapGenerator.class,
        method = "generateDungeon"
)
public class BetterMapGenPatch {
    public static float chance = 1; //placeholder

    @SpirePrefixPatch
    public static SpireReturn<ArrayList<ArrayList<MapRoomNode>>> altGen(int height, int width, int pathDensity, Random rng) {
        if (chance >= 1 || rng.copy().randomBoolean(chance)) { //Avoid affecting map gen if false
            return SpireReturn.Return(BetterMapGenerator.generator.generate(rng, width, height, pathDensity));
        }
        return SpireReturn.Continue();
    }
}
