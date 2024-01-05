package spireMapOverhaul.zones.gremlinTown.events.misc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;

public class Shell {
    private static final int RAW_W = 128;
    public Hitbox hb;
    private static Texture img;
    public float rotation;
    public boolean hide;
    
    public Shell(float x, float y, float rot) {
        hb = new Hitbox(x, y, RAW_W, RAW_W);
        hide = false;
    }

    static {
        img = new Texture("images/relics/test4.png");
    }

    public void update() {
        hb.update();
    }

    public void render(SpriteBatch sb) {
        if (hide)
            return;

        sb.setColor(Color.WHITE);

        sb.draw(img, hb.cX - RAW_W/2.0F, hb.cY - RAW_W/2.0F, RAW_W/2.0F, RAW_W/2.0F, RAW_W, RAW_W,
                Settings.scale, Settings.scale, rotation, 0, 0, RAW_W, RAW_W, false, false);

        hb.render(sb);
    }
}
