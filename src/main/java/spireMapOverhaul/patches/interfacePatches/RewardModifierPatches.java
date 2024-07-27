package spireMapOverhaul.patches.interfacePatches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import javassist.CtBehavior;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;

import java.util.ArrayList;
import java.util.Collections;

public class RewardModifierPatches {
    @SpirePatch2(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class AddCardRewardsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void AddCardRewards(CombatRewardScreen __instance) {
            AbstractZone zone = Wiz.getCurZone();
            if (zone instanceof RewardModifyingZone) {
                ArrayList<ArrayList<AbstractCard>> additionalCardRewards = ((RewardModifyingZone) zone).getAdditionalCardRewards();
                for (ArrayList<AbstractCard> cards : additionalCardRewards) {
                    RewardItem rewardItem = new RewardItem();
                    rewardItem.cards = cards;
                    __instance.rewards.add(rewardItem);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.NewExprMatcher(RewardItem.class);
                int[] lines = LineFinder.findAllInOrder(ctMethodToPatch, matcher);
                // We only want the first two uses, which are respectively the branch for the Vintage modifier (only
                // elites and bosses drop cards) and the normal branch. The third use is for Prayer Wheel.
                // The reason this matters (and why this isn't just a postfix patch) is because want to avoid adding
                // additional card rewards to fights that normally don't drop rewards, and we don't want to reproduce
                // the logic that CombatRewardScreen.setupItemRewards has for that.
                return new int[]{lines[0], lines[1]};
            }
        }
    }

    @SpirePatch2(clz = AbstractRoom.class, method = "update")
    public static class ModifyRewardsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void ModifyRewards(AbstractRoom __instance) {
            AbstractZone zone = Wiz.getCurZone();
            if (zone instanceof RewardModifyingZone) {
                // We don't want to affect uses of CombatRewardScreen that are in non-battle events such as Sensory Stone
                if (AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrMapNode().room != null && AbstractDungeon.getCurrMapNode().room.isBattleOver) {
                    RewardModifyingZone z = ((RewardModifyingZone) zone);
                    for (RewardItem rewardItem : AbstractDungeon.combatRewardScreen.rewards) {
                        if (rewardItem.type == RewardItem.RewardType.CARD && rewardItem.cards != null && !rewardItem.cards.isEmpty()) {
                            z.modifyRewardCards(rewardItem.cards);
                        }
                        z.modifyReward(rewardItem);
                    }
                    AbstractDungeon.combatRewardScreen.rewards.addAll(z.getAdditionalRewards());
                    z.modifyRewards(AbstractDungeon.combatRewardScreen.rewards);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                // We ensure that this happens after the save file is made, outside of any checks for loading saves
                // The block of rewards code this is part of runs both when you win a combat and when you save/reload
                // after combat, and the game relies on doing things in a specific order and monkeying with rng in ways
                // that make it very easy to create save/reload inconsistencies when adding or changing rewards
                // By ensuring that our patch happens after the save file is made, outside of any checks for loading saves,
                // we sidestep these issues
                Matcher firstMatcher = new Matcher.MethodCallMatcher(SaveAndContinue.class, "save");
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "loading_post_combat");
                return LineFinder.findInOrder(ctMethodToPatch, Collections.singletonList(firstMatcher), finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = ShowCardAndObtainEffect.class, method = "update")
    public static class OnObtainCardShowCardAndObtainPatch {
        @SpirePostfixPatch
        public static void OnObtainCardPatch(ShowCardAndObtainEffect __instance) {
            AbstractZone zone = Wiz.getCurZone();
            if (zone instanceof RewardModifyingZone) {
                AbstractCard card = ReflectionHacks.getPrivate(__instance, ShowCardAndObtainEffect.class, "card");
                if (__instance.isDone) {
                    ((RewardModifyingZone) zone).onObtainCard(card);
                }
            }
        }
    }

    @SpirePatch2(clz = FastCardObtainEffect.class, method = "update")
    public static class OnObtainCardFastCardObtainPatch {
        @SpirePostfixPatch
        public static void OnObtainCardPatch(FastCardObtainEffect __instance) {
            AbstractZone zone = Wiz.getCurZone();
            if (zone instanceof RewardModifyingZone) {
                AbstractCard card = ReflectionHacks.getPrivate(__instance, FastCardObtainEffect.class, "card");
                if (__instance.isDone) {
                    ((RewardModifyingZone) zone).onObtainCard(card);
                }
            }
        }
    }

    @SpirePatch2(clz = AbstractRoom.class, method = "alterCardRarityProbabilities")
    public static class ChangeCardRarityRewardChancePatch {
        @SpirePostfixPatch
        public static void patch(AbstractRoom __instance) {
            Wiz.forCurZone(RewardModifyingZone.class, z -> {
                __instance.uncommonCardChance = z.changeUncommonCardRewardChance(__instance.uncommonCardChance);
                __instance.rareCardChance = z.changeRareCardRewardChance(__instance.rareCardChance);
            });
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class UpgradeAndCardNumModification {
        private static float baseChance;

        @SpireInsertPatch(rloc = 64) // 1856: for (AbstractCard c : retVal2) {
        public static void preUpgradeRoll(@ByRef float[] ___cardUpgradedChance) {
            Wiz.forCurZone(RewardModifyingZone.class, z -> {
                float baseChance = ___cardUpgradedChance[0];
                ___cardUpgradedChance[0] = z.changeCardUpgradeChance(___cardUpgradedChance[0]);
            });
        }

        @SpireInsertPatch(rloc = 74, localvars = {"retVal2"}) // 1866: return retVal2;
        public static void postUpgradeRoll(@ByRef float[] ___cardUpgradedChance, ArrayList<AbstractCard> retVal2) {
            Wiz.forCurZone(RewardModifyingZone.class, z -> {
                if (z.allowUpgradingRareCards()) {
                    for (AbstractCard c : retVal2) {
                        if (c.rarity == AbstractCard.CardRarity.RARE && c.canUpgrade() && AbstractDungeon.cardRng.randomBoolean(___cardUpgradedChance[0])) {
                            c.upgrade();
                        }
                    }
                }

                ___cardUpgradedChance[0] = baseChance;
            });
        }

        @SpireInsertPatch(rloc = 13, localvars = {"numCards"}) // 1805: for (int i = 0; i < numCards; i++) {
        public static void changeNumberOfCardsInReward(@ByRef int[] numCards) {
            Wiz.forCurZone(RewardModifyingZone.class, z -> numCards[0] = z.changeNumberOfCardsInReward(numCards[0]));
        }
    }

    //Borrowed from replay the spire
     @SpirePatch(cls = "com.megacrit.cardcrawl.screens.CombatRewardScreen", method = "rewardViewUpdate")
        public static class ReplayRewardSkipPositionPatch {

        public static float HIDE_X = -1.0f;
        public static float SHOW_X = -1.0f;

         public static void Postfix(CombatRewardScreen __Instance) {
             AbstractZone zone = Wiz.getCurZone();

             if (zone instanceof RewardModifyingZone && ((RewardModifyingZone) zone).cannotSkipCardRewards()) {
                    if (HIDE_X == -1.0f) {
                        HIDE_X = AbstractDungeon.topPanel.mapHb.cX - 400.0f * Settings.scale;
                        SHOW_X = AbstractDungeon.topPanel.mapHb.cX;
                    }
                    boolean proceed = true;
                    for (int i = 0; i < __Instance.rewards.size(); i++){
                        if (__Instance.rewards.get(i).type == RewardItem.RewardType.CARD){
                            proceed = false;
                            break;
                        }
                    }
                    if (proceed) {
                        AbstractDungeon.overlayMenu.proceedButton.show();
                        AbstractDungeon.topPanel.mapHb.move(SHOW_X, AbstractDungeon.topPanel.mapHb.cY);
                    } else {
                        AbstractDungeon.overlayMenu.proceedButton.hide();
                        AbstractDungeon.overlayMenu.cancelButton.hide();
                        //ReflectionHacks.setPrivate((Object)__Instance, (Class)CombatRewardScreen.class, "labelOverride", (Object)null);
                        AbstractDungeon.topPanel.mapHb.move(ReplayRewardSkipPositionPatch.HIDE_X, AbstractDungeon.topPanel.mapHb.cY);
                    }
                }
            }
        }
}
