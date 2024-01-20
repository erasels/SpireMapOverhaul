package spireMapOverhaul.zones.gremlincamp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EmberEffect extends AbstractGameEffect {
    private float x, y, vX, vY;
    private float startDur, targetScale;
    private TextureAtlas.AtlasRegion img;
    private float rotateSpeed = 0f;

    public EmberEffect(float x, float y) {
        switch (MathUtils.random(2)) {
            case 0:
                color = Color.CORAL.cpy();
                break;
            case 1:
                color = Color.ORANGE.cpy();
                break;
            case 2:
                color = Color.SCARLET.cpy();
                break;
        }

        duration = MathUtils.random(0.6f, 1.4f);
        duration *= duration;
        targetScale = MathUtils.random(0.3f, 0.6f);
        startDur = duration;

        vX = MathUtils.random(-20f * Settings.scale, 20f * Settings.scale);
        vY = MathUtils.random(1 * Settings.scale, 50f * Settings.scale);
        this.x = x;
        this.y = y;
        this.scale = 0.01f;
        img = setImg();
        rotateSpeed = MathUtils.random(-200f, 200f);
    }

    private TextureAtlas.AtlasRegion setImg() {
        switch (MathUtils.random(0, 5)) {
            case 0:
                return ImageMaster.DUST_1;
            case 1:
                return ImageMaster.DUST_2;
            case 2:
                return ImageMaster.DUST_3;
            case 3:
                return ImageMaster.DUST_4;
            case 4:
                return ImageMaster.DUST_5;
            default:
                return ImageMaster.DUST_6;
        }
    }

    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0f) {
            isDone = true;
        }
        x += vX * Gdx.graphics.getDeltaTime();
        y += vY * Gdx.graphics.getDeltaTime();

        rotation += rotateSpeed * Gdx.graphics.getDeltaTime();
        scale = Interpolation.swing.apply(0.01f, targetScale, 1f - duration / startDur);

        if (duration < 0.5f) {
            color.a = duration * 2f;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(img, x, y, img.offsetX, img.offsetY, img.packedWidth, img.packedHeight, scale, scale, rotation);
        sb.setColor(new Color(color.r, color.g, color.b, color.a / 3f));
        sb.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE); // Additive Mode
        sb.draw(img, x, y, img.offsetX, img.offsetY, img.packedWidth, img.packedHeight, scale, scale, rotation);
        sb.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA); // NORMAL
    }

    @Override
    public void dispose() {
    }
}

