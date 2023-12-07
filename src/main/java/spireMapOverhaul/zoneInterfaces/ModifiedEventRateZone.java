package spireMapOverhaul.zoneInterfaces;

import com.megacrit.cardcrawl.events.AbstractEvent;

public interface ModifiedEventRateZone {
    /**
     * @return If not null, forces the event rolled to be this. Recommended to use with y-position based checks,
     */
    default AbstractEvent forceEvent() { return null; }
    default float zoneSpecificEventRate() { return 0; }; //Chance of guaranteeing an event tied to this zone
    //Not required to have zone specific events, only if you want to give them an increased appearance rate.
}
