package spireMapOverhaul.patches.interfacePatches;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.ActUtil;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;

import java.util.List;
import java.util.stream.Collectors;

public class EncounterModifierPatches {
    // These are deliberately postfix patches so that the original logic still takes place, including advancing through
    // the list of planned encounters in the act (which means that zone encounters don't delay getting to "hard pool"
    // normal fights and that you can get the same non-zone encounter if there's a zone encounter in between).
    @SpirePatch2(clz = AbstractDungeon.class, method = "getMonsterForRoomCreation")
    public static class NormalEncounterPatch {
        @SpirePostfixPatch
        public static MonsterGroup ChangeEncounter(AbstractDungeon __instance, MonsterGroup __result) {
            AbstractZone zone = Wiz.getCurZone();
            if (zone instanceof EncounterModifyingZone) {
                List<EncounterModifyingZone.ZoneEncounter> encounters = ((EncounterModifyingZone) zone).getNormalEncounters();
                if (encounters != null) {
                    encounters = encounters.stream().filter(e -> e.getActNum() == ActUtil.getRealActNum()).collect(Collectors.toList());
                    if (LastZoneNormalEncounter.lastZoneNormalEncounter != null && LastZoneNormalEncounter.lastZoneNormalEncounter.startsWith(zone.id + ":")) {
                        String encounterId = LastZoneNormalEncounter.lastZoneNormalEncounter.substring(zone.id.length() + 1);
                        encounters = encounters.stream().filter(e -> !e.getID().equals(encounterId)).collect(Collectors.toList());
                    }
                    if (encounters.isEmpty()) {
                        LastZoneNormalEncounter.lastZoneNormalEncounter = null;
                        return __result;
                    }
                    EncounterModifyingZone.ZoneEncounter ze = encounters.get(AbstractDungeon.monsterRng.random(encounters.size() - 1));
                    AbstractDungeon.lastCombatMetricKey = ze.getID();
                    LastZoneNormalEncounter.lastZoneNormalEncounter = zone.id + ":" + ze.getID();
                    return ze.getMonsterSupplier().get();
                }
            }
            return __result;
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "getEliteMonsterForRoomCreation")
    public static class EliteEncounterPatch {
        @SpirePostfixPatch
        public static MonsterGroup ChangeEncounter(AbstractDungeon __instance, MonsterGroup __result) {
            AbstractZone zone = Wiz.getCurZone();
            if (zone instanceof EncounterModifyingZone) {
                List<EncounterModifyingZone.ZoneEncounter> encounters = ((EncounterModifyingZone) zone).getEliteEncounters();
                if (encounters != null) {
                    encounters = encounters.stream().filter(e -> e.getActNum() == ActUtil.getRealActNum()).collect(Collectors.toList());
                    if (LastZoneEliteEncounter.lastZoneEliteEncounter != null && LastZoneEliteEncounter.lastZoneEliteEncounter.startsWith(zone.id + ":")) {
                        String encounterId = LastZoneEliteEncounter.lastZoneEliteEncounter.substring(zone.id.length() + 1);
                        encounters = encounters.stream().filter(e -> !e.getID().equals(encounterId)).collect(Collectors.toList());
                    }
                    if (encounters.isEmpty()) {
                        LastZoneEliteEncounter.lastZoneEliteEncounter = null;
                        return __result;
                    }
                    EncounterModifyingZone.ZoneEncounter ze = encounters.get(AbstractDungeon.monsterRng.random(encounters.size() - 1));
                    AbstractDungeon.lastCombatMetricKey = ze.getID();
                    LastZoneEliteEncounter.lastZoneEliteEncounter = zone.id + ":" + ze.getID();
                    return ze.getMonsterSupplier().get();
                }
            }
            return __result;
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "generateSeeds")
    public static class NullOutZoneEncountersPatch {
        @SpirePostfixPatch
        public static void NullOutZoneEncounters() {
            LastZoneNormalEncounter.lastZoneNormalEncounter = null;
            LastZoneEliteEncounter.lastZoneEliteEncounter = null;
        }
    }

    public static class LastZoneNormalEncounter implements CustomSavable<String> {
        public final static String SaveKey = "LastZoneNormalEncounter";

        public static String lastZoneNormalEncounter = null;

        @Override
        public String onSave() {
            return lastZoneNormalEncounter;
        }

        @Override
        public void onLoad(String s) {
            LastZoneNormalEncounter.lastZoneNormalEncounter = s;
        }
    }

    public static class LastZoneEliteEncounter implements CustomSavable<String> {
        public final static String SaveKey = "LastZoneEliteEncounter";

        public static String lastZoneEliteEncounter = null;

        @Override
        public String onSave() {
            return lastZoneEliteEncounter;
        }

        @Override
        public void onLoad(String s) {
            LastZoneNormalEncounter.lastZoneNormalEncounter = s;
        }
    }
}
