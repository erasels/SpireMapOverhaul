package spireMapOverhaul.zones.brokenspace.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.WingBoots;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.brokenspace.events.FakeEventRoom;
import spireMapOverhaul.zones.brokenspace.events.WingBootEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrokenWingBoots extends BrokenRelic {
    public static final String ID = "BrokenWingBoots";

    public BrokenWingBoots() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, WingBoots.ID);
    }


    @Override
    public boolean canSpawn() {
        return AbstractDungeon.actNum < 3 || Settings.isEndless;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room.event instanceof WingBootEvent) {
            this.flash();
        }
        super.onEnterRoom(room);
    }

    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = "generateRoom"
    )
    public static class WingBootEventPatch {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr e) throws CannotCompileException {
                    if (e.getClassName().equals(EventRoom.class.getName())) {
                        e.replace("{" +
                                "if (" + AbstractDungeon.class.getName() + ".player.hasRelic(" + SpireAnniversary6Mod.class.getName() + ".makeID(" + BrokenWingBoots.class.getName() + ".ID))) {" +
                                "$_ = new " + FakeEventRoom.class.getName() + "(new " + WingBootEvent.class.getName() + "());" +
                                SpireAnniversary6Mod.class.getName() + ".logger.info(\"WingBootEventPatch\");" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}" +
                                "}");
                    }
                }
            };
        }
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String CHOICES_STAT = "choices";

    public String getStatsDescription() {
        return DESCRIPTIONS[1].replace("{0}", stats.get(CHOICES_STAT) + "");
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(CHOICES_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(CHOICES_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(CHOICES_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementChoicesStat(int amount) {
        stats.put(CHOICES_STAT, stats.getOrDefault(CHOICES_STAT, 0) + amount);
    }
}
