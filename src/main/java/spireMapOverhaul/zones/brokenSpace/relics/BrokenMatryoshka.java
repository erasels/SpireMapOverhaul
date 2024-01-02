package spireMapOverhaul.zones.brokenSpace.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Matryoshka;
import com.megacrit.cardcrawl.rewards.RewardItem;
import spireMapOverhaul.zones.brokenSpace.relics.BrokenRelic;

import java.util.ArrayList;

public class BrokenMatryoshka extends BrokenRelic {
    public static final String ID = "BrokenMatryoshka";
    public static final int AMOUNT = 3;
    public static final int EMPTY = 2;

    public BrokenMatryoshka() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Matryoshka.ID);
    }

    @Override
    public void obtain() {
        for (int i = 0; i < AMOUNT; i++) {
            AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, r);
            counter = EMPTY;
        }
        super.obtain();
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
