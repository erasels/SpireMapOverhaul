package spireMapOverhaul.zones.voidseed.campfire;

import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CtBehavior;
import spireMapOverhaul.zones.voidseed.cardmods.CorruptedModifier;

public class CorruptScreenPatch {

    public static boolean active = false;

    @SpirePatch2(clz = GridCardSelectScreen.class, method = "update")
    public static class CorruptScreenPatchUpdate {
        @SpireInsertPatch(locator = CorruptScreenPatchUpdateLocator.class)
        public static void Insert(GridCardSelectScreen __instance) {
            if (__instance.forUpgrade && active) {
                AbstractCard c = ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "hoveredCard");
                CardModifierManager.addModifier(c, new CorruptedModifier());

                __instance.upgradePreviewCard = c;
            }
        }
    }

    public static class CorruptScreenPatchUpdateLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(GridCardSelectScreen.class, "updateUpgradePreviewCard");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
