package spireMapOverhaul.patches.interfacePatches;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.ActUtil;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EncounterModifierPatches {
    @SpirePatch(clz = MapRoomNode.class, method = SpirePatch.CLASS)
    public static class EncounterModifierPatchFields
    {
        public static SpireField<Boolean> isZoneNormalEncounter = new SpireField<>(() -> false);
        public static SpireField<Boolean> isZoneEliteEncounter = new SpireField<>(() -> false);
        public static SpireField<Boolean> resetZoneNormalEncounter = new SpireField<>(() -> false);
        public static SpireField<Boolean> resetZoneEliteEncounter = new SpireField<>(() -> false);
        public static SpireField<Boolean> loadingPostCombat = new SpireField<>(() -> false);
    }

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
                    boolean hasPreviousZoneEncounter = LastZoneNormalEncounter.lastZoneNormalEncounter != null && LastZoneNormalEncounter.lastZoneNormalEncounter.startsWith(zone.id + ":");
                    if (hasPreviousZoneEncounter) {
                        String encounterId = LastZoneNormalEncounter.lastZoneNormalEncounter.substring(zone.id.length() + 1);
                        encounters = encounters.stream().filter(e -> !e.getID().equals(encounterId)).collect(Collectors.toList());
                    }

                    if (EncounterModifierPatchFields.loadingPostCombat.get(AbstractDungeon.getCurrMapNode()) && hasPreviousZoneEncounter) {
                        String encounterId = LastZoneNormalEncounter.lastZoneNormalEncounter.substring(zone.id.length() + 1);
                        EncounterModifyingZone.ZoneEncounter ze = z.getNormalEncounters().stream().filter(e -> e.getID().equals(encounterId)).collect(Collectors.toList()).get(0);
                        AbstractDungeon.lastCombatMetricKey = ze.getID();
                        result = ze.getMonsterSupplier().get();
                    } else if (encounters.isEmpty()) {
                        EncounterModifierPatchFields.resetZoneNormalEncounter.set(AbstractDungeon.getCurrMapNode(), true);
                    } else {
                        EncounterModifyingZone.ZoneEncounter ze = encounters.get(AbstractDungeon.monsterRng.random(encounters.size() - 1));
                        AbstractDungeon.lastCombatMetricKey = ze.getID();
                        result = ze.getMonsterSupplier().get();
                        EncounterModifierPatchFields.isZoneNormalEncounter.set(AbstractDungeon.getCurrMapNode(), true);
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
                    boolean hasPreviousZoneEncounter = LastZoneEliteEncounter.lastZoneEliteEncounter != null && LastZoneEliteEncounter.lastZoneEliteEncounter.startsWith(zone.id + ":");
                    if (hasPreviousZoneEncounter) {
                        String encounterId = LastZoneEliteEncounter.lastZoneEliteEncounter.substring(zone.id.length() + 1);
                        encounters = encounters.stream().filter(e -> !e.getID().equals(encounterId)).collect(Collectors.toList());
                    }

                    if (EncounterModifierPatchFields.loadingPostCombat.get(AbstractDungeon.getCurrMapNode()) && hasPreviousZoneEncounter) {
                        String encounterId = LastZoneEliteEncounter.lastZoneEliteEncounter.substring(zone.id.length() + 1);
                        EncounterModifyingZone.ZoneEncounter ze = z.getEliteEncounters().stream().filter(e -> e.getID().equals(encounterId)).collect(Collectors.toList()).get(0);
                        AbstractDungeon.lastCombatMetricKey = ze.getID();
                        result = ze.getMonsterSupplier().get();
                    } else if (encounters.isEmpty()) {
                        EncounterModifierPatchFields.resetZoneEliteEncounter.set(AbstractDungeon.getCurrMapNode(), true);
                    } else {
                        EncounterModifyingZone.ZoneEncounter ze = encounters.get(AbstractDungeon.monsterRng.random(encounters.size() - 1));
                        AbstractDungeon.lastCombatMetricKey = ze.getID();
                        result = ze.getMonsterSupplier().get();
                        EncounterModifierPatchFields.isZoneEliteEncounter.set(AbstractDungeon.getCurrMapNode(), true);
                    }
                }

                addAdditionalMonsters((EncounterModifyingZone) zone, result);
                result = z.changeEncounter(result, AbstractDungeon.lastCombatMetricKey);
            }
            return result;
        }
    }

    @SpirePatch2(clz = AbstractRoom.class, method = "endBattle")
    public static class RecordLastZoneCombatPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void recordLastZoneCombat() {
            if (Wiz.getCurZone() instanceof EncounterModifyingZone) {
                if (EncounterModifierPatchFields.isZoneNormalEncounter.get(AbstractDungeon.getCurrMapNode())) {
                    LastZoneNormalEncounter.lastZoneNormalEncounter = Wiz.getCurZone().id + ":" + AbstractDungeon.lastCombatMetricKey;
                }
                if (EncounterModifierPatchFields.isZoneEliteEncounter.get(AbstractDungeon.getCurrMapNode())) {
                    LastZoneEliteEncounter.lastZoneEliteEncounter = Wiz.getCurZone().id + ":" + AbstractDungeon.lastCombatMetricKey;
                }
                if (EncounterModifierPatchFields.resetZoneNormalEncounter.get(AbstractDungeon.getCurrMapNode())) {
                    LastZoneNormalEncounter.lastZoneNormalEncounter = null;
                }
                if (EncounterModifierPatchFields.resetZoneEliteEncounter.get(AbstractDungeon.getCurrMapNode())) {
                    LastZoneEliteEncounter.lastZoneEliteEncounter = null;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "endBattleTimer");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
    public static class TrackLoadingPostCombatPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void trackLoadingPostCombatBefore(SaveFile saveFile) {
            if (CardCrawlGame.loadingSave && saveFile != null && saveFile.post_combat) {
                EncounterModifierPatchFields.loadingPostCombat.set(AbstractDungeon.getCurrMapNode(), true);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "onPlayerEntry");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        @SpirePostfixPatch
        public static void trackLoadingPostCombatAfter(SaveFile saveFile) {
            EncounterModifierPatchFields.loadingPostCombat.set(AbstractDungeon.getCurrMapNode(), false);
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
            LastZoneEliteEncounter.lastZoneEliteEncounter = s;
        }
    }
}
