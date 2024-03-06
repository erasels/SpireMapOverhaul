package spireMapOverhaul.zones.brokenspace.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.SneckoSkull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenSneckoSkull extends BrokenRelic implements OnReceivePowerRelic {
    public static final String ID = "BrokenSneckoSkull";
    public static final int AMOUNT = 1;

    public BrokenSneckoSkull() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, SneckoSkull.ID);
    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }


    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature) {
        flash();
        if (abstractPower.type == AbstractPower.PowerType.BUFF && abstractPower.amount > 0) {
            abstractPower.amount += AMOUNT;
        }
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature source, int stackAmount) {
        flash();
        if (power.type == AbstractPower.PowerType.BUFF && stackAmount > 0) {
            incrementBuffsStat(AMOUNT);
            return stackAmount + AMOUNT;
        }
        return stackAmount;
    }

    @Override
    public boolean canSpawn() {
        return adp().chosenClass == AbstractPlayer.PlayerClass.THE_SILENT;
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String BUFFS_STAT = "buffs";

    public String getStatsDescription() {
        return DESCRIPTIONS[2].replace("{0}", stats.get(BUFFS_STAT) + "");
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float buffs = stats.get(BUFFS_STAT);
        String buffsPerCombat = format.format(buffs / Math.max(totalCombats, 1));
        return getStatsDescription() + DESCRIPTIONS[3].replace("{0}", buffsPerCombat);
    }

    public void resetStats() {
        stats.put(BUFFS_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(BUFFS_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(BUFFS_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementBuffsStat(int amount) {
        stats.put(BUFFS_STAT, stats.getOrDefault(BUFFS_STAT, 0) + amount);
    }
}
