package spireMapOverhaul.patches;

import basemod.eventUtil.EventUtils;
import basemod.patches.com.megacrit.cardcrawl.characters.AbstractPlayer.SeenEvents;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EventGenPatch {
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "generateEvent"
    )
    public static class ModifyGen {
        @SpirePrefixPatch
        public static SpireReturn<AbstractEvent> modifyGen(Random rng) {
            AbstractZone current = ZonePatches.currentZone();
            if (current instanceof ModifiedEventRateZone) {
                String retID = ((ModifiedEventRateZone) current).forceEvent();
                if (retID != null) {
                    HashSet<String> seenEvents = SeenEvents.seenEvents.get(AbstractDungeon.player);
                    AbstractEvent e = EventUtils.getEvent(retID);
                    if (e == null) {
                        e = EventHelper.getEvent(retID);
                    }
                    if(e != null) {
                        seenEvents.add(retID);
                        return SpireReturn.Return(e);
                    } else {
                        SpireAnniversary6Mod.logger.error("Failed to get event " + retID);
                    }
                }

                Set<String> zoneEvents = SpireAnniversary6Mod.zoneEvents.get(current.id);
                if (zoneEvents == null || zoneEvents.isEmpty() || AbstractDungeon.eventList.isEmpty())
                    return SpireReturn.Continue();

                if (rng.randomBoolean(((ModifiedEventRateZone) current).zoneSpecificEventRate())) {
                    RigRoll.validEvents = zoneEvents;
                    AbstractEvent ret = AbstractDungeon.getEvent(rng);
                    RigRoll.validEvents = null;
                    return SpireReturn.Return(ret);
                }
            }
            return SpireReturn.Continue();
        }
    }
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getEvent"
    )
    public static class RigRoll {
        protected static Set<String> validEvents = null;

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"tmp"}
        )
        public static void insert(Random rng, ArrayList<String> tmp) {
            if (validEvents != null) {
                if (validEvents.isEmpty()) {
                    SpireAnniversary6Mod.logger.info("Tried to force zone event, but no zone events defined. This is likely an error in zone setup.");
                }
                else {
                    ArrayList<String> unseenValidEvents = validEvents.stream().filter(eid -> !SeenEvents.seenEvents.get(AbstractDungeon.player).contains(eid)).collect(Collectors.toCollection(ArrayList::new));
                    if (!unseenValidEvents.isEmpty()) {
                        tmp.removeIf(e -> true);
                        tmp.addAll(unseenValidEvents);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
