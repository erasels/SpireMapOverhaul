package spireMapOverhaul.zones.brokenSpace.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Matryoshka;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class BrokenMatryoshka extends BrokenRelic {
    public static final String ID = "BrokenMatryoshka";
    public static final int AMOUNT = 4;
    public static final int EMPTY = 2;

    public BrokenMatryoshka() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Matryoshka.ID);
    }

    private RelicTier getRelicTier() {
        return AbstractDungeon.relicRng.randomBoolean(0.25F) ? RelicTier.COMMON : RelicTier.UNCOMMON;
    }

    @Override
    public void onEquip() {
        for (int i = 0; i < AMOUNT-1; i++) {
            AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(getRelicTier());
            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(r));
        }
        AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(getRelicTier());
        RewardItem reward = new RewardItem(r);
        AbstractDungeon.getCurrRoom().rewards.add(reward);
        if (!Settings.hasSapphireKey && Settings.isFinalActAvailable) {
            AbstractDungeon.getCurrRoom().addSapphireKey(reward);
        }

        AbstractDungeon.getCurrRoom().rewards.forEach(rewardItem -> {
            if (!AbstractDungeon.combatRewardScreen.rewards.contains(rewardItem)) {
                AbstractDungeon.combatRewardScreen.rewards.add(rewardItem);
            }
        });

        counter = EMPTY;

    }

    public void onChestOpenAfter(boolean bossChest) {
        if (!bossChest && this.counter > 0) {// 15 16
            --this.counter;// 17
            this.flash();// 19
            AbstractDungeon.getCurrRoom().removeOneRelicFromRewards();// 20
            if (this.counter == 0) {// 22
                this.setCounter(-2);// 23
                usedUp();
            }
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1] + EMPTY + DESCRIPTIONS[2];
    }
}
