package spireMapOverhaul.zones.humidity.encounters;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.city.SnakePlant;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.SneckoPlantPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class SnakePlantPatches {

    public static final String ID = makeID("SneckoPlant");
    public static final String NAME = CardCrawlGame.languagePack.getMonsterStrings(ID).NAME;

    @SpirePatch2(clz = SnakePlant.class, method = SpirePatch.CONSTRUCTOR)
    public static class NamePatch {
        @SpirePostfixPatch
        public static void Foo(SnakePlant __instance) {
            if (HumidityZone.isNotInZone()) return;
            __instance.name = NAME;
            __instance.powers.add(new SneckoPlantPower(__instance));
        }
    }

    @SpirePatch2(clz = SnakePlant.class, method = "getMove")
    public static class IntentPatch {
        @SpirePostfixPatch
        public static void Foo(SnakePlant __instance) {
            if (HumidityZone.isNotInZone()) return;
            AbstractPower p = __instance.getPower(SneckoPlantPower.POWER_ID);
            if (p == null) return;
            EnemyMoveInfo move = ReflectionHacks.getPrivate(__instance, AbstractMonster.class, "move");
            if (move.intent == AbstractMonster.Intent.ATTACK && move.isMultiDamage) {
                move.multiplier = ((SneckoPlantPower) p).nextTurnRoll;
            }
            //no need to updateIntentTip as the intent hasn't been created yet
        }
    }

    @SpirePatch2(clz = SnakePlant.class, method = "takeTurn")
    public static class TurnPatch {
        @SpireInsertPatch(rloc = 6, localvars = {"numBlows"})
        public static void Foo(SnakePlant __instance, @ByRef int[] numBlows) {
            if (HumidityZone.isNotInZone()) return;
            AbstractPower p = __instance.getPower(SneckoPlantPower.POWER_ID);
            if (p == null) return;
            numBlows[0] = ((SneckoPlantPower) p).thisTurnRoll;
        }

    }

}
