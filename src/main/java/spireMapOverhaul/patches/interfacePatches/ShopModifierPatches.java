package spireMapOverhaul.patches.interfacePatches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.apache.commons.lang3.math.NumberUtils;
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
            Wiz.forCurZone(ShopModifyingZone.class, z -> tmpPrice[0] = NumberUtils.max(0, z.modifyCardBaseCost(c, tmpPrice[0])));
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "price");
                return Arrays.copyOfRange(LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher), 0, 2);
            }
        }
    }

    @SpirePatch2(clz = ShopScreen.class, method = "setPrice", paramtypez = { AbstractCard.class })
    public static class BaseCostModificationHookForCourier {
        @SpireInsertPatch(locator = Locator.class, localvars = { "tmpPrice" })
        public static void patch(AbstractCard card, @ByRef float[] tmpPrice) {
            Wiz.forCurZone(ShopModifyingZone.class, z -> tmpPrice[0] = NumberUtils.max(0, z.modifyCardBaseCost(card, tmpPrice[0])));
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                // In order to have this happen after the colorless card price modification, but before Courier and Membership Card
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
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

    @SpirePatch2(clz = ShopScreen.class, method = "purchaseCard")
    public static class CourierPatch {
        public static class CourierPatchExprEditor extends ExprEditor {
            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                if (methodCall.getClassName().equals(ArrayList.class.getName()) && methodCall.getMethodName().equals("set")) {
                    methodCall.replace(String.format("{ if (%1$s.handleCourier(this, $0, $2, hoveredCard)) { $_ = hoveredCard; } else { $_ = $proceed($$); } }", CourierPatch.class.getName()));
                }
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor shopCourierPatch() {
            return new CourierPatchExprEditor();
        }

        public static boolean handleCourier(ShopScreen shopScreen, ArrayList<AbstractCard> list, Object replacementCard, AbstractCard purchasedCard) {
            if ((list == shopScreen.coloredCards || list == shopScreen.colorlessCards) && Wiz.getCurZone() instanceof ShopModifyingZone) {
                ShopModifyingZone smz = ((ShopModifyingZone)Wiz.getCurZone());
                AbstractCard card = (AbstractCard)replacementCard;
                AbstractCard zoneReplacementCard = smz.getReplacementShopCardForCourier(purchasedCard);
                if (zoneReplacementCard != null) {
                    for (AbstractRelic r : AbstractDungeon.player.relics) {
                        r.onPreviewObtainCard(zoneReplacementCard);
                    }
                    card = zoneReplacementCard;

                    card.current_x = purchasedCard.current_x;
                    card.current_y = purchasedCard.current_y;
                    card.target_x = card.current_x;
                    card.target_y = card.current_y;
                    ReflectionHacks.privateMethod(ShopScreen.class, "setPrice", AbstractCard.class).invoke(shopScreen, card);
                }

                int coloredIndex = shopScreen.coloredCards.indexOf(purchasedCard);
                if (coloredIndex != -1) {
                    shopScreen.coloredCards.set(coloredIndex, card);
                }

                int colorlessIndex = shopScreen.colorlessCards.indexOf(purchasedCard);
                if (colorlessIndex != -1) {
                    shopScreen.colorlessCards.set(colorlessIndex, card);
                }

                return true;
            }
            return false;
        }
    }
}
