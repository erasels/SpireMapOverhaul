package spireMapOverhaul.zones.gremlinTown;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;
import spireMapOverhaul.zones.gremlinTown.cards.*;
import spireMapOverhaul.zones.gremlinTown.monsters.*;
import spireMapOverhaul.zones.gremlinTown.potions.*;
import spireMapOverhaul.zones.gremlinTown.relics.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.asc;

public class GremlinTown extends AbstractZone
        implements EncounterModifyingZone,
        RewardModifyingZone,
        ShopModifyingZone {
    public static final String ID = GremlinTown.class.getSimpleName();
    public static final String GREMLIN_RIDERS = SpireAnniversary6Mod.makeID("Gremlin_Riders");
    public static final String GREMLIN_GANG_TWO = SpireAnniversary6Mod.makeID("Gremlin_Gang_Two");
    public static final String NIB = SpireAnniversary6Mod.makeID("Nib");
    public static final String GREMLIN_ELDER = SpireAnniversary6Mod.makeID("Gremlin_Elder");
    private static ArrayList<AbstractCard> gremlinCards;
    private static ArrayList<AbstractCard> commonGremlinCards;
    private static ArrayList<AbstractCard> uncommonGremlinCards;
    private static ArrayList<AbstractCard> rareGremlinCards;
    private static ArrayList<String> gremlinCardIds;
    private static ArrayList<AbstractPotion> commonGremlinPotions;
    private static ArrayList<AbstractPotion> uncommonGremlinPotions;
    private static ArrayList<AbstractPotion> rareGremlinPotions;
    private static ArrayList<AbstractRelic> commonGremlinRelics;
    private static ArrayList<AbstractRelic> uncommonGremlinRelics;
    private static ArrayList<AbstractRelic> rareGremlinRelics;

    private final ZoneEncounter GREMLIN_LEADER_ENCOUNTER = new ZoneEncounter(MonsterHelper.GREMLIN_LEADER_ENC, 2,
            () -> MonsterHelper.getEncounter("Gremlin Leader"));

    public GremlinTown() {
        super(ID, Icons.MONSTER, Icons.ELITE, Icons.EVENT);
        width = 4;
        height = 6;
    }

    @Override
    public AbstractZone copy() {
        return new GremlinTown();
    }

    @Override
    public Color getColor() {
        return Color.FIREBRICK.cpy();
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
    public List<ZoneEncounter> getNormalEncounters() {
        // This example has new custom enemies in act 1, remixes of base game enemies in act 2, and isn't valid in act 3
        // (Ignore the lack of any thematic connection, it's just an example)
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

        AbstractMonster[] retVal = new AbstractMonster[4];
        int index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        String key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[0] = getGremlin(key, -530.0F, -25.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[1] = getGremlin(key, -270.0F, 21.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[2] = getGremlin(key, 15.0F, -30.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[3] = getGremlin(key, 245.0F, 30.0F);

        return new MonsterGroup(retVal);
    }

    public static AbstractMonster getGremlin(String key, float xPos, float yPos) {
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
                return new AcidSlime_S(xPos, yPos, 0);
        }
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        // Override that one method so the gremlin leader encounter isn't double registered
        return Arrays.asList(
                GREMLIN_LEADER_ENCOUNTER,
                new ZoneEncounter(GREMLIN_ELDER, 2, () -> new MonsterGroup(new GremlinElder(0f, 0f)))
        );
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
        encounters.add(new ZoneEncounter(GREMLIN_ELDER, 2, () -> new MonsterGroup(new GremlinElder(0f, 0f))));

        for (ZoneEncounter ze : encounters)
            BaseMod.addMonster(ze.getID(), ze.getName(), () -> ze.getMonsterSupplier().get());
    }

    public ArrayList<AbstractCard> getAdditionalCardReward() {
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        int numCards = 3;

        for (AbstractRelic r : adp().relics)
            numCards = r.changeNumberOfCardsInReward(numCards);

        if (ModHelper.isModEnabled("Binary"))
            --numCards;

        float cardUpgradedChance = 0.25f;
        if (asc() >= 12)
            cardUpgradedChance = 0.125f;

        AbstractCard card;
        for(int i = 0; i < numCards; ++i) {
            AbstractCard.CardRarity rarity = rollRarity();
            card = null;
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
            }

            boolean containsDupe = true;
            while(containsDupe) {
                containsDupe = false;
                card = getGremlinCardByRarity(rarity);

                for (AbstractCard c : retVal) {
                    assert card != null;
                    if (c.cardID.equals(card.cardID)) {
                        containsDupe = true;
                        break;
                    }
                }
            }

            if (card != null)
                retVal.add(card);
        }

        for (AbstractCard c : retVal) {
            if (c.rarity != AbstractCard.CardRarity.RARE && cardRng.randomBoolean(cardUpgradedChance) && c.canUpgrade())
                c.upgrade();
            else
                for (AbstractRelic r : adp().relics)
                    r.onPreviewObtainCard(c);
        }

        return retVal;
    }

    @Override
    public RewardItem getAdditionalReward() {
        if (AbstractDungeon.lastCombatMetricKey.equals(NIB)) {
            int x = AbstractDungeon.potionRng.random(0, 99);

            if (x < PotionHelper.POTION_COMMON_CHANCE)
                return new RewardItem(getRandomCommonGPotion());
            else if (x < PotionHelper.POTION_COMMON_CHANCE + PotionHelper.POTION_UNCOMMON_CHANCE)
                return new RewardItem(getRandomUncommonGPotion());
            else
                return new RewardItem(getRandomRareGPotion());
        }

        if (AbstractDungeon.currMapNode.room instanceof MonsterRoomElite)
            return new RewardItem(getRandomGRelic());

        return null;
    }

    private static AbstractCard getRandomCommonGremlin() {
        if (gremlinCards == null)
            initGremlinCards();

        Collections.shuffle(commonGremlinCards);
        return commonGremlinCards.get(0).makeCopy();
    }

    private static AbstractCard getRandomUncommonGremlin() {
        if (gremlinCards == null)
            initGremlinCards();

        Collections.shuffle(uncommonGremlinCards);
        return uncommonGremlinCards.get(0).makeCopy();
    }

    private static AbstractCard getRandomRareGremlin() {
        if (gremlinCards == null)
            initGremlinCards();

        Collections.shuffle(rareGremlinCards);
        return rareGremlinCards.get(0).makeCopy();
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

    public static AbstractCard getRandomGremlin() {
        if (gremlinCards == null)
            initGremlinCards();

        Collections.shuffle(gremlinCards);
        return gremlinCards.get(0).makeCopy();
    }

    private static AbstractPotion getRandomCommonGPotion() {
        if (commonGremlinPotions == null)
            initGremlinPotions();

        Collections.shuffle(commonGremlinPotions);
        return commonGremlinPotions.get(0).makeCopy();
    }

    private static AbstractPotion getRandomUncommonGPotion() {
        if (uncommonGremlinPotions == null)
            initGremlinPotions();

        Collections.shuffle(uncommonGremlinPotions);
        return uncommonGremlinPotions.get(0).makeCopy();
    }

    private static AbstractPotion getRandomRareGPotion() {
        if (rareGremlinPotions == null)
            initGremlinPotions();

        Collections.shuffle(rareGremlinPotions);
        return rareGremlinPotions.get(0).makeCopy();
    }

    private static AbstractRelic getRandomGRelic() {
        if (commonGremlinRelics == null)
            initGremlinRelics();

        AbstractRelic r = null;
        AbstractRelic.RelicTier tier = returnRandomRelicTier();
        if (tier == AbstractRelic.RelicTier.COMMON) {
            if (commonGremlinRelics.size() > 0) {
                r = commonGremlinRelics.get(0);
                commonGremlinRelics.remove(0);
            }
        }
        else if (tier == AbstractRelic.RelicTier.UNCOMMON) {
            if (uncommonGremlinRelics.size() > 0) {
                r = uncommonGremlinRelics.get(0);
                uncommonGremlinRelics.remove(0);
            }
        }
        else {
            if (rareGremlinRelics.size() > 0) {
                r = rareGremlinRelics.get(0);
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

        uncommonGremlinCards = new ArrayList<>();
        uncommonGremlinCards.add(new Assassinate());
        uncommonGremlinCards.add(new Cure());
        uncommonGremlinCards.add(new Frenzy());
        uncommonGremlinCards.add(new FullPlate());
        uncommonGremlinCards.add(new MountedCombat());
        uncommonGremlinCards.add(new Sit());

        rareGremlinCards = new ArrayList<>();
        rareGremlinCards.add(new Flurry());
        rareGremlinCards.add(new RRrroohrrRGHHhhh());
        rareGremlinCards.add(new OmegaCurse());
        rareGremlinCards.add(new CriticalHit());

        gremlinCards = new ArrayList<>();
        gremlinCards.addAll(commonGremlinCards);
        gremlinCards.addAll(uncommonGremlinCards);
        gremlinCards.addAll(rareGremlinCards);

        gremlinCardIds = new ArrayList<>();
        for (AbstractCard c : gremlinCards)
            gremlinCardIds.add(c.cardID);
    }

    public static ArrayList<String> getGremlinCardIds() {
        if (gremlinCards == null)
            initGremlinCards();
        return gremlinCardIds;
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
    }

    private static void initGremlinRelics() {
        // All special rarity, but they're distributed as if they have common/uncommon/rare
        commonGremlinRelics = new ArrayList<>();
        commonGremlinRelics.add(new GremlinClaw());
        commonGremlinRelics.add(new GremlinClub());
        Collections.shuffle(commonGremlinRelics);

        uncommonGremlinRelics = new ArrayList<>();
        uncommonGremlinRelics.add(new LargeScarf());
        uncommonGremlinRelics.add(new Duelity());
        Collections.shuffle(uncommonGremlinRelics);

        rareGremlinRelics = new ArrayList<>();
        rareGremlinRelics.add(new WarHorn());
        rareGremlinRelics.add(new GremlinStaff());
        Collections.shuffle(rareGremlinRelics);
    }
}
