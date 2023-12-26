package spireMapOverhaul.zones.candyland;

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
import spireMapOverhaul.SpireAnniversary6Mod;
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
import java.util.Objects;

public class CandyLand extends AbstractZone implements RewardModifyingZone, CampfireModifyingZone, ShopModifyingZone {
    public static final String ID = "CandyLand";

    public CandyLand() {
        super(ID, Icons.REWARD, Icons.REST, Icons.SHOP);
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
            consumables.removeIf(c -> c.rarity != card.rarity || containCard(cards, c));
            AbstractCard c = consumables.get(AbstractDungeon.cardRng.random(0, consumables.size()-1));
            SpireAnniversary6Mod.logger.info("Adding " + c.name + cards.contains(c));
            cards.add(c);
        }
    }

    private boolean containCard(ArrayList<AbstractCard> list, AbstractCard c){
        for(AbstractCard card : list)
            if (Objects.equals(c.cardID, card.cardID))
                return true;
        return false;
    }

    @Override
    public void modifyReward(RewardItem rewardItem) {
        if(rewardItem.type == RewardItem.RewardType.RELIC && !(rewardItem.relic instanceof Mango || rewardItem.relic instanceof Pear || rewardItem.relic instanceof Strawberry || rewardItem.relic instanceof Waffle)){
            switch(rewardItem.relic.tier){
                case RARE:
                    SpireAnniversary6Mod.logger.info("Replacing " + rewardItem.relic.name + " with Mango");
                    rewardItem.relic = new Mango();
                    rewardItem.text = rewardItem.relic.name;
                    break;
                case UNCOMMON:
                    SpireAnniversary6Mod.logger.info("Replacing " + rewardItem.relic.name + " with Pear");
                    rewardItem.relic = new Pear();
                    rewardItem.text = rewardItem.relic.name;
                    break;
                case COMMON:
                    SpireAnniversary6Mod.logger.info("Replacing " + rewardItem.relic.name + " with Strawberry");
                    rewardItem.relic = new Strawberry();
                    rewardItem.text = rewardItem.relic.name;
                    break;
                default:
                    SpireAnniversary6Mod.logger.info("Non-standard rarity found, replacing " + rewardItem.relic.name + " with Waffle");
                    rewardItem.relic = new Waffle();
                    rewardItem.text = rewardItem.relic.name;
                    break;
            }
        } else if(rewardItem.type == RewardItem.RewardType.POTION){
            while(rewardItem.potion.isThrown){
                SpireAnniversary6Mod.logger.info("Try to replace " + rewardItem.potion.name + "with drinkable potion.");
                rewardItem.potion = AbstractDungeon.returnRandomPotion(rewardItem.potion.rarity, false);
            }
        }
    }

    @Override
    public void modifyRewards(ArrayList<RewardItem> rewards) {
        SpireAnniversary6Mod.logger.info("Turning Gold into Chocolate");
        for(RewardItem r : (ArrayList<RewardItem>) rewards.clone()){
            if(r.type == RewardItem.RewardType.GOLD){
                rewards.add(new HealReward("ChocolateCoins.png", r.goldAmt));
            }
        }
        rewards.removeIf(r -> r.type == RewardItem.RewardType.GOLD);
    }

    public void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {
        for (AbstractCampfireOption button : buttons){
            if (button instanceof SmithOption){
                button.usable = false;
            }
        }
    }

    public void postUseCampfireOption(AbstractCampfireOption option) {
        if(option instanceof RestOption) {
            AbstractDungeon.player.increaseMaxHp(5, true);
        }
    }

    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        ArrayList<AbstractCard> topRow = new ArrayList<>();
        for(AbstractCard card : coloredCards){
            ArrayList<AbstractConsumable> consumables = getConsumables();
            consumables.removeIf(c -> c.rarity != card.rarity || c.type != card.type || containCard(topRow, c));
            if(consumables.isEmpty()){
                consumables = getConsumables();
                consumables.removeIf(c -> c.type != card.type || containCard(topRow, c));

            }
            topRow.add(consumables.get(AbstractDungeon.cardRng.random(0, consumables.size()-1)));
        }
        coloredCards.clear();
        coloredCards.addAll(topRow);

        colorlessCards.clear();
        colorlessCards.add(new Bite());
        colorlessCards.add(new Feed());
    }

    public float modifyCardBaseCost(AbstractCard c, float baseCost) {
        switch(c.cardID) {
            case Bite.ID: return 80 + AbstractDungeon.merchantRng.random(1, 19); // Uncommon Colorless cost
            case Feed.ID: return baseCost;
            default: return (float) (baseCost*0.5);
        }
    }

    public void postCreateShopRelics(ShopScreen screen, ArrayList<StoreRelic> relics) {
        relics.clear();
        relics.add(new StoreRelic(new Waffle(), 0, screen));
        relics.add(new StoreRelic(new Pear(), 1, screen));
        relics.add(new StoreRelic(new Mango(), 2, screen));
    }

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
        consumables.add(new RottenFlesh());
        consumables.add(new ChewingGum());
        consumables.add(new Broccoli());
        consumables.add(new CandyCane());
        consumables.add(new Cookie());
        consumables.add(new EnergyDrink());
        consumables.add(new FastFood());
        for(AbstractConsumable c : consumables){
            c.setRarity();
        }
        return consumables;
    }

}
