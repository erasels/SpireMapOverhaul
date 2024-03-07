package spireMapOverhaul.zones.brokenspace.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrayerWheel;
import com.megacrit.cardcrawl.rewards.RewardItem;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                int gold = tmp * prayerWheelAmt * (GOLD - 1);
                __instance.bonusGold += gold;
                incrementGoldStat(gold);
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

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String GOLD_STAT = "gold";

    public String getStatsDescription() {
        return DESCRIPTIONS[3].replace("{0}", stats.get(GOLD_STAT) + "");
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(GOLD_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(GOLD_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(GOLD_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementGoldStat(int amount) {
        stats.put(GOLD_STAT, stats.getOrDefault(GOLD_STAT, 0) + amount);
    }
}
