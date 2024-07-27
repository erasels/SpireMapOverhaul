package spireMapOverhaul.zones.brokenspace.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.MawBank;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenMawBank extends BrokenRelic {
    public static final String ID = "BrokenMawBank";
    public static final int AMOUNT = 400;

    private boolean active = false;

    public BrokenMawBank() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, MawBank.ID);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof ShopRoom && !usedUp) {
            this.flash();
            adp().gainGold(AMOUNT);
            active = true;
        }
        super.onEnterRoom(room);
    }

    @Override
    public void setCounter(int setCounter) {
        this.counter = setCounter;
        if (setCounter == -2) {
            this.usedUp();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1] + AMOUNT + DESCRIPTIONS[2];
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = { SaveFile.class })
    public static class BrokenMawBankPatch {
        @SpirePrefixPatch
        public static void MarkRelicAsUsed(AbstractDungeon __instance, SaveFile saveFile) {
            BrokenMawBank brokenMawBank = (BrokenMawBank)adp().getRelic(SpireAnniversary6Mod.makeID(BrokenMawBank.ID));
            if (brokenMawBank != null && brokenMawBank.active) {
                brokenMawBank.active = false;
                brokenMawBank.flash();
                int goldLost = Math.min(AMOUNT, adp().gold);
                adp().loseGold(AMOUNT);
                incrementGoldLostStat(goldLost);
                brokenMawBank.usedUp();
                brokenMawBank.counter = -2;
            }
        }
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String GOLD_LOST_STAT = "goldLost";

    public String getStatsDescription() {
        return DESCRIPTIONS[3].replace("{0}", stats.get(GOLD_LOST_STAT) + "");
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(GOLD_LOST_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(GOLD_LOST_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(GOLD_LOST_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementGoldLostStat(int amount) {
        stats.put(GOLD_LOST_STAT, stats.getOrDefault(GOLD_LOST_STAT, 0) + amount);
    }
}
