package spireMapOverhaul.patches.interfacePatches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;

import java.util.ArrayList;

public class CampfireModifierPatches {
    public static int optionsSelectedAmt = 0;

    @SpirePatch2(clz = CampfireUI.class, method = "initializeButtons")
    public static class PostButtonAddingCatcher {
        @SpireInsertPatch(rloc = 25) // 117: boolean cannotProceed = true;
        public static void postAddingButtons(CampfireUI __instance, ArrayList<AbstractCampfireOption> ___buttons) {
            Wiz.forCurZone(CampfireModifyingZone.class, z -> z.postAddButtons(___buttons));
        }
    }

    @SpirePatch2(clz = CampfireUI.class, method = "getCampMessage")
    public static class CampfireMessageCatcher {
        @SpireInsertPatch(locator = Locator.class, localvars = {"msgs"})
        public static void patch(ArrayList<String> msgs) {
            Wiz.forCurZone(CampfireModifyingZone.class, z -> z.postAddCampfireMessages(msgs));
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
            Wiz.forCurZone(CampfireModifyingZone.class, z -> z.postUseCampfireOption(__instance));
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(CampfireUI.class, "somethingSelected");
                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[1]}; // After useOption()
            }
        }
    }

    @SpirePatch2(clz = RestRoom.class, method = "onPlayerEntry")
    public static class RestRoomEntryCatcher {
        @SpirePostfixPatch
        public static void patch() {
            optionsSelectedAmt = 0;

            Wiz.forCurZone(CampfireModifyingZone.class, CampfireModifyingZone::onEnterRestRoom);
        }
    }
}
