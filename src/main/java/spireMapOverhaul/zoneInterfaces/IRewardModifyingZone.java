package spireMapOverhaul.zoneInterfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Collections;

public interface IRewardModifyingZone {
    /**
     * Hook for adding any number of additional card rewards.
     * AbstractDungeon.cardRng should be the only RNG used in this method.
     * @return A list where each entry is the set of cards that should go in a card reward.
     */
    default ArrayList<ArrayList<AbstractCard>> getAdditionalCardRewards() {
        ArrayList<AbstractCard> cards = getAdditionalCardReward();
        return cards == null ? new ArrayList<>() : new ArrayList<>(Collections.singletonList(cards));
    }

    /**
     * Hook for adding a single additional card rewards (a convenience wrapper for getAdditionalCardRewards).
     * AbstractDungeon.cardRng should be the only RNG used in this method.
     * @return The list of cards that should go in a card reward.
     */
    default ArrayList<AbstractCard> getAdditionalCardReward() {
        return null;
    }

    /**
     * Hook for adding any number of additional rewards.
     * @return A list of rewards to add.
     */
    default ArrayList<RewardItem> getAdditionalRewards() {
        RewardItem reward = getAdditionalReward();
        return reward == null ? new ArrayList<>() : new ArrayList<>(Collections.singletonList(reward));
    }
    /**
     * Hook for adding a single additional rewards (a convenience wrapper for getAdditionalRewards).
     * @return A list of rewards to add.
     */
    default RewardItem getAdditionalReward() { return null; }

    /**
     * Hook for modifying the cards in each card reward.
     * You can add or remove cards, add card modifiers, etc.
     * Note that if you both add additional card rewards through getAdditionalCardRewards and implement this,
     * this will get called on the rewards you added.
     * @param cards The cards in the reward.
     */
    default void modifyRewardCards(ArrayList<AbstractCard> cards) {}

    /**
     * Hook for modifying each reward.
     * Check the rewardItem's type to determine what kind of reward it is (card, gold, relic, etc.).
     * You can change values (e.g. increase or reduce gold), substitute relics, change the image, etc.
     * Note that if you both add additional rewards and implement this, this will get called on the rewards you added.
     * For card rewards, this will be called after modifications from modifyRewardCards.
     * @param rewardItem The reward.
     */
    default void modifyReward(RewardItem rewardItem) {}

    /**
     * Hook for modifying the set of rewards.
     * You can remove rewards. For other use cases, it's be better to use one of the modify or getAdditional methods.
     * Note that the list of rewards will include any additional rewards you added and any modifications you made.
     * @param rewards The set of rewards.
     */
    default void modifyRewards(ArrayList<RewardItem> rewards) {}

    /**
     * Hook for triggering effects when a card is added to the deck.
     * @param card The card added.
     */
    default void onObtainCard(AbstractCard card) {}

    /**
     * Hook for changing the chance of rare cards in rewards.
     * @param rareCardRewardChance The base chance of rare cards in rewards. Effects such as N'loth's Gift are already applied.
     * @return The new rare card reward chance.
     */
    default int changeRareCardRewardChance(int rareCardRewardChance) {
        return rareCardRewardChance;
    }
}
