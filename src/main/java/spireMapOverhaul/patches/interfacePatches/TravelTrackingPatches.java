package spireMapOverhaul.patches.interfacePatches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.zoneInterfaces.OnTravelZone;

import static spireMapOverhaul.SpireAnniversary6Mod.logger;
import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class TravelTrackingPatches {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = SpirePatch.CLASS
    )
    public static class Field {
        public static final String ID = makeID("LastZone");
        public static SpireField<String> lastZoneID = new SpireField<>(()->"");

        public static String lastZoneID() {
            return AbstractDungeon.player == null ? "" : lastZoneID.get(AbstractDungeon.player);
        }
        public static void setLastZoneID(String newID) {
            if (AbstractDungeon.player != null)
                lastZoneID.set(AbstractDungeon.player, newID);
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "nextRoomTransition",
            paramtypez = { SaveFile.class }
    )
    public static class NextRoomTravel {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void onEnterNextRoom(AbstractDungeon __instance, SaveFile file) {
            AbstractZone nextZone;
            if (AbstractDungeon.nextRoom == null ||
                    (nextZone = ZonePatches.Fields.zone.get(AbstractDungeon.nextRoom)) == null) {
                leftZone(); //next room does not have a zone
                return;
            }

            if (nextZone instanceof OnTravelZone) {
                ((OnTravelZone) nextZone).onEnterRoom();
            }

            //Next room has a zone, check if it's a new one.
            String lastZoneID = Field.lastZoneID();
            if (!nextZone.id.equals(lastZoneID)) {
                //changing zones
                leftZone();
                logger.info("Entered zone " + nextZone.id);
                if (nextZone instanceof OnTravelZone) {
                    ((OnTravelZone) nextZone).onEnter();
                }
                if (file == null) {
                    //Not loading a save, can add entering the zone to metrics or something
                }
                Field.setLastZoneID(nextZone.id);
            }
        }

        private static void leftZone() {
            String lastZoneID = Field.lastZoneID();

            if (lastZoneID.isEmpty()) return;

            for (AbstractZone zone : BetterMapGenerator.getActiveZones(AbstractDungeon.map)) {
                if (zone.id.equals(lastZoneID)) {
                    if (zone instanceof OnTravelZone) {
                        ((OnTravelZone) zone).onExit();
                    }
                    logger.info("Left zone " + lastZoneID);
                    Field.setLastZoneID("");
                    return;
                }
            }

            logger.warn("Left zone " + lastZoneID + " but could not find in active zones.");
            Field.setLastZoneID("");
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
