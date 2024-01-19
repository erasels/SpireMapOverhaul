package spireMapOverhaul.zones.gremlinTown.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import spireMapOverhaul.zones.gremlinTown.events.Surprise;

import static spireMapOverhaul.util.Wiz.adRoom;


public class SurprisePatch {
    @SpirePatch(
            clz = AbstractRoom.class,
            method = "update"
    )
    public static class RoomPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractRoom __instance) {
            if (adRoom().event != null && adRoom().event instanceof Surprise && ((Surprise) adRoom().event).mimic)
                AbstractDungeon.overlayMenu.proceedButton.show();
        }

        public static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            @Override
            public int[] Locate(CtBehavior behavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "event");
                int[] x = LineFinder.findAllInOrder(behavior, matcher);
                return new int[]{x[3]};
            }
        }
    }

    @SpirePatch(
            clz = AbstractEvent.class,
            method = "renderRoomEventPanel"
    )
    public static class PanelPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractEvent __instance) {
            if (__instance instanceof Surprise)
                return SpireReturn.Return();
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = TipHelper.class,
            method = "renderTipBox"
    )
    public static class SpoilerPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix() {
            if (adRoom() != null && adRoom().event != null && adRoom().event instanceof Surprise && ((Surprise) adRoom().event).mimic)
                return SpireReturn.Return();
            return SpireReturn.Continue();
        }
    }
}
