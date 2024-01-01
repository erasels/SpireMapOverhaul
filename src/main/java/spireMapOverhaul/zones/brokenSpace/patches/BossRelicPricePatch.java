package spireMapOverhaul.zones.brokenSpace.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Objects;

@SpirePatch(
        clz = AbstractRelic.class,
        method = "getPrice"
)
public class BossRelicPricePatch {


    @SpirePrefixPatch
    public static SpireReturn<Integer> Prefix(AbstractRelic relic) {
        // 14
        if (Objects.requireNonNull(relic.tier) == AbstractRelic.RelicTier.BOSS) {
            return SpireReturn.Return(500);// 30
        }
        return SpireReturn.Continue();
    }
}