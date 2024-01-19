package spireMapOverhaul.zones.packattack;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import java.util.*;
import java.util.stream.Collectors;

public class PackAttackZone extends AbstractZone implements CombatModifyingZone, RewardModifyingZone, ShopModifyingZone, ModifiedEventRateZone {
    public static final String ID = "PackAttack";

    public static Class<?> anniv5;
    public static Class<?> abstractCardPack;

    private ArrayList<AbstractCard> cards;
    private ArrayList<AbstractCard> commonPool;
    private ArrayList<AbstractCard> uncommonPool;
    private ArrayList<AbstractCard> rarePool;

    private ArrayList<String> packNames;

    public PackAttackZone() {
        super(ID, Icons.MONSTER, Icons.EVENT, Icons.SHOP);
        this.width = 2;
        this.maxHeight = 4;
        this.height = 3;
        this.maxHeight = 4;

        if (Loader.isModLoaded("anniv5")) {
            try {
                anniv5 = Class.forName("thePackmaster.SpireAnniversary5Mod");
                abstractCardPack = Class.forName("thePackmaster.packs.AbstractCardPack");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Error retrieving classes from Packmaster", e);
            }
        }
    }

    public ArrayList<AbstractCard> getCards() {
        return cards;
    }

    @Override
    public AbstractZone copy() {
        return new PackAttackZone();
    }

    @Override
    public Color getColor() {
        return new Color(0.76f, 0.65f, 0.52f, 1);
    }

    @Override
    public String getDescriptionText() {
        if (this.packNames == null) {
            return TEXT[1];
        }

        List<String> packNameStrings = this.packNames.stream().map(name -> FontHelper.colorString(name, "b")).collect(Collectors.toList());
        return TEXT[3].replace("{0}", packNameStrings.get(0)).replace("{1}", packNameStrings.get(1)).replace("{2}", packNameStrings.get(2));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canSpawn() {
        return anniv5 != null && abstractCardPack != null && ((ArrayList<Object>)ReflectionHacks.getPrivateStatic(anniv5, "allPacks")).size() >= 3;
    }

    @Override
    public void distributeRooms(Random rng, ArrayList<AbstractRoom> roomList) {
        //Guarantee at least one event
        placeRoomRandomly(rng, roomOrDefault(roomList, (room)->room instanceof EventRoom, EventRoom::new));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void mapGenDone(ArrayList<ArrayList<MapRoomNode>> map) {
        ArrayList<Object> allPacks = new ArrayList<>(ReflectionHacks.getPrivateStatic(anniv5, "allPacks"));
        Collections.shuffle(allPacks, new java.util.Random(AbstractDungeon.mapRng.randomLong()));
        ArrayList<Object> packs = allPacks.stream().limit(3).collect(Collectors.toCollection(ArrayList::new));
        this.packNames = new ArrayList<>();
        this.cards = new ArrayList<>();
        for (Object pack : packs) {
            try {
                this.packNames.add((String)ReflectionHacks.getCachedField(abstractCardPack, "name").get(pack));
                this.cards.addAll((ArrayList<AbstractCard>)ReflectionHacks.getCachedField(abstractCardPack, "cards").get(pack));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Error determining packs for Pack zone", e);
            }
        }
        List<AbstractCard.CardRarity> validRarities = Arrays.asList(AbstractCard.CardRarity.COMMON, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardRarity.RARE);
        this.cards.removeIf(c -> !validRarities.contains(c.rarity));
        this.commonPool = this.cards.stream().filter(c -> c.rarity == AbstractCard.CardRarity.COMMON).collect(Collectors.toCollection(ArrayList::new));
        this.uncommonPool = this.cards.stream().filter(c -> c.rarity == AbstractCard.CardRarity.UNCOMMON).collect(Collectors.toCollection(ArrayList::new));
        this.rarePool = this.cards.stream().filter(c -> c.rarity == AbstractCard.CardRarity.RARE).collect(Collectors.toCollection(ArrayList::new));
        this.updateDescription();
    }

    @Override
    public float zoneSpecificEventRate() {
        return 1;
    }

    @Override
    public void atPreBattle() {
        AbstractDungeon.player.gameHandSize -= 1;
    }

    @Override
    public void atTurnStart() {
        List<AbstractCard> validCards = this.cards.stream().filter(card -> !card.hasTag(AbstractCard.CardTags.HEALING)).collect(Collectors.toList());
        AbstractCard c = validCards.get(AbstractDungeon.cardRandomRng.random(validCards.size() - 1)).makeCopy();
        Wiz.atb(new MakeTempCardInHandAction(c, 1));
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        this.replaceWithPackCards(cards, false, AbstractDungeon.cardRng);
        this.applyStandardUpgradeLogic(cards);
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        this.replaceWithPackCards(coloredCards, true, AbstractDungeon.merchantRng);
        for (AbstractCard c : coloredCards) {
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                relic.onPreviewObtainCard(c);
            }
        }
    }

    @Override
    public AbstractCard getReplacementShopCardForCourier(AbstractCard purchasedCard) {
        if (this.cards.stream().anyMatch(c -> c.cardID.equals(purchasedCard.cardID))) {
            return this.getPackCard(AbstractDungeon.rollRarity(), purchasedCard.type, new HashSet<>(), AbstractDungeon.merchantRng);
        }
        return null;
    }

    private void replaceWithPackCards(ArrayList<AbstractCard> cards, boolean sameCardType, Random rng) {
        HashSet<String> cardsSoFar = new HashSet<>();
        for (int i = 0; i < cards.size(); i++) {
            AbstractCard card = this.getPackCard(cards.get(i).rarity, sameCardType ? cards.get(i).type : null, cardsSoFar, rng);
            cards.set(i, card);
            cardsSoFar.add(card.cardID);
        }
    }

    private AbstractCard getPackCard(AbstractCard.CardRarity rarity, AbstractCard.CardType type, HashSet<String> excludedCards, Random rng) {
        if (rarity == AbstractCard.CardRarity.COMMON && type == AbstractCard.CardType.POWER) {
            rarity = AbstractCard.CardRarity.UNCOMMON;
        }

        ArrayList<AbstractCard> options;
        switch(rarity) {
            case UNCOMMON:
                options = this.uncommonPool;
                break;
            case RARE:
                options = this.rarePool;
                break;
            default:
                options = this.commonPool;
                break;
        }

        ArrayList<AbstractCard> filteredOptions = options.stream().filter(c -> !excludedCards.contains(c.cardID)).collect(Collectors.toCollection(ArrayList::new));
        if (filteredOptions.size() > 0) {
            options = filteredOptions;
        }
        if (type != null) {
            ArrayList<AbstractCard> filteredOptions2 = options.stream().filter(c -> c.type == type).collect(Collectors.toCollection(ArrayList::new));
            if (filteredOptions2.size() > 0) {
                options = filteredOptions2;
            }
        }
        return options.get(rng.random(options.size() - 1)).makeCopy();
    }
}
