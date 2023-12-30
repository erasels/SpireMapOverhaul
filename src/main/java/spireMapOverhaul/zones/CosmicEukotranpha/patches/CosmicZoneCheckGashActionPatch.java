package spireMapOverhaul.zones.CosmicEukotranpha.patches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.defect.GashAction;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicZoneGameActionHistory;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class CosmicZoneCheckGashActionPatch{@SpirePatch(clz=GashAction.class,method="update")
public static class thing{@SpirePostfixPatch public static void Postfix(GashAction __this){CosmicZoneMod.logger.info("Patch: CosmicZoneCheckGashActionPatch triggered");
    CosmicZoneGameActionHistory.gashAmount+=__this.amount;}}}
