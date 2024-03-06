package spireMapOverhaul.zones.gremlinTown.relics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

public class GremlinClaw extends AbstractSMORelic {
    public static final String ID = makeID(GremlinClaw.class.getSimpleName());

    public GremlinClaw() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0 && !m.hasPower(MinionPower.POWER_ID)) {
            flash();
            atb(new RelicAboveCreatureAction(adp(), this));
            atb(new HealAction(adp(), adp(), 1));
            incrementHealStat(1);
        }
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String HEAL_STAT = "heal";

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(HEAL_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float heal = stats.get(HEAL_STAT);
        String healPerCombat = format.format(heal / Math.max(totalCombats, 1));
        return getStatsDescription() + MessageFormat.format(DESCRIPTIONS[2], healPerCombat);
    }

    public void resetStats() {
        stats.put(HEAL_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(HEAL_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(HEAL_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementHealStat(int amount) {
        stats.put(HEAL_STAT, stats.getOrDefault(HEAL_STAT, 0) + amount);
    }
}
