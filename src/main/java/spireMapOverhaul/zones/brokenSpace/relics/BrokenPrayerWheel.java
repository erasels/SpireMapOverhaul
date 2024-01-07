package spireMapOverhaul.zones.brokenSpace.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrayerWheel;
import com.megacrit.cardcrawl.rewards.RewardItem;
import javassist.CtBehavior;

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

    @SpirePatch2(clz = RewardItem.class, method = "applyGoldBonus")

    public static class BrokenPrayerWheelPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void patch(RewardItem __instance, int tmp) {
            if (AbstractDungeon.player.hasRelic(makeID(ID))) {
                int prayerWheelAmt = 0;
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r instanceof BrokenPrayerWheel) {
                        prayerWheelAmt++;
                    }
                }

                //noinspection PointlessArithmeticExpression
                __instance.bonusGold += tmp * prayerWheelAmt * (GOLD - 1);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }

    }
}
