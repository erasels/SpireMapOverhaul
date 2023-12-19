package spireMapOverhaul.patches.interfacePatches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;

import java.util.List;

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
                    EncounterModifyingZone.ZoneEncounter ze = encounters.get(AbstractDungeon.monsterRng.random(encounters.size() - 1));
                    AbstractDungeon.lastCombatMetricKey = ze.getID();
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
                    EncounterModifyingZone.ZoneEncounter ze = encounters.get(AbstractDungeon.monsterRng.random(encounters.size() - 1));
                    AbstractDungeon.lastCombatMetricKey = ze.getID();
                    return ze.getMonsterSupplier().get();
                }
            }
            return __result;
        }
    }

}
