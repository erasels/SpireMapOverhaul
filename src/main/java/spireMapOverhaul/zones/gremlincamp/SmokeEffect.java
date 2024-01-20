package spireMapOverhaul.zones.gremlincamp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SmokeEffect extends AbstractGameEffect {
    private float x, y, vY, aV;
    private float startDur, targetScale;
    private TextureAtlas.AtlasRegion img;

    public SmokeEffect(float x, float y) {
        color = new Color(0f, 0f, 0f, 1f);
        color.r = MathUtils.random(0.5f, 0.6f);
        color.g = color.r + MathUtils.random(0f, 0.2f);
        color.b = 0.2f;

        if (MathUtils.randomBoolean()) {
            img = ImageMaster.EXHAUST_L;
            duration = MathUtils.random(0.5f, 1f);
            targetScale = MathUtils.random(0.1f, 0.4f);
        } else {
            img = ImageMaster.EXHAUST_S;
            duration = MathUtils.random(0.5f, 1f);
            targetScale = MathUtils.random(0.2f, 0.5f);
        }

        startDur = duration;

        this.x = x;
        this.y = y;
        this.scale = 0.01f;
        rotation = MathUtils.random(360f);
        aV = MathUtils.random(-50f, 50f);
        vY = MathUtils.random(Settings.scale, 2f * Settings.scale);
    }

    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0f) {
            isDone = true;
        }
        x += MathUtils.random(-1f * Settings.scale, 1f * Settings.scale);
        x += vY;
        y += MathUtils.random(-1f * Settings.scale, 1f * Settings.scale);
        y += vY;
        rotation += aV * Gdx.graphics.getDeltaTime();
        scale = Interpolation.linear.apply(0.01f, targetScale, 1f - duration / startDur);

        if (duration < 0.33f) {
            color.a = duration * 3f;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(
                img,
                x,
                y,
                img.packedWidth / 2f,
                img.packedHeight / 2f,
                img.packedWidth,
                img.packedHeight,
                scale,
                scale,
                rotation);
    }

    @Override
    public void dispose() {

    }
}

