package spireMapOverhaul.zones.brokenspace.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnChannelRelic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.relics.DataDisk;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenDataDisk extends BrokenRelic implements OnChannelRelic {
    public static final String ID = "BrokenDataDisk";
    public static final int AMOUNT = 1;
    private boolean usedThisTurn = false;
    private boolean firstTurnJank = true;

    public BrokenDataDisk() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, DataDisk.ID);
    }

    @Override
    public void onChannel(AbstractOrb abstractOrb) {
        if (usedThisTurn) {
            return;
        }
        addToBot(new ChannelAction(abstractOrb.makeCopy()));
        incrementOrbsStat(1);
        flash();
        usedThisTurn = true;
    }


    @Override
    public void atTurnStart() {
        if(!firstTurnJank) {
            usedThisTurn = false;
        } else {
            firstTurnJank = false; // GameActionManager.turn is 1 on the first two turns of combat
        }
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new ApplyPowerAction(adp(), adp(), new FocusPower(adp(), -AMOUNT), -AMOUNT));
        addToBot(new RelicAboveCreatureAction(adp(), BrokenDataDisk.this));
    }

    @Override
    public void onVictory() {
        usedThisTurn = false;
        firstTurnJank = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }

    @Override
    public boolean canSpawn() {
        return adp().chosenClass == AbstractPlayer.PlayerClass.DEFECT;
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String ORBS_STAT = "orbs";

    public String getStatsDescription() {
        return DESCRIPTIONS[2].replace("{0}", stats.get(ORBS_STAT) + "");
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float orbs = stats.get(ORBS_STAT);
        String orbsPerCombat = format.format(orbs / Math.max(totalCombats, 1));
        return getStatsDescription() + DESCRIPTIONS[3].replace("{0}", orbsPerCombat);
    }

    public void resetStats() {
        stats.put(ORBS_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(ORBS_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(ORBS_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementOrbsStat(int amount) {
        stats.put(ORBS_STAT, stats.getOrDefault(ORBS_STAT, 0) + amount);
    }
}
