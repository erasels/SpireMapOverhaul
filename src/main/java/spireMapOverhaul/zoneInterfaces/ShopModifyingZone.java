package spireMapOverhaul.zoneInterfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public interface ShopModifyingZone {

    /**
     * Provides both lists that will end up being the cards that will be priced and put up in the shop.
     * @param coloredCards List that contains the top row of cards, those based on the character class.
     *                     Remember, normal distribution is attack x2, skill x2, power x1
     * @param colorlessCards The colorless cards, uncommon x1, rare x1
     */
    default void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {}

    /**
     * Happens after all messages are added to the list.
     * @param idleMessages The current list of messages which can be modified to your liking.
     */
    default void postAddIdleMessages(ArrayList<String> idleMessages) {}

    /**
     * Hook to modify the card's base cost before it is modified by ascensions and relics etc
     * @param c The card you're modifying the cost of
     * @param baseCost The current base cost (cost of rarity multiplied by 0.9-1.1 and additionally 1.2 if colorless)
     * @return New pre discount cost will be cast to int after
     */
    default float modifyCardBaseCost(AbstractCard c, float baseCost) {
        return baseCost;
    }
}
