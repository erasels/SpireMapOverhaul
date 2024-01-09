package spireMapOverhaul.zones.packmaster;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PackmasterZone extends AbstractZone implements CombatModifyingZone, RewardModifyingZone, ShopModifyingZone, ModifiedEventRateZone {
    public static final String ID = "Packmaster";

    public static Class<?> anniv5;
    public static Class<?> abstractCardPack;

    private ArrayList<AbstractCard> cards;
    private ArrayList<AbstractCard> commonPool;
    private ArrayList<AbstractCard> uncommonPool;
    private ArrayList<AbstractCard> rarePool;

    private ArrayList<String> packNames;

    public PackmasterZone() {
        super(ID, Icons.MONSTER, Icons.EVENT, Icons.SHOP);
        this.width = 3;
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
        return new PackmasterZone();
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
        return TEXT[2].replace("{0}", packNameStrings.get(0)).replace("{1}", packNameStrings.get(1)).replace("{2}", packNameStrings.get(2));
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
        AbstractCard c = this.cards.get(AbstractDungeon.cardRandomRng.random(this.cards.size() - 1)).makeCopy();
        Wiz.atb(new MakeTempCardInHandAction(c, 1));
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        replaceWithPackCards(cards, AbstractDungeon.cardRng);
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        replaceWithPackCards(coloredCards, AbstractDungeon.merchantRng);
    }

    private void replaceWithPackCards(ArrayList<AbstractCard> cards, Random rng) {
        List<AbstractCard.CardRarity> validRarities = Arrays.asList(AbstractCard.CardRarity.COMMON, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardRarity.RARE);
        for (int i = 0; i < cards.size(); i++) {
            AbstractCard.CardRarity rarity = cards.get(i).rarity;
            if (!validRarities.contains(rarity)) {
                rarity = AbstractCard.CardRarity.COMMON;
            }
            cards.set(i, this.getPackCard(rarity, rng));
        }
    }

    private AbstractCard getPackCard(AbstractCard.CardRarity rarity, Random rng) {
        ArrayList<AbstractCard> options;
        switch(rarity) {
            case COMMON:
                options = this.commonPool;
                break;
            case UNCOMMON:
                options = this.uncommonPool;
                break;
            case RARE:
                options = this.rarePool;
                break;
            default:
                throw new RuntimeException("Invalid card rarity for rewards: " + rarity);
        }

        return options.get(rng.random(options.size() - 1)).makeCopy();
    }
}
