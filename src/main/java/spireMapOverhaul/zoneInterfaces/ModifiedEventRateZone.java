package spireMapOverhaul.zoneInterfaces;

import basemod.eventUtil.EventUtils;
import basemod.patches.com.megacrit.cardcrawl.characters.AbstractPlayer.SeenEvents;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import spireMapOverhaul.SpireAnniversary6Mod;

public interface ModifiedEventRateZone {
    /**
     * @return If not null, forces the event rolled to be this. Recommended to use with y-position based checks,
     */
    default AbstractEvent forceEvent() { return null; }

    static AbstractEvent returnIfUnseen(String ID) {
        if (!SeenEvents.seenEvents.get(AbstractDungeon.player).contains(ID)) {
            AbstractEvent e = EventUtils.getEvent(ID);
            if (e == null) {
                e = EventHelper.getEvent(ID);
                if (e == null) {
                    SpireAnniversary6Mod.logger.info("Failed to get event " + ID);
                }
            }
            return e;
        }
        return null;
    }

    /**
     * Provides a chance to guarantee a zone-specific event. Will do nothing if the zone has no events or if all events have been seen.
     * Not required for zone specific events, only for giving them an increased appearance rate.
     * @return Chance of guaranteeing a zone-specific event from 0 to 1.
     */
    default float zoneSpecificEventRate() { return 0; }
}
