package spireMapOverhaul.zones.gremlinTown.events.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;
import static spireMapOverhaul.util.Wiz.adp;

public class Shell {
    private static final int RAW_W = 64;
    private static final float FIRE_RATIO =  52.0F/-112.0F;;
    public Hitbox hb;
    private static Texture img;
    public float rotation;
    public boolean hide;
    private float animTimer;
    private float flightTime;
    private float gravity;
    private float startVelY;
    private float vel_x;

    private final float START_X;
    private final float END_X;
    private final float START_Y;
    private final float END_Y;

    
    public Shell(float startX, float startY, float endX, float endY, float flightTime) {
        hb = new Hitbox(startX, startY, RAW_W*Settings.scale, RAW_W*Settings.scale);
        animTimer = flightTime;
        hide = true;
        START_X = startX;
        START_Y = startY;
        END_X = endX;
        END_Y = endY;
        this.flightTime = flightTime;
        rotation = (float)Math.atan(FIRE_RATIO);
        calculateFlightParameters();
    }

    static {
        img = new Texture(makeImagePath("events/GremlinTown/Shell.png"));
    }

    public void update() {
        if (animTimer >= 0.0F) {
            float delta = Gdx.graphics.getDeltaTime();
            animTimer -= delta;
            float t = flightTime - animTimer;
            float y = START_Y + startVelY*t + gravity*t*t/2.0F;
            float x = START_X + t/flightTime * (END_X - START_X);
            hb.move(x, y);
            float vel_y = startVelY + gravity*t;
            rotation = (float)((180.0F / Math.PI) * Math.atan2(vel_y, vel_x));
            hide = false;
        } else if (animTimer < 0F && !hide) {
            AbstractDungeon.effectsQueue.add(new SmokeBombEffect(END_X, adp().hb.cY));
            hide = true;
        }
        hb.update();
    }

    private void calculateFlightParameters() {
        vel_x = (END_X - START_X) / flightTime;
        startVelY = vel_x * FIRE_RATIO;
        gravity = 2 * ((END_Y - START_Y)/(flightTime*flightTime) - startVelY / flightTime);
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
