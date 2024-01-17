package spireMapOverhaul.zones.gremlinTown.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.GainBlockRandomMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.HordeHelper;

import java.util.ArrayList;

public class HordePatches {
    @SpirePatch2(
            clz = MonsterGroup.class,
            method = "areMonstersBasicallyDead"
    )
    public static class basicallyAlivePatch {
        @SpirePostfixPatch
        public static boolean Postfix(boolean __result) {
            if (AbstractDungeon.lastCombatMetricKey != null && AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE))
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
            if (AbstractDungeon.lastCombatMetricKey != null && AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE))
                return HordeHelper.areMonstersDead(__result);
            return __result;
        }
    }

    @SpirePatch2(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class updatePatch {
        @SpireInsertPatch (
                locator = Locator.class
        )
        public static void Insert() {
            if (AbstractDungeon.lastCombatMetricKey != null && AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE))
                HordeHelper.update();
        }

        public static class Locator extends SpireInsertLocator {
            private Locator() {}

            @Override
            public int[] Locate(CtBehavior behavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "totalDiscardedThisTurn");
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
            if (AbstractDungeon.lastCombatMetricKey != null && AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE))
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
            if (AbstractDungeon.lastCombatMetricKey != null && AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE))
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
            if (AbstractDungeon.lastCombatMetricKey != null && AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE)) {
                HordeHelper.calculateBackAttack();
            }
        }
    }

    @SpirePatch2(
            clz = GainBlockRandomMonsterAction.class,
            method = "update"
    )
    public static class shieldPatch {
        @SpireInsertPatch (
                locator = Locator.class,
                localvars = {"validMonsters"}
        )
        public static void Insert(ArrayList<AbstractMonster> validMonsters) {
            validMonsters.removeIf(AbstractCreature::isDeadOrEscaped);
        }

        public static class Locator extends SpireInsertLocator {
            private Locator() {}

            @Override
            public int[] Locate(CtBehavior behavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
                return LineFinder.findInOrder(behavior, matcher);
            }
        }
    }

    @SpirePatch2(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class stackingBackAttackPatch {
        @SpireInsertPatch (
                locator = Locator.class
        )
        public static SpireReturn Insert(ApplyPowerAction __instance) {
            AbstractPower pow = ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
            AbstractCreature target = ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "target");
            if (pow instanceof BackAttackPower && target.hasPower(BackAttackPower.POWER_ID)) {
                __instance.isDone = true;
                return SpireReturn.Return();
            } else
                return SpireReturn.Continue();
        }

        public static class Locator extends SpireInsertLocator {
            private Locator() {}

            @Override
            public int[] Locate(CtBehavior behavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(ApplyPowerAction.class, "powerToApply");
                return LineFinder.findInOrder(behavior, matcher);
            }
        }
    }

    @SpirePatch(
            clz= AbstractMonster.class,
            method=SpirePatch.CLASS
    )
    public static class GremlinField
    {
        public static SpireField<Boolean> horde = new SpireField<>(() -> false);
    }

    @SpirePatch2(
            clz = AbstractMonster.class,
            method = "applyPowers"
    )
    public static class ApplyPowersOptimizationPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractMonster __instance) {
            if (__instance.isDeadOrEscaped() && GremlinField.horde.get(__instance))
                return SpireReturn.Return();
            else
                return SpireReturn.Continue();
        }
    }
}
