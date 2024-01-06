package spireMapOverhaul.zoneInterfaces;

public interface OnTravelZone {
    default void onEnter() { }
    default void onExit() { }
    default void onEnterRoom() { }
}
