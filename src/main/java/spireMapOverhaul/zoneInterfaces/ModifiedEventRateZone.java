package spireMapOverhaul.zoneInterfaces;

import basemod.eventUtil.EventUtils;
import basemod.patches.com.megacrit.cardcrawl.characters.AbstractPlayer.SeenEvents;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.Set;

public interface ModifiedEventRateZone {
    /**
     * @return If not null, forces the event rolled to be this. Recommended to use with y-position based checks,
     */
    default String forceEvent() { return null; }

    static String returnIfUnseen(String ID) {
        if (!SeenEvents.seenEvents.get(AbstractDungeon.player).contains(ID)) {
            if(!EventUtils.eventIDs.contains(ID)) {
                SpireAnniversary6Mod.logger.error("Failed to find event " + ID);
            } else {
                return ID;
            }
        }
        return null;
    }

    /**
     * Provides a chance to guarantee a zone-specific event. Will do nothing if the zone has no events or if all events have been seen.
     * Not required for zone specific events, only for giving them an increased appearance rate.
     * @return Chance of guaranteeing a zone-specific event from 0 to 1.
     */
    default float zoneSpecificEventRate() { return 0; }

    /**
     * Method to add additional events to your zone that can spawn when the zoneSpecificEventRate roll succeeds.
     * Only use this for adding events that you haven't made yourself/aren't in your Zone package.
     * @return a set of strings that will be added
     */
    default Set<String> addSpecificEvents() {return null;}
}
