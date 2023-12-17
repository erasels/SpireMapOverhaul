package spireMapOverhaul.patches.interfacePatches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.shop.Merchant;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import java.util.ArrayList;

public class ShopModifierPatches {
    @SpirePatch2(clz= Merchant.class, method = SpirePatch.CONSTRUCTOR)
    public static class PostCreateCardsHook {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(Merchant __instance, ArrayList<AbstractCard> ___cards1, ArrayList<AbstractCard> ___cards2, ArrayList<String> ___idleMessages) {
            Wiz.forCurZone(ShopModifyingZone.class, z -> {
                z.postCreateShopCards(___cards1, ___cards2);
                z.postAddIdleMessages(___idleMessages);
            });
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(Merchant.class, "speechTimer");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
