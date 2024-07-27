package spireMapOverhaul.zones.brokenspace.relics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.SingingBowl;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.interfaces.relics.MaxHPChangeRelic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrokenSingingBowl extends BrokenRelic implements MaxHPChangeRelic {
    public static final String ID = "BrokenSingingBowl";
    public static final int AMOUNT = 1;

    public BrokenSingingBowl() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, SingingBowl.ID);
    }

    @Override
    public int onMaxHPChange(int amount) {
        if(amount <= 0) return amount;

        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        flash();

        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {// 102
                upgradableCards.add(c);// 103
            }
        }

        for (int i = 0; i < AMOUNT; i++) {
            if (!upgradableCards.isEmpty()) {
                AbstractCard c = upgradableCards.remove(AbstractDungeon.miscRng.random(upgradableCards.size() - 1));
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                incrementUpgradesStat(1);
            }
        }
        return amount;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String UPGRADES_STAT = "upgrades";

    public String getStatsDescription() {
        return DESCRIPTIONS[2].replace("{0}", stats.get(UPGRADES_STAT) + "");
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(UPGRADES_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(UPGRADES_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(UPGRADES_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementUpgradesStat(int amount) {
        stats.put(UPGRADES_STAT, stats.getOrDefault(UPGRADES_STAT, 0) + amount);
    }
}
