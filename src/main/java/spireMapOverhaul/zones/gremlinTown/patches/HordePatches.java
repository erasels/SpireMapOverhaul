package spireMapOverhaul.zones.gremlinTown.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.HordeHelper;

public class HordePatches {
    @SpirePatch2(
            clz = MonsterGroup.class,
            method = "areMonstersBasicallyDead"
    )
    public static class basicallyAlivePatch {
        @SpirePostfixPatch
        public static boolean Postfix(boolean __result) {
            if (AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE))
                return HordeHelper.areMonstersBasicallyDead(__result);
            return __result;
        }
    }
    @SpirePatch2(
            clz = MonsterGroup.class,
            method = "areMonstersDead"
    )
    public static class alivePatch {
        @SpirePostfixPatch
        public static boolean Postfix(boolean __result) {
            if (AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE))
                return HordeHelper.areMonstersDead(__result);
            return __result;
        }
    }

    @SpirePatch2(
            clz = MonsterGroup.class,
            method = "applyEndOfTurnPowers"
    )
    public static class updatePatch {
        @SpireInsertPatch (
                locator = Locator.class
        )
        public static void Insert() {
            if (AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE))
                HordeHelper.update();
        }

        public static class Locator extends SpireInsertLocator {
            private Locator() {}

            @Override
            public int[] Locate(CtBehavior behavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findInOrder(behavior, matcher);
            }
        }
    }

    @SpirePatch2(
            clz = AbstractRoom.class,
            method = "render"
    )
    public static class renderPatch {
        @SpireInsertPatch (
                locator = Locator.class,
                localvars = {"sb"}
        )
        public static void Insert(SpriteBatch sb) {
            if (AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE))
                HordeHelper.render(sb);
        }

        public static class Locator extends SpireInsertLocator {
            private Locator() {}

            @Override
            public int[] Locate(CtBehavior behavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "render");
                return LineFinder.findInOrder(behavior, matcher);
            }
        }
    }

    @SpirePatch2(
            clz = AbstractPlayer.class,
            method = "onVictory"
    )
    public static class victoryPatch {
        @SpirePrefixPatch
        public static void Prefix() {
            if (AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE))
                HordeHelper.onVictory();
        }
    }

    @SpirePatch2(
            clz = AbstractMonster.class,
            method = "die",
            paramtypez = boolean.class
    )
    public static class backAttackPatch {
        @SpirePostfixPatch
        public static void Postfix() {
            if (AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE)) {
                HordeHelper.calculateBackAttack();
            }
        }
    }
}
