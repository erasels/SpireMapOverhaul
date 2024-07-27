package spireMapOverhaul.zones.gremlinTown.relics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToEnemyTop;

public class NobClub extends AbstractSMORelic {
    public static final String ID = makeID(NobClub.class.getSimpleName());
    private static final int ATTACK_THRESHOLD = 5;

    public NobClub() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replace("{0}", Integer.toString(ATTACK_THRESHOLD));
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            ++counter;
            if (counter % ATTACK_THRESHOLD == 0) {
                counter = 0;
                flash();
                addToBot(new RelicAboveCreatureAction(adp(), this));
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        AbstractMonster m = Wiz.getRandomEnemy();
                        if (m != null) {
                            applyToEnemyTop(m, new VulnerablePower(m, 1, false));
                            incrementVulnerableStat(1);
                        }
                        isDone = true;
                    }
                });
            }
        }
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String VULNERABLE_STAT = "vulnerable";

    public String getStatsDescription() {
        return DESCRIPTIONS[1].replace("{0}", stats.get(VULNERABLE_STAT) + "");
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float vulnerable = stats.get(VULNERABLE_STAT);
        String vulnerablePerTurn = format.format(vulnerable / Math.max(totalTurns, 1));
        String vulnerablePerCombat = format.format(vulnerable / Math.max(totalCombats, 1));
        return getStatsDescription() + DESCRIPTIONS[2].replace("{0}", vulnerablePerTurn).replace("{1}", vulnerablePerCombat);
    }

    public void resetStats() {
        stats.put(VULNERABLE_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(VULNERABLE_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(VULNERABLE_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementVulnerableStat(int amount) {
        stats.put(VULNERABLE_STAT, stats.getOrDefault(VULNERABLE_STAT, 0) + amount);
    }
}
