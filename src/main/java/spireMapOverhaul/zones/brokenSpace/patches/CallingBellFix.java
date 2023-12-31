package spireMapOverhaul.zones.brokenSpace.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.CallingBell;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import jdk.vm.ci.code.site.Call;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class CallingBellFix {

    public static boolean callingBellActive = false;

    @SpirePatch2(
            clz = ProceedButton.class,
            method = "update"
    )
    public static class antiSoftlock {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"hb"}
        )
        public static SpireReturn<Void> Prefix(Hitbox ___hb) {
            if (callingBellActive && ___hb.hovered && InputHelper.justClickedLeft) {
                callingBellActive = false;
                AbstractDungeon.closeCurrentScreen();

                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = CallingBell.class,
            method = "update"
    )
    public static class CallingBellUpdatePatch {
        public CallingBellUpdatePatch() {
        }// 24

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"cardsReceived"}
        )
        public static SpireReturn<Void> callingBellStart(boolean ___cardsReceived) throws InterruptedException {
            callingBellActive = !___cardsReceived;// 27
            return SpireReturn.Continue();// 29
        }

        public static class Locator extends SpireInsertLocator {
            public Locator() {
            }// 32

            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "open");// 34
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList(), finalMatcher);// 35
            }
        }
    }
}
