package spireMapOverhaul.zones.example;

import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.MindBlast;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rewards.RewardItem;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;

import java.util.ArrayList;

public class ExampleRewardModifyingZone extends AbstractZone implements RewardModifyingZone {
    public static final String ID = "ExampleRewardModifying";

    public ExampleRewardModifyingZone() {
        super(ID);
        this.width = 3;
        this.height = 3;
    }

    @Override
    public AbstractZone copy() {
        return new ExampleRewardModifyingZone();
    }

    @Override
    public Color getColor() {
        return new Color(1f,0f,0f,1f);
    }

    @Override
    public ArrayList<AbstractCard> getAdditionalCardReward() {
        // This creates a reward of all uncommons, following the normal logic for how many cards are in the reward and whether any of them are upgraded
        ArrayList<AbstractCard> cards = new ArrayList<>();
        int numCards = this.getNumberOfCardsInReward();
        for (int i = 0; i < numCards; i++) {
            AbstractCard card = AbstractDungeon.getCard(AbstractCard.CardRarity.UNCOMMON);
            this.applyStandardUpgradeLogic(card);
            cards.add(card);
        }
        return cards;
    }

    @Override
    public RewardItem getAdditionalReward() {
        if (AbstractDungeon.treasureRng.randomBoolean()) {
            return new RewardItem(new Circlet());
        }
        return null;
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        AbstractCard extraCard = new MindBlast();
        this.applyStandardUpgradeLogic(extraCard);
        cards.add(extraCard);
        for (AbstractCard card : cards) {
            CardModifierManager.addModifier(card, new EtherealMod());
        }
    }

    @Override
    public void modifyReward(RewardItem rewardItem) {
        if (rewardItem.type == RewardItem.RewardType.GOLD) {
            float multiplier = AbstractDungeon.treasureRng.random(1.5f, 2.0f);
            SpireAnniversary6Mod.logger.info("Multiplying gold amount by " + multiplier);
            rewardItem.goldAmt = (int)(rewardItem.goldAmt * multiplier);
        }
    }

    @Override
    public void modifyRewards(ArrayList<RewardItem> rewards) {
        if (AbstractDungeon.treasureRng.randomBoolean()) {
            SpireAnniversary6Mod.logger.info("Removing all non-Circlet relics");
            rewards.removeIf(r -> r.type == RewardItem.RewardType.RELIC && !(r.relic instanceof Circlet));
        }
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        AbstractDungeon.player.increaseMaxHp(1, false);
    }

    @Override
    public int changeRareCardRewardChance(int rareCardRewardChance) {
        return rareCardRewardChance + 50;
    }

    @Override
    public boolean allowUpgradingRareCards() {
        return true;
    }

    @Override
    public float changeCardUpgradeChance(float curChance) {
        return 0.5f;
    }
}
