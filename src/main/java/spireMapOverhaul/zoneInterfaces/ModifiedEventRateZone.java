package spireMapOverhaul.zoneInterfaces;

public interface ModifiedEventRateZone {
    float zoneSpecificEventRate(); //Chance of guaranteeing an event tied to this zone
    //Not required to have zone specific events; only if you want to give them an increased appearance rate.
}
