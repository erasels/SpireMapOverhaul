package spireMapOverhaul.zones.gremlinTown.relics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

public class LousePlushie extends AbstractSMORelic {
    public static final String ID = makeID(LousePlushie.class.getSimpleName());
    private static final int BLOCK_AMOUNT = 15;
    private boolean usedThisCombat = false;

    public LousePlushie() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replace("{0}", Integer.toString(BLOCK_AMOUNT));
    }

    @Override
    public void atPreBattle() {
        usedThisCombat = false;
        pulse = true;
        beginPulse();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !usedThisCombat
                && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            pulse = false;
            applyToSelf(new NextTurnBlockPower(adp(), BLOCK_AMOUNT));
            att(new RelicAboveCreatureAction(adp(), this));
            usedThisCombat = true;
            grayscale = true;
            incrementBlockStat(BLOCK_AMOUNT);
        }
        return damageAmount;
    }

    public void justEnteredRoom(AbstractRoom room) {
        grayscale = false;
    }

    public void onVictory() {
        pulse = false;
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String BLOCK_STAT = "block";

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(BLOCK_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float block = stats.get(BLOCK_STAT);
        String blockPerCombat = format.format(block / Math.max(totalCombats, 1));
        return getStatsDescription() + MessageFormat.format(DESCRIPTIONS[2], blockPerCombat);
    }

    public void resetStats() {
        stats.put(BLOCK_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(BLOCK_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(BLOCK_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementBlockStat(int amount) {
        stats.put(BLOCK_STAT, stats.getOrDefault(BLOCK_STAT, 0) + amount);
    }
}
