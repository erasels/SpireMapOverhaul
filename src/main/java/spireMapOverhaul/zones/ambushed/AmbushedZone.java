package spireMapOverhaul.zones.ambushed;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.*;
import com.megacrit.cardcrawl.cards.colorless.*;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.cards.purple.*;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.GamblersBrew;
import com.megacrit.cardcrawl.potions.SneckoOil;
import com.megacrit.cardcrawl.potions.SwiftPotion;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class AmbushedZone extends AbstractZone implements CombatModifyingZone, EncounterModifyingZone, RewardModifyingZone, ShopModifyingZone {
    public static final String ID = "Ambushed";
    private static final String AMBUSHED_BY_RATS = makeID("AMBUSHED_BY_RATS");
    private static final String AMBUSHED_BY_JAW_SLIME = makeID("AMBUSHED_BY_JAW_SLIME");
    private static final String AMBUSHED_BY_THIEF_SLIME = makeID("AMBUSHED_BY_THIEF_SLIME");
    private static final String AMBUSHED_BY_JAW_LOUSE = makeID("AMBUSHED_BY_JAW_LOUSE");
    private static final String AMBUSHED_BY_THIEVES = makeID("AMBUSHED_BY_THIEVES");
    private static final String AMBUSHED_BY_AVOCADO_RAT = makeID("AMBUSHED_BY_AVOCADO_RAT");
    private static final String AMBUSHED_BY_SENTRY_BASEBALL = makeID("AMBUSHED_BY_SENTRY_BASEBALL");
    private static final String AMBUSHED_BY_POWER_COUPLE = makeID("AMBUSHED_BY_POWER_COUPLE");
    private static final String AMBUSHED_BY_BYRD_CHOSEN = makeID("AMBUSHED_BY_BYRD_CHOSEN");
    private static final String AMBUSHED_BY_CULTIST_CHOSEN = makeID("AMBUSHED_BY_CULTIST_CHOSEN");
    private static final String AMBUSHED_BY_SNOOZE_RAT = makeID("AMBUSHED_BY_SNOOZE_RAT");
    private static final String AMBUSHED_BY_LOUSE_NOB = makeID("AMBUSHED_BY_LOUSE_NOB");
    private static final String AMBUSHED_BY_TASKMASTER_NOB = makeID("AMBUSHED_BY_TASKMASTER_NOB");
    private static final String AMBUSHED_BY_PHILO_BOYS = makeID("AMBUSHED_BY_PHILO_BOYS");
    private static final String AMBUSHED_BY_CULTIST_SLIME = makeID("AMBUSHED_BY_CULTIST_SLIME");

    private ArrayList<String> cardDrawCardIDs; // List of card IDs that offer immediate card draw

    public AmbushedZone() {
        super(ID, Icons.MONSTER, Icons.SHOP);
        this.width = 3;
        this.height = 3;
        initCardDrawCardIDs();
    }

    @Override
    public AbstractZone copy() {
        return new AmbushedZone();
    }

    @Override
    public Color getColor() {
        return new Color(0.588f, 0.106f, 0.106f, 0.86f);
    }

    @Override
    public boolean canSpawn() {
        return !this.isAct(3);
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
    }

    private void initCardDrawCardIDs() {
        cardDrawCardIDs = new ArrayList<>();
        cardDrawCardIDs.add(PommelStrike.ID);
        cardDrawCardIDs.add(ShrugItOff.ID);
        cardDrawCardIDs.add(BattleTrance.ID);
        cardDrawCardIDs.add(BurningPact.ID);
        cardDrawCardIDs.add(Offering.ID);
        cardDrawCardIDs.add(Acrobatics.ID);
        cardDrawCardIDs.add(Backflip.ID);
        cardDrawCardIDs.add(DaggerThrow.ID);
        cardDrawCardIDs.add(Prepared.ID);
        cardDrawCardIDs.add(QuickSlash.ID);
        cardDrawCardIDs.add(CalculatedGamble.ID);
        cardDrawCardIDs.add(EscapePlan.ID);
        cardDrawCardIDs.add(Expertise.ID);
        cardDrawCardIDs.add(Adrenaline.ID);
        cardDrawCardIDs.add(Coolheaded.ID);
        cardDrawCardIDs.add(SweepingBeam.ID);
        cardDrawCardIDs.add(Overclock.ID);
        cardDrawCardIDs.add(Skim.ID);
        cardDrawCardIDs.add(Reboot.ID);
        cardDrawCardIDs.add(CutThroughFate.ID);
        cardDrawCardIDs.add(EmptyMind.ID);
        cardDrawCardIDs.add(Sanctity.ID);
        cardDrawCardIDs.add(WheelKick.ID);
        cardDrawCardIDs.add(Scrawl.ID);
        cardDrawCardIDs.add(DeepBreath.ID);
        cardDrawCardIDs.add(Finesse.ID);
        cardDrawCardIDs.add(FlashOfSteel.ID);
        cardDrawCardIDs.add(Impatience.ID);
        cardDrawCardIDs.add(MasterOfStrategy.ID);
        cardDrawCardIDs.add(ThinkingAhead.ID);
        cardDrawCardIDs.add(Violence.ID);
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> rewardCards) {
        HashSet<String> selectedCardIDs = new HashSet<>(); // To keep track of selected card IDs
        ArrayList<AbstractCard> newCards = new ArrayList<>();
        for (AbstractCard card : rewardCards) {
            AbstractCard newCard = getCardDrawCard(card.rarity, selectedCardIDs);
            if (newCard != null) {
                newCards.add(newCard);
            }
        }
        rewardCards.clear();
        applyStandardUpgradeLogic(newCards);
        rewardCards.addAll(newCards);
    }


    private AbstractCard getCardDrawCard(AbstractCard.CardRarity rarity, HashSet<String> selectedCardIDs) {
        if(!Arrays.asList(AbstractCard.CardRarity.COMMON, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardRarity.RARE).contains(rarity)) {
            rarity = AbstractCard.CardRarity.UNCOMMON;
        }

        // Filter the cardDrawCardIDs list by the specified rarity
        AbstractCard.CardRarity finalRarity = rarity;
        List<String> filteredCardIDs = cardDrawCardIDs.stream()
                .filter(id -> isCardOfRarity(id, finalRarity))
                .collect(Collectors.toList());

        // Exclude already selected card IDs to avoid duplicates
        filteredCardIDs.removeAll(selectedCardIDs);

        if (filteredCardIDs.isEmpty()) {
            return null; // Return null or a default card if no matching cards are found
        }

        // Randomly select a card ID from the filtered list
        String selectedCardID = filteredCardIDs.get(AbstractDungeon.cardRng.random(filteredCardIDs.size() - 1));
        selectedCardIDs.add(selectedCardID); // Add to the set of selected card IDs

        // Return the card from the game's card library based on the selected ID
        return CardLibrary.getCard(selectedCardID).makeCopy();
    }

    private boolean isCardOfRarity(String cardID, AbstractCard.CardRarity rarity) {
        AbstractCard card = CardLibrary.getCard(cardID);
        return card != null && card.rarity == rarity;
    }

    @Override
    public void beforePreBattlePrep() {
        AbstractDungeon.player.movePosition((float) Settings.WIDTH / 2.0F, AbstractDungeon.floorY);
    }

    @Override
    public void atPreBattle() {
        AbstractDungeon.player.powers.add(new SurroundedPower(AbstractDungeon.player));
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        replaceShopCards(coloredCards, true); // Replace colored cards
        replaceShopCards(colorlessCards, false); // Replace colorless cards
    }

    private void replaceShopCards(ArrayList<AbstractCard> cards, boolean isColored) {
        ArrayList<AbstractCard> newCards = new ArrayList<>();
        HashSet<String> selectedCardIDs = new HashSet<>();

        for (AbstractCard card : cards) {
            AbstractCard newCard = getCardForShop(card.rarity, card.type, isColored, selectedCardIDs);
            if (newCard != null) {
                newCards.add(newCard);
            }
        }

        cards.clear();
        cards.addAll(newCards);
    }

    private AbstractCard getCardForShop(AbstractCard.CardRarity rarity, AbstractCard.CardType type, boolean isColored, HashSet<String> selectedCardIDs) {
        List<String> filteredCardIDs = getFilteredCardIDsForShop(rarity, type, isColored, selectedCardIDs);

        // Fallback 1: If no cards of the specified type, try another type
        if (filteredCardIDs.isEmpty()) {
            for (AbstractCard.CardType fallbackType : AbstractCard.CardType.values()) {
                if (fallbackType != type) {
                    filteredCardIDs = getFilteredCardIDsForShop(rarity, fallbackType, isColored, selectedCardIDs);
                    if (!filteredCardIDs.isEmpty()) break;
                }
            }
        }

        // Fallback 2: If still no cards, try a different rarity
        if (filteredCardIDs.isEmpty()) {
            for (AbstractCard.CardRarity fallbackRarity : AbstractCard.CardRarity.values()) {
                if (fallbackRarity != rarity) {
                    filteredCardIDs = getFilteredCardIDsForShop(fallbackRarity, type, isColored, selectedCardIDs);
                    if (!filteredCardIDs.isEmpty()) break;
                }
            }
        }

        if (filteredCardIDs.isEmpty()) {
            return null;
        }

        String selectedCardID = filteredCardIDs.get(AbstractDungeon.cardRng.random(filteredCardIDs.size() - 1));
        selectedCardIDs.add(selectedCardID);

        return CardLibrary.getCard(selectedCardID).makeCopy();
    }


    private List<String> getFilteredCardIDsForShop(AbstractCard.CardRarity rarity, AbstractCard.CardType type, boolean isColored, HashSet<String> selectedCardIDs) {
        // Filter your card draw cards by rarity, type, and whether it's colored or colorless
        return cardDrawCardIDs.stream()
                .filter(id -> isCardForShop(id, rarity, type, isColored))
                .filter(id -> !selectedCardIDs.contains(id)) // Exclude already selected card IDs
                .collect(Collectors.toList());
    }


    private boolean isCardForShop(String cardID, AbstractCard.CardRarity rarity, AbstractCard.CardType type, boolean isColored) {
        AbstractCard card = CardLibrary.getCard(cardID);
        boolean isCardColored = card.color != AbstractCard.CardColor.COLORLESS;
        return card != null && card.rarity == rarity && card.type == type && isCardColored == isColored;
    }

    @Override
    public AbstractCard getReplacementShopCardForCourier(AbstractCard purchasedCard) {
        boolean isColored = purchasedCard.color != AbstractCard.CardColor.COLORLESS;
        AbstractCard replacementCard = getCardForCourier(purchasedCard.type, isColored);

        if (replacementCard == null) {
            return null;
        }

        return replacementCard;
    }

    private AbstractCard getCardForCourier(AbstractCard.CardType type, boolean isColored) {

        // Filter cards by type and whether it's colored or colorless
        List<String> filteredCardIDs = cardDrawCardIDs.stream()
                .filter(id -> isCardSuitableForCourier(id, type, isColored))
                .collect(Collectors.toList());

        if (!filteredCardIDs.isEmpty()) {
            String selectedCardID = filteredCardIDs.get(AbstractDungeon.cardRng.random(filteredCardIDs.size() - 1));
            return CardLibrary.getCard(selectedCardID).makeCopy();
        }

        return null;
    }

    private boolean isCardSuitableForCourier(String cardID, AbstractCard.CardType type, boolean isColored) {
        AbstractCard card = CardLibrary.getCard(cardID);
        boolean isCardColored = card.color != AbstractCard.CardColor.COLORLESS;
        return card != null && card.type == type && isCardColored == isColored;
    }


    @Override
    public void postCreateShopPotions(ShopScreen screen, ArrayList<StorePotion> potions) {
        if (potions.size() >= 3) {
            potions.clear();

            // Add Swift Potion with its appropriate price
            potions.add(new StorePotion(PotionHelper.getPotion(SwiftPotion.POTION_ID), 0, screen));

            // Add Gambler's Brew with its appropriate price
            potions.add(new StorePotion(PotionHelper.getPotion(GamblersBrew.POTION_ID), 1, screen));

            // Add Snecko Oil with its appropriate price
            potions.add(new StorePotion(PotionHelper.getPotion(SneckoOil.POTION_ID), 2, screen));

            for (StorePotion potion: potions) {
                if (!Settings.isDailyRun) {
                    potion.price = MathUtils.round(potion.price * AbstractDungeon.merchantRng.random(0.95F, 1.05F));
                }
            }
        }
    }


    @Override
    public void modifyReward(RewardItem rewardItem) {
        if (rewardItem.type == RewardItem.RewardType.POTION && rewardItem.potion != null) {
            // Store the rarity before clearing the existing potion
            AbstractPotion.PotionRarity potionRarity = rewardItem.potion.rarity;

            AbstractPotion replacementPotion;
            switch (potionRarity) {
                case COMMON:
                    replacementPotion = PotionHelper.getPotion(SwiftPotion.POTION_ID);
                    break;
                case UNCOMMON:
                    replacementPotion = PotionHelper.getPotion(GamblersBrew.POTION_ID);
                    break;
                case RARE:
                    replacementPotion = PotionHelper.getPotion(SneckoOil.POTION_ID);
                    break;
                default:
                    return; // Do nothing if the potion has an unexpected rarity
            }
            rewardItem.potion = replacementPotion;

            // Update the reward item's description to match the new potion
            rewardItem.text = replacementPotion.name;
        }
        if (rewardItem.type == RewardItem.RewardType.GOLD) {
            int additionalGold = rewardItem.goldAmt;
            rewardItem.incrementGold(additionalGold);
        }
    }

    @Override
    public void replaceRooms(Random rng) {
        //Replace all event rooms with monster rooms
        for (MapRoomNode node : this.nodes) {
            if(node.room != null && EventRoom.class.equals(node.room.getClass())) {
                node.setRoom(new MonsterRoom());
            }
        }
    }

    protected boolean allowAdditionalEntrances() {
        return false;
    }

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        return Arrays.asList(
                new ZoneEncounter(AMBUSHED_BY_RATS, 1, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new FungiBeast(-1000.0F, 0),
                                new FungiBeast(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_CULTIST_SLIME, 1, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new AcidSlime_M(-1000.0F, 0),
                                new Cultist(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_JAW_SLIME, 1, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new JawWorm(-1000.0F, 0),
                                new AcidSlime_M(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_THIEF_SLIME, 1, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new SpikeSlime_M(-1000.0F, 0),
                                new Looter(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_JAW_LOUSE, 1, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new JawWorm(-1000.0F, 0),
                                new LouseNormal(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_THIEVES, 2, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new Looter(-1000.0F, 0),
                                new Mugger(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_AVOCADO_RAT, 2, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new ShelledParasite(-1000.0F, 0),
                                new FungiBeast(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_SENTRY_BASEBALL, 2, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new Sentry(-1000.0F, 0),
                                new SphericGuardian(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_POWER_COUPLE, 2, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new Centurion(-1000.0F, 0),
                                new Healer(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_BYRD_CHOSEN, 2, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new Byrd(-1000.0F, 0),
                                new Chosen(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_CULTIST_CHOSEN, 2, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new Cultist(-1000.0F, 0),
                                new Chosen(32.0F, 0)
                        }))
        );
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        return Arrays.asList(
                new ZoneEncounter(AMBUSHED_BY_SNOOZE_RAT, 1, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new FungiBeast(-1000.0F, 0),
                                new Lagavulin(true)
                        })),
                new ZoneEncounter(AMBUSHED_BY_LOUSE_NOB, 1, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new LouseDefensive(-1000.0F, 0),
                                new GremlinNob(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_TASKMASTER_NOB, 2, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new Taskmaster(-1000.0F, 0),
                                new GremlinNob(32.0F, 0)
                        })),
                new ZoneEncounter(AMBUSHED_BY_PHILO_BOYS, 2, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new Byrd(-1000.0F, 0),
                                new BookOfStabbing()
                        }))
        );
    }
}