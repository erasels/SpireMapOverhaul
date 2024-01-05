package spireMapOverhaul.zones.candyland;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Bite;
import com.megacrit.cardcrawl.cards.red.Feed;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.rewards.HealReward;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;
import spireMapOverhaul.zones.candyland.consumables.common.*;
import spireMapOverhaul.zones.candyland.consumables.rare.Banana;
import spireMapOverhaul.zones.candyland.consumables.rare.ChocolateBar;
import spireMapOverhaul.zones.candyland.consumables.rare.Cocktail;
import spireMapOverhaul.zones.candyland.consumables.rare.GoldCandy;
import spireMapOverhaul.zones.candyland.consumables.uncommon.*;

import java.util.ArrayList;

public class CandyLand extends AbstractZone implements RewardModifyingZone, CampfireModifyingZone, ShopModifyingZone {
    public static final String ID = "CandyLand";

    public CandyLand() {
        super(ID, Icons.REST, Icons.SHOP);
        this.width = 3;
        this.height = 3;
    }

    @Override
    public AbstractZone copy() {
        return new CandyLand();
    }

    @Override
    public Color getColor() {
        return Color.PINK.cpy();
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : (ArrayList<AbstractCard>) cards.clone()) {
            cards.remove(card);
            ArrayList<AbstractConsumable> consumables = getConsumables();
            consumables.removeIf(c -> c.rarity != card.rarity || cards.stream().anyMatch(listCard -> c.cardID.equals(listCard.cardID)));
            AbstractCard c = consumables.get(AbstractDungeon.cardRng.random(0, consumables.size()-1));
            cards.add(c);
        }
    }

    @Override
    public void modifyReward(RewardItem rewardItem) {
        if(rewardItem.type == RewardItem.RewardType.RELIC && !(rewardItem.relic instanceof Mango || rewardItem.relic instanceof Pear || rewardItem.relic instanceof Strawberry || rewardItem.relic instanceof Waffle)){
            switch(rewardItem.relic.tier){
                case RARE:
                    rewardItem.relic = new Mango();
                    rewardItem.text = rewardItem.relic.name;
                    break;
                case UNCOMMON:
                    rewardItem.relic = new Pear();
                    rewardItem.text = rewardItem.relic.name;
                    break;
                case COMMON:
                    rewardItem.relic = new Strawberry();
                    rewardItem.text = rewardItem.relic.name;
                    break;
                default:
                    rewardItem.relic = new Waffle();
                    rewardItem.text = rewardItem.relic.name;
                    break;
            }
        } else if(rewardItem.type == RewardItem.RewardType.POTION){
            while(rewardItem.potion.isThrown){
                rewardItem.potion = AbstractDungeon.returnRandomPotion(rewardItem.potion.rarity, false);
            }
        }
    }

    @Override
    public void modifyRewards(ArrayList<RewardItem> rewards) {
        for (int i = 0; i < rewards.size(); i++) {
            RewardItem r = rewards.get(i);
            if(r.type == RewardItem.RewardType.GOLD){
                rewards.set(i, new HealReward("CandyLand/ChocolateCoins.png", (int) (0.5*r.goldAmt)));
            }
        }
    }

    @Override
    public void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {
        for (AbstractCampfireOption button : buttons){
            if (button instanceof SmithOption){
                button.usable = false;
            } else if(button instanceof RestOption){
                String newDescription = ReflectionHacks.getPrivateInherited(button, RestOption.class, "description");
                newDescription += TEXT[2];
                ReflectionHacks.setPrivateInherited(button, RestOption.class, "description", newDescription);

            }
        }
    }

    @Override
    public void postUseCampfireOption(AbstractCampfireOption option) {
        if(option instanceof RestOption) {
            AbstractDungeon.player.increaseMaxHp(5, true);
        }
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        ArrayList<AbstractCard> topRow = new ArrayList<>();
        for(AbstractCard card : coloredCards){
            ArrayList<AbstractConsumable> consumables = getConsumables();
            consumables.removeIf(c -> c.rarity != card.rarity || c.type != card.type || topRow.stream().anyMatch(rowCard -> c.cardID.equals(rowCard.cardID)) || c.cardID.equals(GoldCandy.ID));
            if(consumables.isEmpty()){
                consumables = getConsumables();
                consumables.removeIf(c -> c.type != card.type || topRow.stream().anyMatch(rowCard -> c.cardID.equals(rowCard.cardID)));

            }
            topRow.add(consumables.get(AbstractDungeon.cardRng.random(0, consumables.size()-1)));
        }
        coloredCards.clear();
        coloredCards.addAll(topRow);

        colorlessCards.clear();
        colorlessCards.add(new Bite());
        colorlessCards.add(new Feed());
    }

    @Override
    public float modifyCardBaseCost(AbstractCard c, float baseCost) {
        switch(c.cardID) {
            case Bite.ID: return 80 + AbstractDungeon.merchantRng.random(1, 19); // Uncommon Colorless cost
            case Feed.ID: return baseCost;
            default: return (float) (baseCost*0.4);
        }
    }

    @Override
    public void postCreateShopRelics(ShopScreen screen, ArrayList<StoreRelic> relics) {
        relics.clear();
        relics.add(new StoreRelic(new Waffle(), 0, screen));
        relics.add(new StoreRelic(new Pear(), 1, screen));
        relics.add(new StoreRelic(new Mango(), 2, screen));
    }

    @Override
    public void postCreateShopPotions(ShopScreen screen, ArrayList<StorePotion> potions) {
        for(StorePotion potion : potions){
            while(potion.potion.isThrown){
                potion.potion = AbstractDungeon.returnRandomPotion(potion.potion.rarity, true);
            }
        }
    }


    public ArrayList<AbstractConsumable> getConsumables(){
        ArrayList<AbstractConsumable> consumables = new ArrayList<>();
        consumables.add(new Banana());
        consumables.add(new ChocolateBar());
        consumables.add(new Cocktail());
        consumables.add(new GoldCandy());
        consumables.add(new Cake());
        consumables.add(new FrozenYoghurt());
        consumables.add(new JawBreaker());
        consumables.add(new JellyBeans());
        consumables.add(new Milk());
        consumables.add(new Pepper());
        consumables.add(new PoisonedApple());
        consumables.add(new ChewingGum());
        consumables.add(new Broccoli());
        consumables.add(new CandyCane());
        consumables.add(new Cookie());
        consumables.add(new EnergyDrink());
        consumables.add(new FastFood());
        return consumables;
    }

}
