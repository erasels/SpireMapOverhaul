package spireMapOverhaul.zones.brokenspace.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Matryoshka;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

public class BrokenMatryoshka extends BrokenRelic {
    public static final String ID = "BrokenMatryoshka";
    public static final int AMOUNT = 4;
    public static final int EMPTY = 2;
    public boolean triggered = false;

    public BrokenMatryoshka() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Matryoshka.ID);
    }

    private RelicTier getRelicTier() {
        return AbstractDungeon.relicRng.randomBoolean(0.25F) ? RelicTier.COMMON : RelicTier.UNCOMMON;
    }

    @Override
    public void onEquip() {
        triggered = true;
        counter = EMPTY;

    }

    @Override
    public void update() {
        if (triggered) {
            triggered = false;
            ArrayList<RewardItem> newRelics = new ArrayList<>();
            for (int i = 0; i < AMOUNT - 1; i++) {
                AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(getRelicTier());
                RewardItem reward = new RewardItem(r);
                AbstractDungeon.getCurrRoom().rewards.add(reward);
                newRelics.add(reward);

            }
            AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(getRelicTier());
            RewardItem reward = new RewardItem(r);
            AbstractDungeon.getCurrRoom().rewards.add(reward);
            newRelics.add(reward);
            if (!Settings.hasSapphireKey && Settings.isFinalActAvailable) {
                RewardItem key = new RewardItem(reward, RewardItem.RewardType.SAPPHIRE_KEY);
                AbstractDungeon.getCurrRoom().rewards.add(key);
                newRelics.add(key);

            }
            AbstractDungeon.getCurrRoom().rewards.removeIf(rewardItem -> rewardItem.relic == this);
            AbstractDungeon.combatRewardScreen.rewards.removeIf(rewardItem -> rewardItem.relic == this);

            AbstractDungeon.combatRewardScreen.rewards.addAll(newRelics);
            AbstractDungeon.combatRewardScreen.positionRewards();
        }

        super.update();
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
