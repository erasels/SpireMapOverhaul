package spireMapOverhaul.zones.gremlinTown.relics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
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

public class Duelity extends AbstractSMORelic {
    public static final String ID = makeID(Duelity.class.getSimpleName());

    public Duelity() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            flash();
            atb(new RelicAboveCreatureAction(adp(), this));
            atb(new ApplyPowerAction(adp(), adp(), new StrengthPower(adp(), 1), 1));
            atb(new ApplyPowerAction(adp(), adp(), new LoseStrengthPower(adp(), 1), 1));
            incrementStrengthStat(1);
        }
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String STRENGTH_STAT = "strength";

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(STRENGTH_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float strength = stats.get(STRENGTH_STAT);
        String strengthPerTurn = format.format(strength / Math.max(totalTurns, 1));
        String strengthPerCombat = format.format(strength / Math.max(totalCombats, 1));
        return getStatsDescription() + MessageFormat.format(DESCRIPTIONS[2], strengthPerTurn, strengthPerCombat);
    }

    public void resetStats() {
        stats.put(STRENGTH_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(STRENGTH_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(STRENGTH_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementStrengthStat(int amount) {
        stats.put(STRENGTH_STAT, stats.getOrDefault(STRENGTH_STAT, 0) + amount);
    }
}
