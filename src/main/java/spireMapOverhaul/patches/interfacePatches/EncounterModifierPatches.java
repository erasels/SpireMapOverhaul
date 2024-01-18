package spireMapOverhaul.patches.interfacePatches;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.ActUtil;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;

import java.util.Comparator;
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
            MonsterGroup result = __result;
            AbstractZone zone = Wiz.getCurZone();
            if (zone instanceof EncounterModifyingZone) {
                EncounterModifyingZone z = (EncounterModifyingZone) zone;
                List<EncounterModifyingZone.ZoneEncounter> encounters = z.getNormalEncounters();
                if (encounters != null) {
                    encounters = encounters.stream().filter(e -> e.getActNum() == ActUtil.getRealActNum()).collect(Collectors.toList());
                    if (LastZoneNormalEncounter.lastZoneNormalEncounter != null && LastZoneNormalEncounter.lastZoneNormalEncounter.startsWith(zone.id + ":")) {
                        String encounterId = LastZoneNormalEncounter.lastZoneNormalEncounter.substring(zone.id.length() + 1);
                        encounters = encounters.stream().filter(e -> !e.getID().equals(encounterId)).collect(Collectors.toList());
                    }
                    if (encounters.isEmpty()) {
                        LastZoneNormalEncounter.lastZoneNormalEncounter = null;
                    } else {
                        EncounterModifyingZone.ZoneEncounter ze = encounters.get(AbstractDungeon.monsterRng.random(encounters.size() - 1));
                        AbstractDungeon.lastCombatMetricKey = ze.getID();
                        LastZoneNormalEncounter.lastZoneNormalEncounter = zone.id + ":" + ze.getID();
                        result = ze.getMonsterSupplier().get();
                    }
                }

                addAdditionalMonsters((EncounterModifyingZone) zone, result);
                result = z.changeEncounter(result, AbstractDungeon.lastCombatMetricKey);
            }
            return result;
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "getEliteMonsterForRoomCreation")
    public static class EliteEncounterPatch {
        @SpirePostfixPatch
        public static MonsterGroup ChangeEncounter(AbstractDungeon __instance, MonsterGroup __result) {
            MonsterGroup result = __result;
            AbstractZone zone = Wiz.getCurZone();
            if (zone instanceof EncounterModifyingZone) {
                EncounterModifyingZone z = (EncounterModifyingZone) zone;
                List<EncounterModifyingZone.ZoneEncounter> encounters = z.getEliteEncounters();
                if (encounters != null) {
                    encounters = encounters.stream().filter(e -> e.getActNum() == ActUtil.getRealActNum()).collect(Collectors.toList());
                    if (LastZoneEliteEncounter.lastZoneEliteEncounter != null && LastZoneEliteEncounter.lastZoneEliteEncounter.startsWith(zone.id + ":")) {
                        String encounterId = LastZoneEliteEncounter.lastZoneEliteEncounter.substring(zone.id.length() + 1);
                        encounters = encounters.stream().filter(e -> !e.getID().equals(encounterId)).collect(Collectors.toList());
                    }
                    if (encounters.isEmpty()) {
                        LastZoneEliteEncounter.lastZoneEliteEncounter = null;
                    } else {
                        EncounterModifyingZone.ZoneEncounter ze = encounters.get(AbstractDungeon.monsterRng.random(encounters.size() - 1));
                        AbstractDungeon.lastCombatMetricKey = ze.getID();
                        LastZoneEliteEncounter.lastZoneEliteEncounter = zone.id + ":" + ze.getID();
                        result = ze.getMonsterSupplier().get();
                    }
                }

                addAdditionalMonsters((EncounterModifyingZone) zone, result);
                result = z.changeEncounter(result, AbstractDungeon.lastCombatMetricKey);
            }
            return result;
        }
    }

    private static void addAdditionalMonsters(EncounterModifyingZone zone, MonsterGroup monsterGroup) {
        List<AbstractMonster> additionalMonsters = zone.getAdditionalMonsters();
        if (additionalMonsters != null) {
            float currentX = monsterGroup.monsters.stream().map(m -> m.drawX).min(Comparator.comparingDouble(x -> (double) x)).orElse(0.0f);
            for (AbstractMonster monster : additionalMonsters) {
                // We give healthy additional space to account for fights such as Reptomancer, which spawn minions in
                // the empty space near the main monster. Since there are modded enemies with this behavior too, this
                // isn't perfect, but it's pretty much impossible to get perfect positioning for this given all the
                // modded enemies out there and other ways that enemies can be added to fights.
                currentX -= monster.hb_w + 150.0f * Settings.xScale;
                monster.drawX = currentX;
                monsterGroup.monsters.add(0, monster);
            }
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "generateSeeds")
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
