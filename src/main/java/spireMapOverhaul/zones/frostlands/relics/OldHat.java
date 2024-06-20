package spireMapOverhaul.zones.frostlands.relics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.frostlands.FrostlandsZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class OldHat extends AbstractSMORelic{
    public static final String ID = makeID(OldHat.class.getSimpleName());
    public static final int amount = 3;
    public OldHat() {
        super(ID, FrostlandsZone.ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atTurnStart() {
        Wiz.atb(new ApplyPowerAction(Wiz.adp(), Wiz.adp(), new VigorPower(Wiz.adp(), amount)));
        incrementVigorStat(amount);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OldHat();
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String VIGOR_STAT = "vigor";

    public String getStatsDescription() {
        return DESCRIPTIONS[2].replace("{0}", stats.get(VIGOR_STAT) + "");
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(VIGOR_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(VIGOR_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(VIGOR_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementVigorStat(int amount) {
        stats.put(VIGOR_STAT, stats.getOrDefault(VIGOR_STAT, 0) + amount);
    }

}
