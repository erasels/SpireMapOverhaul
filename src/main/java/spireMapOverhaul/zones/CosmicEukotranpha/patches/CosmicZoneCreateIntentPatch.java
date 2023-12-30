package spireMapOverhaul.zones.CosmicEukotranpha.patches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.CosmicZoneMonster;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class CosmicZoneCreateIntentPatch{@SpirePatch(clz=MonsterGroup.class,method="showIntent")
public static class thing{@SpirePostfixPatch public static void Postfix(MonsterGroup __this){CosmicZoneMod.logger.info("Patch: CosmicZoneCreateIntentPatch triggered");
    for(AbstractMonster mo:AbstractDungeon.getMonsters().monsters){if(mo instanceof CosmicZoneMonster){((CosmicZoneMonster)mo).startOfTurnIntentCheck();}}}}}
