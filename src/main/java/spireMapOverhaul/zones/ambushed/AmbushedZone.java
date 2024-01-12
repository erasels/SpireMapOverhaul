package spireMapOverhaul.zones.ambushed;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import java.util.*;
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
    private static final String AMBUSHED_BY_SENTRIES = makeID("AMBUSHED_BY_SENTRIES");
    private static final String AMBUSHED_BY_LOUSE_NOB = makeID("AMBUSHED_BY_LOUSE_NOB");
    private static final String AMBUSHED_BY_TASKMASTER_NOB = makeID("AMBUSHED_BY_TASKMASTER_NOB");
    private static final String AMBUSHED_BY_SLAVERS = makeID("AMBUSHED_BY_SLAVERS");
    private static final String AMBUSHED_BY_CULTIST_SLIME = makeID("AMBUSHED_BY_CULTIST_SLIME");

    private ArrayList<String> cardDrawCardIDs; // List of card IDs that offer immediate card draw

    public AmbushedZone() {
        super(ID, Icons.MONSTER, Icons.SHOP);
        this.width = 3;
        this.height = 4;
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
        //Since this zone always has an elite, we don't want it to show up in the rows of the act that never have elites
        //It also has normal fights that are tuned as hard pool fights, so that's another reason to prevent early spawns
        return false;
    }

    private void initCardDrawCardIDs() {
        cardDrawCardIDs = new ArrayList<>();
        cardDrawCardIDs.add("Pommel Strike");
        cardDrawCardIDs.add("Shrug It Off");
        cardDrawCardIDs.add("Battle Trance");
        cardDrawCardIDs.add("Burning Pact");
        cardDrawCardIDs.add("Offering");
        cardDrawCardIDs.add("Acrobatics");
        cardDrawCardIDs.add("Backflip");
        cardDrawCardIDs.add("Dagger Throw");
        cardDrawCardIDs.add("Prepared");
        cardDrawCardIDs.add("Quick Slash");
        cardDrawCardIDs.add("Calculated Gamble");
        cardDrawCardIDs.add("Escape Plan");
        cardDrawCardIDs.add("Expertise");
        cardDrawCardIDs.add("Adrenaline");
        cardDrawCardIDs.add("Coolheaded");
        cardDrawCardIDs.add("Sweeping Beam");
        cardDrawCardIDs.add("Steam Power");
        cardDrawCardIDs.add("Skim");
        cardDrawCardIDs.add("Reboot");
        cardDrawCardIDs.add("CutThroughFate");
        cardDrawCardIDs.add("EmptyMind");
        cardDrawCardIDs.add("InnerPeace");
        cardDrawCardIDs.add("Sanctity");
        cardDrawCardIDs.add("WheelKick");
        cardDrawCardIDs.add("Scrawl");
        cardDrawCardIDs.add("Deep Breath");
        cardDrawCardIDs.add("Finesse");
        cardDrawCardIDs.add("Flash of Steel");
        cardDrawCardIDs.add("Impatience");
        cardDrawCardIDs.add("Master of Strategy");
        cardDrawCardIDs.add("Thinking Ahead");
        cardDrawCardIDs.add("Violence");
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
        // Filter the cardDrawCardIDs list by the specified rarity
        List<String> filteredCardIDs = cardDrawCardIDs.stream()
                .filter(id -> isCardOfRarity(id, rarity))
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
    public void atPreBattle() {
        AbstractDungeon.player.movePosition((float) Settings.WIDTH / 2.0F, AbstractDungeon.floorY);
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
    public void postCreateShopPotions(ShopScreen screen, ArrayList<StorePotion> potions) {
        for (StorePotion storePotion : potions) {
            AbstractPotion replacementPotion;
            switch (storePotion.potion.rarity) {
                case COMMON:
                    replacementPotion = PotionHelper.getPotion(SwiftPotion.POTION_ID);
                    break;
                case UNCOMMON:
                    replacementPotion = PotionHelper.getPotion(GamblersBrew.POTION_ID);
                    break;
                case RARE:
                    // Randomly select between Smoke Bomb and Snecko Oil for Rare potions
                    if (AbstractDungeon.potionRng.randomBoolean()) {
                        replacementPotion = PotionHelper.getPotion(SmokeBomb.POTION_ID);
                    } else {
                        replacementPotion = PotionHelper.getPotion(SneckoOil.POTION_ID);
                    }
                    break;
                default:
                    continue; // Skip if the potion has an unexpected rarity
            }
            storePotion.potion = replacementPotion;
        }
    }

    @Override
    public void modifyReward(RewardItem rewardItem) {
        if (rewardItem.type == RewardItem.RewardType.POTION && rewardItem.potion != null) {
            // Store the rarity before clearing the existing potion
            AbstractPotion.PotionRarity potionRarity = rewardItem.potion.rarity;

            // Clearing the existing potion
            rewardItem.potion = null;

            AbstractPotion replacementPotion;
            switch (potionRarity) {
                case COMMON:
                    replacementPotion = PotionHelper.getPotion(SwiftPotion.POTION_ID);
                    break;
                case UNCOMMON:
                    replacementPotion = PotionHelper.getPotion(GamblersBrew.POTION_ID);
                    break;
                case RARE:
                    // Randomly select between Smoke Bomb and Snecko Oil for Rare potions
                    if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                        replacementPotion = PotionHelper.getPotion(SmokeBomb.POTION_ID);
                    } else {
                        replacementPotion = PotionHelper.getPotion(SneckoOil.POTION_ID);
                    }
                    break;
                default:
                    return; // Do nothing if the potion has an unexpected rarity
            }
            rewardItem.potion = replacementPotion;

            // Update the reward item's description to match the new potion
            rewardItem.text = replacementPotion.name;
        }
    }

    @Override
    public void replaceRooms(Random rng) {
        replaceRoomsRandomly(rng, MonsterRoom::new, (room)->room instanceof EventRoom, 100);
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
                new ZoneEncounter(AMBUSHED_BY_SENTRIES, 1, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new Sentry(-1000.0F, 0),
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
                new ZoneEncounter(AMBUSHED_BY_SLAVERS, 2, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new SlaverBlue(-1000.0F, 0),
                                new SlaverRed(32.0F, 0)
                        }))
        );
    }
}