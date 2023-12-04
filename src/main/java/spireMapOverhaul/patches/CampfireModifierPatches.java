package spireMapOverhaul.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;

import java.util.ArrayList;

public class CampfireModifierPatches {
    @SpirePatch2(clz = CampfireUI.class, method = "initializeButtons")
    public static class PostButtonAddingCatcher {
        @SpireInsertPatch(rloc = 25) // 117: boolean cannotProceed = true;
        public static void postAddingButtons(CampfireUI __instance, ArrayList<AbstractCampfireOption> ___buttons) {
            AbstractZone curZone = ZonePatches.currentZone();
            if (curZone instanceof CampfireModifyingZone) {
                ((CampfireModifyingZone) curZone).postAddButtons(___buttons);
            }
        }
    }

    @SpirePatch2(clz = CampfireUI.class, method = "getCampMessage")
    public static class CampfireMessageCatcher {
        @SpireInsertPatch(locator = Locator.class, localvars = {"msgs"})
        public static void patch(ArrayList<String> msgs) {
            AbstractZone curZone = ZonePatches.currentZone();
            if (curZone instanceof CampfireModifyingZone) {
                ((CampfireModifyingZone) curZone).postAddCampfireMessages(msgs);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "get");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = AbstractCampfireOption.class, method = "update")
    public static class PostCampfireUseOptionCatcher {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractCampfireOption __instance) {
            AbstractZone curZone = ZonePatches.currentZone();
            if (curZone instanceof CampfireModifyingZone) {
                ((CampfireModifyingZone) curZone).postUseCampfireOption(__instance);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(CampfireUI.class, "somethingSelected");
                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[1]}; // After useOption()
            }
        }
    }
}
