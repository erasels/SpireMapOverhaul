package spireMapOverhaul.zones.windy.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zones.windy.WindyZone;

public class PostDamagePatch {
    @SpirePatch2(clz = AbstractMonster.class, method = "damage")
    public static class OnAttackedMonsterHook {
//        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        @SpirePostfixPatch
        public static void patch(AbstractMonster __instance, DamageInfo info) {
            if(ZonePatches.currentZone() instanceof WindyZone){
                ((WindyZone)ZonePatches.currentZone()).postDamageCheck(__instance);
            }
        }
//
//        private static class Locator extends SpireInsertLocator {
//            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
//                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "lastDamageTaken");
//                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
//            }
//        }
    }
}
