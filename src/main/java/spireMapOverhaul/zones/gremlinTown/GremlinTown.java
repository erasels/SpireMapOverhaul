package spireMapOverhaul.zones.gremlinTown;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWarrior;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.*;
import spireMapOverhaul.zones.gremlinTown.cards.*;
import spireMapOverhaul.zones.gremlinTown.events.misc.Chest;
import spireMapOverhaul.zones.gremlinTown.monsters.*;
import spireMapOverhaul.zones.gremlinTown.potions.*;
import spireMapOverhaul.zones.gremlinTown.relics.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;
import static com.megacrit.cardcrawl.helpers.MonsterHelper.getGremlin;
import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makePath;

public class GremlinTown extends AbstractZone
        implements EncounterModifyingZone,
        RewardModifyingZone,
        ShopModifyingZone,
        CombatModifyingZone,
        ModifiedEventRateZone {
    public static final String ID = GremlinTown.class.getSimpleName();
    public static final String GREMLIN_RIDERS = SpireAnniversary6Mod.makeID("Gremlin_Riders");
    public static final String GREMLIN_GANG_TWO = SpireAnniversary6Mod.makeID("Gremlin_Gang_Two");
    public static final String NIB = SpireAnniversary6Mod.makeID("Nib");
    public static final String GREMLIN_ELDER = SpireAnniversary6Mod.makeID("Gremlin_Elder");
    public static final String GREMLIN_HORDE = SpireAnniversary6Mod.makeID("Gremlin_Horde");
    public static final String GREMLIN_JERK = SpireAnniversary6Mod.makeID("Wheel_Gremlin");
    public static final String SURPRISE = SpireAnniversary6Mod.makeID("Surprise");
    private static ArrayList<AbstractCard> gremlinCards;
    private static ArrayList<AbstractCard> commonGremlinCards;
    private static ArrayList<AbstractCard> uncommonGremlinCards;
    private static ArrayList<AbstractCard> rareGremlinCards;
    private static ArrayList<AbstractPotion> gremlinPotions;
    private static ArrayList<AbstractPotion> commonGremlinPotions;
    private static ArrayList<AbstractPotion> uncommonGremlinPotions;
    private static ArrayList<AbstractPotion> rareGremlinPotions;
    private static ArrayList<AbstractRelic> commonGremlinRelics;
    private static ArrayList<AbstractRelic> uncommonGremlinRelics;
    private static ArrayList<AbstractRelic> rareGremlinRelics;
    private static final String SHOP_STRINGS = SpireAnniversary6Mod.makeID("ShopBanter");
    public static final String MERCHANT_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "ui/gremlinTownShop/skeleton.atlas");
    public static final String MERCHANT_SKELETON = SpireAnniversary6Mod.makeImagePath(
            "ui/gremlinTownShop/skeleton.json");
    private final ZoneEncounter GREMLIN_LEADER_ENCOUNTER = new ZoneEncounter(MonsterHelper.GREMLIN_LEADER_ENC, 2,
            () -> MonsterHelper.getEncounter("Gremlin Leader"));
    public static final String PLATFORM_KEY = makeID("GremlinTown:Platform");
    public static final String PLATFORM_OGG = makePath("audio/GremlinTown/Platform.ogg");
    private final Color zoneColor;

    public GremlinTown() {
        super(ID, Icons.MONSTER, Icons.EVENT, Icons.SHOP);
        width = 2;
        maxWidth = 3;
        height = 5;
        maxHeight = 6;
        zoneColor = Color.FIREBRICK.cpy();
        zoneColor.a = 1.0F;
    }

    @Override
    public void beforePreBattlePrep() {
        HordeHelper.needsUpdate = false;
    }

    @Override
    public AbstractZone copy() {
        return new GremlinTown();
    }

    @Override
    public Color getColor() {
        return zoneColor;
    }

    @Override
    public boolean canSpawn() {
        return isAct(2);
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
    }

    @Override
    protected boolean canIncludeFinalCampfireRow() {
        return false;
    }

    @Override
    public float zoneSpecificEventRate() {
        return 1.0f;
    }

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        return Arrays.asList(
                new ZoneEncounter(GREMLIN_RIDERS, 2, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new GremlinRiderGreen(-320.0F, -12.0F),
                                new GremlinRiderRed(-100.0F, -30.0F),
                                new GremlinRiderBlue(150.0F, 15.0F)
                        })),
                new ZoneEncounter(GREMLIN_GANG_TWO, 2, GremlinTown::getGremlinGangGroup),
                new ZoneEncounter(NIB, 2, () -> new MonsterGroup(new GremlinNib(0f, 0f)))
        );
    }

    private static MonsterGroup getGremlinGangGroup() {
        ArrayList<String> gremlinPool = new ArrayList<>();
        gremlinPool.add("ArmoredGremlin");
        gremlinPool.add("ChubbyGremlin");
        gremlinPool.add("GremlinAssassin");
        gremlinPool.add("GremlinBarbarian");
        gremlinPool.add("GremlinHealer");

        AbstractMonster[] retVal = new AbstractMonster[3];
        int index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        String key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[0] = getAdvancedGremlin(key, -430.0F, -25.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[1] = getAdvancedGremlin(key, -170.0F, 21.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[2] = getAdvancedGremlin(key, 115.0F, -30.0F);

        return new MonsterGroup(retVal);
    }

    public static AbstractMonster getAdvancedGremlin(String key, float xPos, float yPos) {
        switch (key) {
            case "GremlinAssassin":
                return new GremlinAssassin(xPos, yPos);
            case "GremlinBarbarian":
                return new GremlinBarbarian(xPos, yPos);
            case "GremlinHealer":
                return new GremlinHealer(xPos, yPos);
            case "ChubbyGremlin":
                return new ChubbyGremlin(xPos, yPos);
            case "ArmoredGremlin":
                return new ArmoredGremlin(xPos, yPos);
            default:
                return new GremlinWarrior(xPos, yPos);
        }
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        // Override that one method so the gremlin leader encounter isn't double registered
        return Arrays.asList(
                GREMLIN_LEADER_ENCOUNTER,
                new ZoneEncounter(GREMLIN_ELDER, 2, () -> new MonsterGroup(new GremlinElder(-80f, -50f))),
                new ZoneEncounter(GREMLIN_HORDE, 2, GremlinTown::getHordeStartGroup)
        );
    }

    private static MonsterGroup getHordeStartGroup() {
        ArrayList<String> gremlinPool = new ArrayList<>();
        gremlinPool.add("GremlinWarrior");
        gremlinPool.add("GremlinThief");
        gremlinPool.add("GremlinFat");
        gremlinPool.add("GremlinTsundere");

        AbstractMonster[] retVal = new AbstractMonster[4];
        int index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        String key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[0] = getGremlin(key, -320.0F, 25.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[1] = getGremlin(key, -160.0F, -12.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[2] = getGremlin(key, 25.0F, -35.0F);
        key = gremlinPool.get(0);
        gremlinPool.remove(0);
        retVal[3] = getGremlin(key, 205.0F, 40.0F);

        return new MonsterGroup(retVal);
    }

    @Override
    public void registerEncounters() {
        List<ZoneEncounter> normalEncounters = this.getNormalEncounters();
        List<ZoneEncounter> encounters = new ArrayList<>();
        if (normalEncounters != null)
            encounters.addAll(normalEncounters);

        //  Will need to manually add all Elite encounters that aren't leader
        //  Since leader is already registered in base game
        //  encounters.addAll(eliteEncounters);
        encounters.add(new ZoneEncounter(GREMLIN_ELDER, 2, () -> new MonsterGroup(new GremlinElder(-80f, -50f))));
        encounters.add(new ZoneEncounter(GREMLIN_HORDE, 2, GremlinTown::getHordeStartGroup));

        //  Also add event fights
        encounters.add(new ZoneEncounter(GREMLIN_JERK, 2, () -> new MonsterGroup(new GremlinJerk(-120f, -80f))));
        // This isn't quite the same as the monster group loaded by the event fight
        // These start in their combat positions instead of spawning off the screen and being animated
        encounters.add(new ZoneEncounter(SURPRISE, 2, () -> new MonsterGroup(
                new AbstractMonster[]{
                    new GremlinRiderRed((Settings.WIDTH * -0.22F)/Settings.scale, 40F),
                    new GremlinRiderRed((Settings.WIDTH * -0.32F)/Settings.scale, -60F),
                        new GremlinCannon((Chest.CHEST_LOC_X - Settings.WIDTH*0.75F)/Settings.scale,
                                (Chest.CHEST_LOC_Y - AbstractDungeon.floorY - 256.0F*Settings.scale)/Settings.yScale)
                })));

        for (ZoneEncounter ze : encounters)
            BaseMod.addMonster(ze.getID(), ze.getName(), () -> ze.getMonsterSupplier().get());
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        // This should be flexible with mods that modify shop layouts
        ArrayList<AbstractCard> copiedCards = new ArrayList<>(coloredCards);
        coloredCards.clear();

        AbstractCard card;
        for (AbstractCard ogCard : copiedCards) {
            AbstractCard.CardRarity rarity = ogCard.rarity;
            AbstractCard.CardType type = ogCard.type;
            card = null;

            int attempts = 0;
            boolean containsDupe = true;
            while (containsDupe) {
                containsDupe = false;
                card = getGremlinCardByRarityType(rarity, type);

                if (attempts >= 20 || card == null) {
                    card = ogCard;
                    continue;
                }
                for (AbstractCard c : coloredCards) {
                    if (c.cardID.equals(card.cardID)) {
                        containsDupe = true;
                        attempts++;
                        break;
                    }
                }
            }

            coloredCards.add(card);
        }
    }

    @Override
    public AbstractCard getReplacementShopCardForCourier(AbstractCard purchasedCard) {
        if (!purchasedCard.hasTag(SpireAnniversary6Mod.Enums.GREMLIN))
            return null;

        ShopScreen screen = AbstractDungeon.shopScreen;
        if (screen == null)
            return null;

        AbstractCard.CardRarity rarity = rollRarity();
        switch (rarity) {
            case COMMON:
                cardBlizzRandomizer -= cardBlizzGrowth;
                if (cardBlizzRandomizer <= cardBlizzMaxOffset) {
                    cardBlizzRandomizer = cardBlizzMaxOffset;
                }
            case UNCOMMON:
                break;
            case RARE:
                cardBlizzRandomizer = cardBlizzStartOffset;
                break;
            default:
                return null;
        }
        AbstractCard.CardType type = purchasedCard.type;
        if (type == AbstractCard.CardType.POWER && rarity == AbstractCard.CardRarity.COMMON)
            rarity = AbstractCard.CardRarity.UNCOMMON;

        AbstractCard card = null;
        int attempts = 0;
        boolean containsDupe = true;
        while (containsDupe) {
            containsDupe = false;
            card = getGremlinCardByRarityType(rarity, type);

            if (attempts >= 20 || card == null)
                return null;

            for (AbstractCard c : screen.coloredCards) {
                if (c.cardID.equals(card.cardID)) {
                    containsDupe = true;
                    attempts++;
                    break;
                }
            }
        }
        return card;
    }

    @Override
    public void postCreateShopRelics(ShopScreen screen, ArrayList<StoreRelic> relics) {
        for (StoreRelic r : relics) {
            int slot = ReflectionHacks.getPrivate(r, StoreRelic.class, "slot");
            // Putting a slot limit so Spicy Shops doesn't single-handedly empty my relic pools
            // Save some for elites/events
            if (r.relic.tier != AbstractRelic.RelicTier.SHOP && slot < 3)
                r.relic = getRandomGRelic(r.relic.tier);
        }
    }

    @Override
    public void postCreateShopPotions(ShopScreen screen, ArrayList<StorePotion> potions) {
        for (StorePotion p : potions)
            p.potion = getRandomShopGPotion();
    }

    @Override
    public void postAddIdleMessages(ArrayList<String> idleMessages) {
        idleMessages.clear();
        UIStrings talkyStrings = CardCrawlGame.languagePack.getUIString(SHOP_STRINGS);
        idleMessages.addAll(Arrays.asList(talkyStrings.TEXT));
    }

    private ArrayList<AbstractCard> getGremlinCardReward(ArrayList<AbstractCard> cards) {
        ArrayList<AbstractCard> retVal = new ArrayList<>();

        AbstractCard card;
        for (AbstractCard ogCard : cards) {
            AbstractCard.CardRarity rarity = ogCard.rarity;
            card = null;

            boolean containsDupe = true;
            int attempts = 0;
            while (containsDupe) {
                containsDupe = false;
                card = getGremlinCardByRarity(rarity);
                if (attempts == 20 || card == null) {
                    retVal.add(ogCard);
                    break;
                }

                for (AbstractCard c : retVal) {
                    if (c.cardID.equals(card.cardID)) {
                        containsDupe = true;
                        attempts++;
                        break;
                    }
                }
            }

            retVal.add(card);
        }

        applyStandardUpgradeLogic(retVal);

        return retVal;
    }

    @Override
    public void modifyRewards(ArrayList<RewardItem> rewards) {
        for (RewardItem item : rewards) {
            if (item.type == RewardItem.RewardType.POTION) {
                item.potion = getRandomWeightedPotion();
                item.text = item.potion.name;
            }

            if (item.type == RewardItem.RewardType.RELIC) {
                item.relic = getRandomGRelic();
                item.text = item.relic.name;
            }

            if (item.type == RewardItem.RewardType.CARD)
                item.cards = getGremlinCardReward(item.cards);

            if (item.type == RewardItem.RewardType.GOLD && lastCombatMetricKey != null
                    && lastCombatMetricKey.equals(GREMLIN_HORDE))
                item.bonusGold = treasureRng.random(150, 200);
        }

        RewardItem item = new RewardItem();
        item.cards = getGremlinCardReward(item.cards);
        rewards.add(item);

        if (lastCombatMetricKey != null && lastCombatMetricKey.equals(NIB))
            rewards.add(new RewardItem(getRandomGRelic()));

        if (lastCombatMetricKey != null && lastCombatMetricKey.equals(GREMLIN_HORDE))
            rewards.add(new RewardItem(getRandomGRelic()));
    }

    private static AbstractCard getRandomCommonGremlin() {
        if (gremlinCards == null)
            initGremlinCards();
        
        return Wiz.getRandomItem(commonGremlinCards, cardRng).makeCopy();
    }

    private static AbstractCard getRandomCommonGremlin(AbstractCard.CardType type) {
        if (type == null)
            return getRandomCommonGremlin();

        if (gremlinCards == null)
            initGremlinCards();

        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : commonGremlinCards)
            if (c.type == type)
                list.add(c);

        return Wiz.getRandomItem(list, cardRng).makeCopy();
    }

    private static AbstractCard getRandomUncommonGremlin() {
        if (gremlinCards == null)
            initGremlinCards();

        return Wiz.getRandomItem(uncommonGremlinCards, cardRng).makeCopy();
    }

    private static AbstractCard getRandomUncommonGremlin(AbstractCard.CardType type) {
        if (type == null)
            return getRandomUncommonGremlin();

        if (gremlinCards == null)
            initGremlinCards();

        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : uncommonGremlinCards)
            if (c.type == type)
                list.add(c);

        return Wiz.getRandomItem(list, cardRng).makeCopy();
    }

    private static AbstractCard getRandomRareGremlin() {
        if (gremlinCards == null)
            initGremlinCards();

        return Wiz.getRandomItem(rareGremlinCards, cardRng).makeCopy();
    }

    private static AbstractCard getRandomRareGremlin(AbstractCard.CardType type) {
        if (type == null)
            return getRandomRareGremlin();

        if (gremlinCards == null)
            initGremlinCards();

        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : rareGremlinCards)
            if (c.type == type)
                list.add(c);

        return Wiz.getRandomItem(list, cardRng).makeCopy();
    }

    private static AbstractCard getGremlinCardByRarity(AbstractCard.CardRarity rarity) {
        if (rarity == AbstractCard.CardRarity.COMMON)
            return getRandomCommonGremlin();
        else if (rarity == AbstractCard.CardRarity.UNCOMMON)
            return getRandomUncommonGremlin();
        else if (rarity == AbstractCard.CardRarity.RARE)
            return getRandomRareGremlin();
        else return null;
    }

    private static AbstractCard getGremlinCardByRarityType(AbstractCard.CardRarity rarity, AbstractCard.CardType type) {
        if (type == null)
            return getGremlinCardByRarity(rarity);
        if (type == AbstractCard.CardType.POWER && rarity == AbstractCard.CardRarity.COMMON)
            rarity = AbstractCard.CardRarity.UNCOMMON;
        if (rarity == AbstractCard.CardRarity.COMMON)
            return getRandomCommonGremlin(type);
        else if (rarity == AbstractCard.CardRarity.UNCOMMON)
            return getRandomUncommonGremlin(type);
        else if (rarity == AbstractCard.CardRarity.RARE)
            return getRandomRareGremlin(type);
        else return null;
    }

    // For use spawning temp cards in combat
    public static AbstractCard getRandomGremlinInCombat() {
        if (gremlinCards == null)
            initGremlinCards();

        return Wiz.getRandomItem(gremlinCards, cardRandomRng).makeCopy();
    }

    private static AbstractPotion getRandomCommonGPotion() {
        if (commonGremlinPotions == null)
            initGremlinPotions();

        return Wiz.getRandomItem(commonGremlinPotions, potionRng).makeCopy();
    }

    private static AbstractPotion getRandomUncommonGPotion() {
        if (uncommonGremlinPotions == null)
            initGremlinPotions();

        return Wiz.getRandomItem(uncommonGremlinPotions, potionRng).makeCopy();
    }

    private static AbstractPotion getRandomRareGPotion() {
        if (rareGremlinPotions == null)
            initGremlinPotions();

        return Wiz.getRandomItem(rareGremlinPotions, potionRng).makeCopy();
    }

    private static AbstractPotion getRandomWeightedPotion() {
        int x = AbstractDungeon.potionRng.random(0, 99);

        if (x < PotionHelper.POTION_COMMON_CHANCE)
            return getRandomCommonGPotion();
        else if (x < PotionHelper.POTION_COMMON_CHANCE + PotionHelper.POTION_UNCOMMON_CHANCE)
            return getRandomUncommonGPotion();
        else
            return getRandomRareGPotion();
    }

    private static AbstractPotion getRandomShopGPotion() {
        if (gremlinPotions == null)
            initGremlinPotions();

        AbstractPotion potion = Wiz.getRandomItem(gremlinPotions, potionRng).makeCopy();
        // This is a hell of a potion.  Shouldn't be quite so easy to get
        if (potion.ID.equals(RitualBlood.POTION_ID) && potionRng.random(0, 1) == 0)
            potion = gremlinPotions.get(1).makeCopy();

        return potion;
    }

    public static AbstractRelic getRandomGRelic() {
        if (commonGremlinRelics == null)
            initGremlinRelics();

        AbstractRelic.RelicTier tier = returnRandomRelicTier();
        return getRandomGRelic(tier);
    }

    private static AbstractRelic getRandomGRelic(AbstractRelic.RelicTier tier) {
        if (commonGremlinRelics == null)
            initGremlinRelics();

        AbstractRelic r = null;
        if (tier == AbstractRelic.RelicTier.COMMON) {
            if (commonGremlinRelics.size() > 0) {
                r = commonGremlinRelics.get(0).makeCopy();
                commonGremlinRelics.remove(0);
            }
        }
        else if (tier == AbstractRelic.RelicTier.UNCOMMON) {
            if (uncommonGremlinRelics.size() > 0) {
                r = uncommonGremlinRelics.get(0).makeCopy();
                uncommonGremlinRelics.remove(0);
            }
        }
        else {
            if (rareGremlinRelics.size() > 0) {
                r = rareGremlinRelics.get(0).makeCopy();
                rareGremlinRelics.remove(0);
            }
        }

        if (r == null)
            return AbstractDungeon.returnRandomRelic(tier);

        return r;
    }

    private static void initGremlinCards() {
        commonGremlinCards = new ArrayList<>();
        commonGremlinCards.add(new SneakAttack());
        commonGremlinCards.add(new AngryStrike());
        commonGremlinCards.add(new Charge());
        commonGremlinCards.add(new MeatMallet());
        commonGremlinCards.add(new Shield());
        commonGremlinCards.add(new ThrowRock());

        uncommonGremlinCards = new ArrayList<>();
        uncommonGremlinCards.add(new Assassinate());
        uncommonGremlinCards.add(new Cure());
        uncommonGremlinCards.add(new Frenzy());
        uncommonGremlinCards.add(new FullPlate());
        uncommonGremlinCards.add(new MountedCombat());
        uncommonGremlinCards.add(new Sit());
        uncommonGremlinCards.add(new Rig());
        uncommonGremlinCards.add(new PoisonSpray());

        rareGremlinCards = new ArrayList<>();
        rareGremlinCards.add(new Flurry());
        rareGremlinCards.add(new RRrroohrrRGHHhhh());
        rareGremlinCards.add(new OmegaCurse());
        rareGremlinCards.add(new CriticalHit());
        rareGremlinCards.add(new TheHorde());
        rareGremlinCards.add(new Inspire());

        gremlinCards = new ArrayList<>();
        gremlinCards.addAll(commonGremlinCards);
        gremlinCards.addAll(uncommonGremlinCards);
        gremlinCards.addAll(rareGremlinCards);
    }

    private static void initGremlinPotions() {
        // All zone rarity, but they're distributed as if they have common/uncommon/rare
        commonGremlinPotions = new ArrayList<>();
        commonGremlinPotions.add(new LouseMilk());
        commonGremlinPotions.add(new PreerelxsBlueRibbon());
        commonGremlinPotions.add(new MushroomSoup());

        uncommonGremlinPotions = new ArrayList<>();
        uncommonGremlinPotions.add(new GremsFire());
        uncommonGremlinPotions.add(new NoxiousBrew());

        rareGremlinPotions = new ArrayList<>();
        rareGremlinPotions.add(new RitualBlood());

        gremlinPotions = new ArrayList<>();
        gremlinPotions.addAll(commonGremlinPotions);
        gremlinPotions.addAll(uncommonGremlinPotions);
        gremlinPotions.addAll(rareGremlinPotions);
    }

    public static void initGremlinRelics() {
        // All special rarity, but they're distributed as if they have common/uncommon/rare
        commonGremlinRelics = new ArrayList<>();
        commonGremlinRelics.add(new GremlinClaw());
        commonGremlinRelics.add(new NobClub());
        commonGremlinRelics.add(new Bowtie());
        commonGremlinRelics.add(new Lockpicks());
        Collections.shuffle(commonGremlinRelics, relicRng.random);

        uncommonGremlinRelics = new ArrayList<>();
        uncommonGremlinRelics.add(new GremlinHood());
        uncommonGremlinRelics.add(new Duelity());
        uncommonGremlinRelics.add(new LousePlushie());
        Collections.shuffle(uncommonGremlinRelics, relicRng.random);

        rareGremlinRelics = new ArrayList<>();
        rareGremlinRelics.add(new WarHorn());
        rareGremlinRelics.add(new GremlinStaff());
        Collections.shuffle(rareGremlinRelics, relicRng.random);
    }
}