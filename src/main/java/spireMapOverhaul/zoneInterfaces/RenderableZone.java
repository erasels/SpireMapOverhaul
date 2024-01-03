package spireMapOverhaul.zoneInterfaces;

public interface RenderableZone {
    default void renderBackground() {}
    default void renderForeground() {}
    default void update() {}
}
