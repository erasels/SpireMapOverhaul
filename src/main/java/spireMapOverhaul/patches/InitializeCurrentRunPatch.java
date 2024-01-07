package spireMapOverhaul.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.HashSet;
import java.util.stream.Collectors;

@SpirePatch2(clz = AbstractDungeon.class, method = "generateSeeds")
public class InitializeCurrentRunPatch {
    @SpirePostfixPatch
    public static void initializeCurrentRun() {
        SpireAnniversary6Mod.currentRunActive = SpireAnniversary6Mod.getActiveConfig();
        SpireAnniversary6Mod.currentRunAllZones = SpireAnniversary6Mod.unfilteredAllZones.stream()
                .map(z -> z.id)
                .filter(SpireAnniversary6Mod::getFilterConfig)
                .collect(Collectors.toCollection(HashSet::new));
        SpireAnniversary6Mod.currentRunSeenZones = new HashSet<>();
    }
}
