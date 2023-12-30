package spireMapOverhaul.patches;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryPath;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.screens.stats.RunData;
import javassist.*;
import org.apache.logging.log4j.Logger;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ZonePerFloorRunHistoryPatch {
    private static final Logger logger = SpireAnniversary6Mod.logger;
    private static final String ZONE_TEXT = CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID("RunHistoryScreen")).TEXT[0];

    @SpirePatch(clz = CardCrawlGame.class, method = SpirePatch.CONSTRUCTOR)
    public static class ZonePerFloorField {
        @SpireRawPatch
        public static void addZonePerFloor(CtBehavior ctBehavior) throws NotFoundException, CannotCompileException {
            CtClass runData = ctBehavior.getDeclaringClass().getClassPool().get(RunData.class.getName());

            String fieldSource = "public java.util.List zone_per_floor;";

            CtField field = CtField.make(fieldSource, runData);

            runData.addField(field);
        }
    }

    @SpirePatch(clz = Metrics.class, method = "gatherAllData")
    public static class GatherAllDataPatch {
        @SpirePostfixPatch
        public static void gatherAllDataPatch(Metrics __instance, boolean death, boolean trueVictor, MonsterGroup monsters) {
            if (death) {
                AbstractZone zone = Wiz.getCurZone();
                String zoneID = zone == null ? null : zone.id;
                ZonePerFloorLog.zonePerFloorLog.add(zoneID);
            }
            ReflectionHacks.privateMethod(Metrics.class, "addData", Object.class, Object.class)
                    .invoke(__instance, "zone_per_floor", ZonePerFloorLog.zonePerFloorLog);
        }
    }

    @SpirePatch(clz = RunPathElement.class, method = SpirePatch.CLASS)
    public static class ZoneField {
        public static final SpireField<String> zone = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = RunHistoryPath.class, method = "setRunData")
    public static class AddZoneDataPatch {
        @SuppressWarnings("rawtypes")
        @SpireInsertPatch(locator = Locator.class, localvars = { "element", "i" })
        public static void addZoneData(RunHistoryPath __instance, RunData newData, RunPathElement element, int i) throws NoSuchFieldException, IllegalAccessException {
            Field field = newData.getClass().getField("zone_per_floor");
            List zone_per_floor = (List)field.get(newData);
            if (zone_per_floor != null && i < zone_per_floor.size()) {
                Object zoneID = zone_per_floor.get(i);
                String s = null;
                if (zoneID instanceof String) {
                    s = (String)zoneID;
                }
                else if (zoneID != null) {
                    logger.warn("Unrecognized zone_per_floor data: " + zoneID);
                }
                ZoneField.zone.set(element, s);
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(RunPathElement.class);
                Matcher finalMatcher = new Matcher.MethodCallMatcher(List.class, "add");
                return LineFinder.findInOrder(ctMethodToPatch, Collections.singletonList(matcher), finalMatcher);
            }
        }
    }

    @SpirePatch(clz = RunPathElement.class, method = "getTipDescriptionText")
    public static class DisplayZoneDataPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "sb" })
        public static void displayZoneData(RunPathElement __instance, StringBuilder sb) {
            String zoneID = ZoneField.zone.get(__instance);
            if (zoneID != null) {
                Optional<AbstractZone> zone = SpireAnniversary6Mod.unfilteredAllZones.stream().filter(z -> z.id.equals(zoneID)).findFirst();
                if (sb.length() > 0) {
                    sb.append(" NL ");
                }
                String zoneName = zoneID;
                if (zone.isPresent()) {
                    zoneName = zone.get().name;
                }
                sb.append(FontHelper.colorString(ZONE_TEXT.replace("{0}", zoneName), "b"));
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(RunPathElement.class, "eventStats");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "incrementFloorBasedMetrics")
    public static class ZonePerFloorAddLoggingPatch {
        @SpirePostfixPatch
        public static void incrementFloorBasedMetricsPatch(AbstractDungeon __instance) {
            if (AbstractDungeon.floorNum != 0) {
                AbstractZone zone = Wiz.getCurZone();
                String zoneID = zone == null ? null : zone.id;
                ZonePerFloorLog.zonePerFloorLog.add(zoneID);
            }
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "generateSeeds")
    public static class InitializeZonePerFloorPatch {
        @SpirePostfixPatch
        public static void initializeZonePerFloor() {
            ZonePerFloorLog.zonePerFloorLog = new ArrayList<>();
        }
    }

    public static class ZonePerFloorLog implements CustomSavable<ArrayList<String>> {
        public final static String SaveKey = "ZonePerFloor";

        public static ArrayList<String> zonePerFloorLog;

        @Override
        public ArrayList<String> onSave() {
            return ZonePerFloorLog.zonePerFloorLog;
        }

        @Override
        public void onLoad(ArrayList<String> list) {
            ZonePerFloorLog.zonePerFloorLog = list != null ? list : new ArrayList<>();
        }
    }
}
