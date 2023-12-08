package spireMapOverhaul.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;

import java.util.ArrayList;
import java.util.Set;

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
                AbstractEvent ret = ((ModifiedEventRateZone) current).forceEvent();
                if (ret != null) return SpireReturn.Return(ret);

                Set<String> zoneEvents = SpireAnniversary6Mod.zoneEvents.get(current.id);
                if (zoneEvents == null || zoneEvents.isEmpty() || AbstractDungeon.eventList.isEmpty())
                    return SpireReturn.Continue();

                if (rng.randomBoolean(((ModifiedEventRateZone) current).zoneSpecificEventRate())) {
                    RigRoll.validEvents = zoneEvents;
                    ret = AbstractDungeon.getEvent(rng);
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
                tmp.removeIf((e) -> !validEvents.contains(e));
                if (tmp.isEmpty()) {
                    SpireAnniversary6Mod.logger.info("Tried to force zone event, but no events found");
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
