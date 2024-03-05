package spireMapOverhaul.zones.brokenspace.relics;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.watcher.MantraPower;
import com.megacrit.cardcrawl.relics.Damaru;
import spireMapOverhaul.util.Wiz;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

public class BrokenDamaru extends BrokenRelic {
    public static final String ID = "BrokenDamaru";

    public static final int MANTRA_AMOUNT = 2;

    public BrokenDamaru() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Damaru.ID);
    }

    @Override
    public boolean canSpawn() {
        return adp().chosenClass == AbstractPlayer.PlayerClass.WATCHER;
    }

    @Override
    public void onPlayerEndTurn() {
        addToBot(new SelectCardsInHandAction(1, DESCRIPTIONS[1], false, true,
                c -> true,
                (abstractCards -> {
                    if(!abstractCards.isEmpty()) {
                        AbstractCard c = abstractCards.get(0);
                        if(Wiz.getLogicalCardCost(c) > 0) {
                            int mantra = Wiz.getLogicalCardCost(c);
                            applyToSelf(new MantraPower(adp(), mantra));
                            incrementMantraStat(mantra);
                            addToBot(new ExhaustSpecificCardAction(c, adp().hand));
                            flash();
                        }
                    }
                }))
        );
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String MANTRA_STAT = "mantra";

    public String getStatsDescription() {
        return DESCRIPTIONS[2].replace("{0}", stats.get(MANTRA_STAT) + "");
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float mantra = stats.get(MANTRA_STAT);
        String mantraPerCombat = format.format(mantra / Math.max(totalCombats, 1));
        return getStatsDescription() + DESCRIPTIONS[3].replace("{0}", mantraPerCombat);
    }

    public void resetStats() {
        stats.put(MANTRA_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(MANTRA_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(MANTRA_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementMantraStat(int amount) {
        stats.put(MANTRA_STAT, stats.getOrDefault(MANTRA_STAT, 0) + amount);
    }
}