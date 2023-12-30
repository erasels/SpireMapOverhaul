package spireMapOverhaul.zones.brokenSpace;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Omega;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;
import spireMapOverhaul.zones.example.CoolExampleEvent;

import java.util.ArrayList;

public class BrokenSpaceZone extends AbstractZone implements RewardModifyingZone, ShopModifyingZone {
    public static final String ID = "BrokenSpace";
    private final int width, height;
    private final Color color;

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

        color = Color.GRAY.cpy();
        this.name = name;
    }


    @Override
    public boolean generateMapArea(BetterMapGenerator.MapPlanner planner) {
        return generateNormalArea(planner, width, height);
    }

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

    public AbstractRelic GetTrulyRandomRelic(Random rng) {
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        relics.addAll(RelicLibrary.commonList);
        relics.addAll(RelicLibrary.uncommonList);
        relics.addAll(RelicLibrary.rareList);
        relics.addAll(RelicLibrary.bossList);
        relics.addAll(RelicLibrary.specialList);
        return relics.get(rng.random(relics.size() - 1)).makeCopy();
    }

    @Override
    public void postCreateShopRelics(ShopScreen screen, ArrayList<StoreRelic> relics) {
        int amount = relics.size();
        relics.clear();

        for (int i = 0; i < amount; i++) {
            relics.add(new StoreRelic(GetTrulyRandomRelic(AbstractDungeon.relicRng), i, screen));
        }
    }

    @Override
    public void modifyReward(RewardItem rewardItem) {
        if (rewardItem.type == RewardItem.RewardType.RELIC) {
            rewardItem.relic = GetTrulyRandomRelic(AbstractDungeon.relicRng);
        }
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        int amount = cards.size();
        cards.clear();

        for (int i = 0; i < amount; i++) {
            cards.add(GetTrulyRandomCard(AbstractDungeon.cardRandomRng));
        }
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        int amount = coloredCards.size();
        coloredCards.clear();

        for (int i = 0; i < amount; i++) {
            coloredCards.add(GetTrulyRandomCard(AbstractDungeon.cardRandomRng));
        }
    }

}