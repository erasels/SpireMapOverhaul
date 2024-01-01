package spireMapOverhaul.zones.brokenSpace;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Omega;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import java.util.ArrayList;

public class BrokenSpaceZone extends AbstractZone implements RewardModifyingZone, ShopModifyingZone {
    public static final String ID = "BrokenSpace";
    private final int width, height;
    private final Color color;
    public static ArrayList<String> BadRelics = new ArrayList<>();

    public BrokenSpaceZone() {
        this("Broken Space 0", 2, 4);

    }

    @Override
    public AbstractZone copy() {
        return new BrokenSpaceZone(name, width, height);
    }

    private BrokenSpaceZone(String name, int width, int height) {
        super(ID, Icons.REWARD, Icons.SHOP);

        this.width = width;
        this.height = height;

        color = Color.DARK_GRAY.cpy();
        this.name = name;
    }


    @Override
    public boolean generateMapArea(BetterMapGenerator.MapPlanner planner) {
        return generateNormalArea(planner, width, height);
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
    protected boolean canIncludeTreasureRow() {
        return false;
    }

//    @Override
//    public boolean canSpawn() {
//        return AbstractDungeon.actNum >= 3;
//    }

    @Override
    public Color getColor() { //I considered changing this to a variable, but a method lets you do funky stuff like a rainbow zone that changes colors or something.
        return color;
    }

    public AbstractCard GetTrulyRandomCard(Random rng) {
        AbstractCard c = CardLibrary.getAllCards().get(rng.random(CardLibrary.getAllCards().size() - 1)).makeCopy();
        // make omega less common
        if (c.cardID == Omega.ID) {
            c = CardLibrary.getAllCards().get(rng.random(CardLibrary.getAllCards().size() - 1)).makeCopy();
        }
        return c;
    }

    public static AbstractRelic GetTrulyRandomRelic(Random rng) {
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        relics.addAll(RelicLibrary.commonList);
        relics.addAll(RelicLibrary.uncommonList);
        relics.addAll(RelicLibrary.rareList);
        relics.addAll(RelicLibrary.bossList);
        relics.addAll(RelicLibrary.specialList);

        for (String s : BadRelics) {
            relics.removeIf(r -> r.relicId.equals(s));
        }

        AbstractRelic r = relics.get(rng.random(relics.size() - 1)).makeCopy();
        UnnaturalRelicField.unnatural.set(r, true);

        return r;
    }

    @Override
    public void postCreateShopRelics(ShopScreen screen, ArrayList<StoreRelic> relics) {
        int amount = relics.size();
        relics.clear();

        for (int i = 0; i < amount; i++) {
            relics.add(new StoreRelic(GetTrulyRandomRelic(AbstractDungeon.relicRng), i, screen));
        }
    }

//    @Override
//    public void modifyReward(RewardItem rewardItem) {
//        if (rewardItem.type == RewardItem.RewardType.RELIC) {
//            rewardItem.relic = GetTrulyRandomRelic(AbstractDungeon.relicRng);
//        }
//    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        int amount = cards.size();
        cards.clear();

        for (int i = 0; i < amount; i++) {
            AbstractCard c = GetTrulyRandomCard(AbstractDungeon.cardRandomRng);
            cards.add(c);
            UnnaturalCardField.unnatural.set(c, true);

        }
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        int amount = coloredCards.size();
        coloredCards.clear();

        for (int i = 0; i < amount; i++) {
            AbstractCard c = GetTrulyRandomCard(AbstractDungeon.cardRandomRng);
            coloredCards.add(c);
            UnnaturalCardField.unnatural.set(c, true);

        }
    }

    @SpirePatch2(
            clz = AbstractCard.class,
            method = SpirePatch.CLASS
    )
    public static class UnnaturalCardField {
        public static SpireField<Boolean> unnatural = new SpireField<>(() -> false);
    }

    @SpirePatch2(
            clz = AbstractRelic.class,
            method = SpirePatch.CLASS
    )
    public static class UnnaturalRelicField {
        public static SpireField<Boolean> unnatural = new SpireField<>(() -> false);
    }

    static {
        BadRelics.add("Orrery");

        BadRelics.add("Tiny House");
        BadRelics.add("Bottled Flame");
        BadRelics.add("Bottled Lightning");
        BadRelics.add("Bottled Tornado");
    }

}