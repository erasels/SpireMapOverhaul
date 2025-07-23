package spireMapOverhaul.zones.humidity.encounters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class LagavulinSleepy {

    @SpirePatch2(
            clz = Lagavulin.class,
            method = "usePreBattleAction"
    )
    public static class IntentPatch1 {
        @SpirePostfixPatch
        public static void Foo(Lagavulin __instance) {
            if (HumidityZone.isNotInZone()) return;
            EnemyMoveInfo ___move = ReflectionHacks.getPrivate(__instance, AbstractMonster.class, "move");
            if (___move.nextMove == 1) {
                __instance.setMove((byte) 7, AbstractMonster.Intent.UNKNOWN);
            }
        }
    }

    @SpirePatch2(
            clz = Lagavulin.class,
            method = "getMove"
    )
    public static class IntentPatch2 {
        @SpirePostfixPatch
        public static void Foo(Lagavulin __instance) {
            if (HumidityZone.isNotInZone()) return;
            EnemyMoveInfo ___move = ReflectionHacks.getPrivate(__instance, AbstractMonster.class, "move");
            //SpireAnniversary6Mod.logger.info("Move: "+___move.nextMove);
            if (___move.nextMove == 1) {
                __instance.setMove((byte) 7, AbstractMonster.Intent.UNKNOWN);
            }
        }
    }

    @SpirePatch2(
            clz = Lagavulin.class,
            method = "takeTurn"
    )
    public static class MovePatch {
        @SpirePostfixPatch
        public static void Foo(Lagavulin __instance, @ByRef boolean[] ___asleep, @ByRef boolean[] ___isOut, @ByRef boolean[] ___isOutTriggered, @ByRef int[] ___idleCount, @ByRef int[] ___debuffTurnCount) {
            if (HumidityZone.isNotInZone()) return;
            if (__instance.nextMove == 7) {
                AnimationStateData ___stateData = ReflectionHacks.getPrivate(__instance, AbstractCreature.class, "stateData");
                AnimationState.TrackEntry e = null;
                ReflectionHacks.privateMethod(AbstractCreature.class, "loadAnimation", String.class, String.class, float.class).invoke(__instance, "images/monsters/theBottom/lagavulin/skeleton.atlas", "images/monsters/theBottom/lagavulin/skeleton.json", 1.0F);
                //__instance.loadAnimation("images/monsters/theBottom/lagavulin/skeleton.atlas", "images/monsters/theBottom/lagavulin/skeleton.json", 1.0F);
                e = __instance.state.setAnimation(0, "Idle_1", true);
                ReflectionHacks.privateMethod(AbstractMonster.class, "updateHitbox", float.class, float.class, float.class, float.class).invoke(__instance, 0.0F, -25.0F, 320.0F, 220.0F);
                e.setTime(e.getEndTime() * MathUtils.random());
                AbstractDungeon.scene.fadeInAmbiance();
                CardCrawlGame.music.fadeOutTempBGM();
                CardCrawlGame.music.precacheTempBgm("ELITE");

                ___asleep[0] = true;
                ___isOut[0] = false;
                ___isOutTriggered[0] = false;
                ___idleCount[0] = 0;
                ___debuffTurnCount[0] = 0;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new MetallicizePower(__instance, 8), 8));
                //no need to apply initial block in addition to metallicize
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(__instance));
            }
        }
    }

}
