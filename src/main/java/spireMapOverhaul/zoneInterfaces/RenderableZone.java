package spireMapOverhaul.zoneInterfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface RenderableZone {
    default void renderBackground(SpriteBatch sb) {}
    default void postRenderBackground(SpriteBatch sb) {}
    default void renderForeground(SpriteBatch sb) {}
    default void update() {}
}
