package spireMapOverhaul.zones.gremlinTown.relics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;

public class Lockpicks extends AbstractSMORelic {
    public static final String ID = makeID(Lockpicks.class.getSimpleName());
    private static final int GOLD_AMOUNT = 30;

    public Lockpicks() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replace("{0}", Integer.toString(GOLD_AMOUNT));
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof RestRoom) {
            flash();
            adp().gainGold(GOLD_AMOUNT);
            incrementGoldStat(GOLD_AMOUNT);
        }
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String GOLD_STAT = "gold";

    public String getStatsDescription() {
        return DESCRIPTIONS[1].replace("{0}", stats.get(GOLD_STAT) + "");
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
