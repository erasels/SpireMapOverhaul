package spireMapOverhaul.zones.brokenSpace.relics;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrayerWheel;
import com.megacrit.cardcrawl.rewards.RewardItem;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class BrokenPrayerWheel extends BrokenRelic {
    public static final String ID = "BrokenPrayerWheel";
    public static final int GOLD = 2;
    public static final int AMOUNT = 1;


    public BrokenPrayerWheel() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, PrayerWheel.ID);

    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards - AMOUNT;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1] + GOLD + DESCRIPTIONS[2];
    }

    @SpirePatch2(clz = RewardItem.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {int.class, boolean.class})
    @SpirePatch2(clz = RewardItem.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {int.class})
    public static class BrokenPrayerWheelPatch {
        @SpirePrefixPatch
        public static void patch(RewardItem __instance, @ByRef int[] gold) {
            if (AbstractDungeon.player.hasRelic(makeID(ID))) {
                int numberOfPrayerWheels = 0;
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r instanceof BrokenPrayerWheel) {
                        numberOfPrayerWheels++;
                    }
                }

                gold[0] *= GOLD * numberOfPrayerWheels;
            }
        }
    }
}
