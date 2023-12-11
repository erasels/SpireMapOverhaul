package spireMapOverhaul.patches.interfacePatches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CtBehavior;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.IRewardModifyingZone;

import java.util.ArrayList;
import java.util.Collections;

public class RewardModifierPatches {
    @SpirePatch2(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class AddCardRewardsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void AddCardRewards(CombatRewardScreen __instance) {
            AbstractZone zone = Wiz.getCurZone();
            if (zone instanceof IRewardModifyingZone) {
                ArrayList<ArrayList<AbstractCard>> additionalCardRewards = ((IRewardModifyingZone)zone).getAdditionalCardRewards();
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
                return new int[] { lines[0], lines[1] };
            }
        }
    }

    @SpirePatch2(clz = AbstractRoom.class, method = "update")
    public static class ModifyRewardsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void ModifyRewards(AbstractRoom __instance) {
            AbstractZone zone = Wiz.getCurZone();
            if (zone instanceof IRewardModifyingZone) {
                // We don't want to affect uses of CombatRewardScreen that are in non-battle events such as Sensory Stone
                if (AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrMapNode().room != null && AbstractDungeon.getCurrMapNode().room.isBattleOver) {
                    IRewardModifyingZone z = ((IRewardModifyingZone)zone);
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
}
