package spireMapOverhaul.zones.mirror.patches;

import basemod.BaseMod;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.RenderFixSwitches;
import basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.BackgroundFix;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;
import spireMapOverhaul.zones.mirror.cards.MirrorMove;

/*
 * MirrorMove uses a different card back than normal colorless card back.
 * This "card back" contains the image for the portrait part, allowing to draw the monster image on top of it.
 * But since MirrorMove's color is CardColor.COLORLESS and not a custom color, custom card backs are not rendered.
 * This patch changes isBaseGameCardColor() to return false while rendering MirrorMove card.
 */
public class MirrorMoveCardBackHack {

    @SpirePatch2(clz = BaseMod.class, method = "isBaseGameCardColor")
    public static class IsBaseGameCardColorPatch {
        public static boolean shouldPatch = false;

        @SpirePostfixPatch
        public static boolean Postfix(boolean __result) {
            if (shouldPatch) {
                return false;
            } else {
                return __result;
            }
        }
    }

    @SpirePatch2(clz = BackgroundFix.BackgroundTexture.class, method = "Prefix")
    public static class LargeCardBackPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"card"})
        public static void Insert(AbstractCard card) {
            if (card instanceof MirrorMove) {
                IsBaseGameCardColorPatch.shouldPatch = true;
            }
        }

        @SpirePostfixPatch
        public static void Postfix() {
            IsBaseGameCardColorPatch.shouldPatch = false;
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(BaseMod.class, "isBaseGameCardColor");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = RenderFixSwitches.RenderBgSwitch.class, method = "Prefix")
    public static class SmallCardBackPatch {

        /*
         * If you try to put __instance here, you get the "Cannot have __instance parameter for static method" error.
         * Here, I'm using Private Field Capture to get the parameter. Unlike the name suggests, Private Field Capture
         * can also capture parameters and localvars.
         */
        @SpirePrefixPatch
        public static void Prefix(AbstractCard _____instance) {
            if (_____instance instanceof MirrorMove) {
                IsBaseGameCardColorPatch.shouldPatch = true;
            }
        }

        @SpirePostfixPatch
        public static void Postfix() {
            IsBaseGameCardColorPatch.shouldPatch = false;
        }
    }

    /*
     * SingleCardViewPopup.renderCardBack() draws the colorless card back again,
     * overwriting the custom card back, so it needs to be disabled.
     */
    @SpirePatch2(clz = SingleCardViewPopup.class, method = "renderCardBack")
    public static class SingleCardViewPopupRenderPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> Insert(AbstractCard ___card) {
            if (___card instanceof MirrorMove) {
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderHelper");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
