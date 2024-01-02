package spireMapOverhaul.zones.brokenSpace.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MawBank;
import com.megacrit.cardcrawl.relics.PrayerWheel;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import spireMapOverhaul.patches.ZonePatches;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;

public class BrokenPrayerWheel extends BrokenRelic {
    public static final String ID = "BrokenPrayerWheel";
    public static final int AMOUNT = 4;


    public BrokenPrayerWheel() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, PrayerWheel.ID);

    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return -99;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }

    @SpirePatch2(clz = RewardItem.class, method = "applyGoldBonus")
    public static class BrokenPrayerWheelPatch {
        @SpirePrefixPatch
        public static void patch(RewardItem __instance) {
            if (AbstractDungeon.player.hasRelic(makeID(ID))) {
                int numberOfPrayerWheels = 0;
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r instanceof BrokenPrayerWheel) {
                        numberOfPrayerWheels++;
                    }
                }

                __instance.goldAmt *= AMOUNT * numberOfPrayerWheels;
            }
        }
    }
}
