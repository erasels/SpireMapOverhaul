package spireMapOverhaul.zoneInterfaces;

import com.megacrit.cardcrawl.events.AbstractEvent;

public interface ModifiedEventRateZone {
    /**
     * @return If not null, forces the event rolled to be this. Recommended to use with y-position based checks,
     */
    default AbstractEvent forceEvent() { return null; }

    /**
     * Provides a chance to guarantee a zone-specific event. Will do nothing if the zone has no events or if all events have been seen.
     * Not required for zone specific events, only for giving them an increased appearance rate.
     * @return Chance of guaranteeing a zone-specific event from 0 to 1.
     */
    default float zoneSpecificEventRate() { return 0; }
}
