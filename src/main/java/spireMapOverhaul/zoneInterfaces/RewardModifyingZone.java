package spireMapOverhaul.zoneInterfaces;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.daily.mods.Binary;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.Collections;

public interface RewardModifyingZone {
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
    default RewardItem getAdditionalReward() {
        return null;
    }

    /**
     * Hook for modifying the cards in each card reward.
     * You can add or remove cards, add card modifiers, etc.
     * Note that if you both add additional card rewards through getAdditionalCardRewards and implement this,
     * this will get called on the rewards you added.
     * @param cards The cards in the reward.
     */
    default void modifyRewardCards(ArrayList<AbstractCard> cards) {
    }

    /**
     * Hook for modifying each reward.
     * Check the rewardItem's type to determine what kind of reward it is (card, gold, relic, etc.).
     * You can change values (e.g. increase or reduce gold), substitute relics, change the image, etc.
     * Note that if you both add additional rewards and implement this, this will get called on the rewards you added.
     * For card rewards, this will be called after modifications from modifyRewardCards.
     * @param rewardItem The reward.
     */
    default void modifyReward(RewardItem rewardItem) {
    }

    /**
     * Hook for modifying the set of rewards.
     * You can remove rewards. For other use cases, it's be better to use one of the modify or getAdditional methods.
     * Note that the list of rewards will include any additional rewards you added and any modifications you made.
     * @param rewards The set of rewards.
     */
    default void modifyRewards(ArrayList<RewardItem> rewards) {
    }

    /**
     * Hook for triggering effects when a card is added to the deck.
     * @param card The card added.
     */
    default void onObtainCard(AbstractCard card) {
    }

    /**
     * Hook for changing the chance of rare cards in rewards.
     * Note: By increasing this number common cards become rarer due to the rolling logic of base game.
     *       roll < rareChance = rare, else roll < rare+uncommon = uncommon, else common
     * @param rareCardRewardChance The base chance of rare cards in rewards. Effects such as N'loth's Gift are already applied.
     * @return The new rare card reward chance.
     */
    default int changeRareCardRewardChance(int rareCardRewardChance) {
        return rareCardRewardChance;
    }

    /**
     * Hook for changing the chance of uncommon cards in rewards.
     * @param uncommonCardRewardChance The base chance of uncommon cards in rewards. Relic effects are already applied
     * @return The new uncommon card reward chance.
     */
    default int changeUncommonCardRewardChance(int uncommonCardRewardChance) {
        return uncommonCardRewardChance;
    }

    /**
     * Allows modifying base game behavior of not upgrading rare cards by chance when they're generated in card rewards.
     * @return When true, rare cards have a chance to be upgraded after normal cards have been upgraded and onPreviewObtain relics have done their thing.
     */
    default boolean allowUpgradingRareCards() {
        return false;
    }

    /**
     * Hook for changing the upgrade chance of cards generated in card rewards.
     * @param curChance The current chance which tends to be decided by the current dungeon, will be reset to this value after the card reward has been modified.
     * @return The newly modified chance.
     */
    default float changeCardUpgradeChance(float curChance) {
        return curChance;
    }

    /**
     * Helper method for getting the number of cards that should be in a standard reward.
     * You don't need to override this.
     * @return The number of cards that should be in the reward.
     */
    default int getNumberOfCardsInReward() {
        int numCards = 3;
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            numCards = r.changeNumberOfCardsInReward(numCards);
        }

        if (ModHelper.isModEnabled(Binary.ID)) {
            numCards--;
        }
        return numCards;
    }

    /**
     * Helper method for applying the standard upgrade logic to a card in a reward.
     * You don't need to override this.
     * @param card The card to apply the standard upgrade logic to.
     */
    default void applyStandardUpgradeLogic(AbstractCard card) {
        float upgradeChance = ReflectionHacks.getPrivateStatic(AbstractDungeon.class, "cardUpgradedChance");
        upgradeChance = this.changeCardUpgradeChance(upgradeChance);
        if ((card.rarity != AbstractCard.CardRarity.RARE || this.allowUpgradingRareCards()) && AbstractDungeon.cardRng.randomBoolean(upgradeChance) && card.canUpgrade()) {
            card.upgrade();
        } else {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onPreviewObtainCard(card);
            }
        }
    }

    default void applyStandardUpgradeLogic(ArrayList<AbstractCard> cards) {
        for(AbstractCard c : cards)
            applyStandardUpgradeLogic(c);
    }
}
