package spireMapOverhaul.patches.interfacePatches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import java.util.ArrayList;
import java.util.Arrays;

public class ShopModifierPatches {
    @SpirePatch2(clz= Merchant.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {float.class, float.class, int.class})
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
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ShopScreen.class, "init");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = ShopScreen.class, method = "init")
    public static class ShopScreenIdleMessages {
        @SpirePostfixPatch
        public static void patch(ShopScreen __instance, ArrayList<String> ___idleMessages) {
            Wiz.forCurZone(ShopModifyingZone.class, z -> z.postAddIdleMessages(___idleMessages));
        }
    }

    @SpirePatch2(clz= ShopScreen.class, method = "initCards")
    public static class BaseCostModificationHook {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c", "tmpPrice"})
        public static void patch(AbstractCard c, @ByRef float[] tmpPrice) {
            Wiz.forCurZone(ShopModifyingZone.class, z -> tmpPrice[0] = z.modifyCardBaseCost(c, tmpPrice[0]));
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "price");
                return Arrays.copyOfRange(LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher), 0, 2);
            }
        }
    }

    @SpirePatch2(clz= ShopScreen.class, method = "initRelics")
    public static class ShopRelicHook {
        @SpirePostfixPatch
        public static void patch(ShopScreen __instance, ArrayList<StoreRelic> ___relics) {
            Wiz.forCurZone(ShopModifyingZone.class, z -> z.postCreateShopRelics(__instance, ___relics));
        }
    }

    @SpirePatch2(clz= ShopScreen.class, method = "initPotions")
    public static class ShopPotionHook {
        @SpirePostfixPatch
        public static void patch(ShopScreen __instance, ArrayList<StorePotion> ___potions) {
            Wiz.forCurZone(ShopModifyingZone.class, z -> z.postCreateShopPotions(__instance, ___potions));
        }
    }

    @SpirePatch2(clz= ShopScreen.class, method = "init")
    public static class PostInitHook {
        @SpirePostfixPatch
        public static void patch(ShopScreen __instance, ArrayList<AbstractCard> ___coloredCards, ArrayList<AbstractCard> ___colorlessCards, ArrayList<StoreRelic> ___relics, ArrayList<StorePotion> ___potions) {
            Wiz.forCurZone(ShopModifyingZone.class, z -> z.postInitShop(__instance, ___coloredCards, ___colorlessCards, ___relics, ___potions));
        }
    }
}
