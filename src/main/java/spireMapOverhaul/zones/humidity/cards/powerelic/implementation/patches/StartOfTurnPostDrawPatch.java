package spireMapOverhaul.zones.humidity.cards.powerelic.implementation.patches;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.util.Wiz;

import java.util.ArrayList;

//under vanilla conditions, applyStartOfTurnPostDrawPowers is only called
// starting with the player's SECOND turn following DrawCardAction in GameActionManager.getNextAction.
// This patch adds it to AbstractRoom.update for the player's FIRST turn
// to immediately follow applyStartOfTurnPowers.
@SpirePatch2(clz = AbstractRoom.class, method = "update")
public class StartOfTurnPostDrawPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void Patch() {
        if(!Loader.isModLoaded("anniv7"))
            Wiz.adp().applyStartOfTurnPostDrawPowers();
    }
    static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnOrbs");
            return LineFinder.findInOrder(ctBehavior, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}