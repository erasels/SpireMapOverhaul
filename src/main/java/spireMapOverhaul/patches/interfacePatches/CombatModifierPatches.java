package spireMapOverhaul.patches.interfacePatches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;

public class CombatModifierPatches {
    @SpirePatch2(clz =AbstractPlayer.class, method = "applyPreCombatLogic")
    public static class PreCombat {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atPreBattle);
        }
    }

    @SpirePatch2(clz =AbstractPlayer.class, method = "applyStartOfCombatLogic")
    public static class AtBattleStart {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atBattleStart);
        }
    }

    @SpirePatch2(clz =AbstractPlayer.class, method = "applyStartOfCombatPreDrawLogic")
    public static class AtBattleStartPreDraw {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atBattleStartPreDraw);
        }
    }

    @SpirePatch2(clz =AbstractPlayer.class, method = "applyStartOfTurnRelics")
    public static class AtTurnStart {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atTurnStart);
        }
    }

    @SpirePatch2(clz =AbstractPlayer.class, method = "applyStartOfTurnPostDrawRelics")
    public static class AtTurnStartPostDraw {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atTurnStartPostDraw);
        }
    }

    @SpirePatch2(clz = AbstractRoom.class, method = "applyEndOfTurnRelics")
    public static class AtTurnEnd {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atTurnEnd);
        }
    }

    @SpirePatch2(clz = MonsterGroup.class, method = "applyEndOfTurnPowers")
    public static class AtEndOfRound {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atRoundEnd);
        }
    }

    @SpirePatch2(clz = AbstractRoom.class, method = "endBattle")
    public static class OnVictory {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::onVictory);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "endBattleTimer");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "damage")
    public static class OnAttackedPlayerHook {
        @SpireInsertPatch(rloc = 57, localvars = {"damageAmount"}) // before onLoseHpLast loop
        public static void patch(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
             Wiz.forCurZone(CombatModifyingZone.class, z -> damageAmount[0] = z.onAttacked(info, damageAmount[0], __instance));
        }
    }

    @SpirePatch2(clz = AbstractMonster.class, method = "damage")
    public static class OnAttackedMonsterHook {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void patch(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {
            Wiz.forCurZone(CombatModifyingZone.class, z -> damageAmount[0] = z.onAttacked(info, damageAmount[0], __instance));
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "lastDamageTaken");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
