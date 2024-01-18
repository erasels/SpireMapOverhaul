package spireMapOverhaul.zoneInterfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;

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
     * Returns the card that should be added to shop due to courier after another card is bought.
     * @param purchasedCard The purchased card that is being replaced.
     * @return The card to add to the shop. If null, the default Courier behavior will take place.
     */
    default AbstractCard getReplacementShopCardForCourier(AbstractCard purchasedCard) {
        return null;
    }

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

    /**
     * Method to modify the shop relics and their prices.
     * @param screen Screen reference needed for initializing a store relic
     * @param relics List of store relics that will be shown
     */
    default void postCreateShopRelics(ShopScreen screen, ArrayList<StoreRelic> relics) {}

    /**
     * Method to modify the shop potions and their prices.
     * @param screen Screen reference needed for initializing a store potion
     * @param potions List of store potions that will be shown
     */
    default void postCreateShopPotions(ShopScreen screen, ArrayList<StorePotion> potions) {}

    /**
     * Hook after the entire shop has been initialized for some final modifications.
     * @param screen Screen reference, many things here are private, may need to use reflection.
     * @param coloredCards List that contains the top row of cards
     * @param colorlessCards The colorless cards
     * @param relics List of store relics
     * @param potions List of store potions
     */
    default void postInitShop(ShopScreen screen, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards, ArrayList<StoreRelic> relics, ArrayList<StorePotion> potions) {}
}
