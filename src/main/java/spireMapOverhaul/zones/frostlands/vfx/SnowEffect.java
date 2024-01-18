package spireMapOverhaul.zones.frostlands.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import spireMapOverhaul.util.TexLoader;

import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;

public class SnowEffect extends AbstractGameEffect {

    // Settings

    // Textures.
    private static final Texture texture = TexLoader.getTexture(makeImagePath("ui/Frostlands/Snowflake.png"));

    private Texture TextureUsed;
    private Texture FinalTexture;

    // Duration.
    private float startingDuration;
    private float duration;

    // Position
    private float x;
    private float y;

    // Where go
    private float speed;
    private float direction = 270.0F;

    // How move
    private float rotation;
    private float rotation_speed;
    private float flip_speed;
    private float flip_counter;
    private float scale;
    private float totalscale;

    public SnowEffect() {
        int randomtex = MathUtils.random(0, 2);

        this.TextureUsed = texture;

        this.FinalTexture = this.TextureUsed;
        this.startingDuration = MathUtils.random(3.0F, 7.0F);
        this.duration = this.startingDuration;
        this.startingDuration = this.duration;
        this.renderBehind = true;
        this.rotation = MathUtils.random(0.0F, 360.0F);
        this.rotation_speed = MathUtils.random(-24.0F, 24.0F) * Settings.scale;
        this.flip_speed = MathUtils.random(-2.0F, 2.0F) * Settings.scale;
        this.flip_counter = MathUtils.random(0.0F, 6.4F);
        this.speed = MathUtils.random(200.0F, 300.0F) * Settings.scale;
        this.scale = MathUtils.random(-1.0F, 1.0F) * Settings.scale;
        this.totalscale = MathUtils.random(0.7F, 1.0F);
        if (MathUtils.random(0, 4) == 0 && this.totalscale >= 0.9F)
            this.totalscale = MathUtils.random(0.9F, 2F);


        // Location
        this.y = Settings.HEIGHT + 40.0F * Settings.scale;
        this.x = MathUtils.random(0.0F, Settings.WIDTH);

        this.color = new Color(1, 1, 1, 1F);
    }

    @Override
    public void render(SpriteBatch sb) {
        this.color.a = this.duration / this.startingDuration;
        sb.setColor(this.color);

        final int w = this.FinalTexture.getWidth();
        final int h = this.FinalTexture.getHeight();
        final int w2 = this.FinalTexture.getWidth();
        final int h2 = this.FinalTexture.getHeight();
        sb.draw(this.FinalTexture, x - w2 / 2f, y - h2 / 2f,
                w / 2f, h / 2f,
                w2, h2,
                (float) (this.scale) * Settings.scale * this.totalscale, 1.0F * Settings.scale * this.totalscale,
                this.rotation,
                0, 0,
                w2, h2,
                false, false);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void update() {
        final float dt = Gdx.graphics.getDeltaTime();

        this.y += MathUtils.sinDeg(this.direction) * this.speed * dt;
        this.x += MathUtils.cosDeg(this.direction) * this.speed * dt;

        this.rotation += this.rotation_speed * dt;
        this.flip_counter += this.flip_speed * dt;

        this.scale = MathUtils.sin(this.flip_counter);

        if (this.scale > 0.0F) {
            this.FinalTexture = this.TextureUsed;
        } else
            this.FinalTexture = texture;

        this.duration -= dt;
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }
}
