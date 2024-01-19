/*
    Directly copied from decompiled base game's DustEffect, with fixed alphas and adjusted speed stuff.
 */
package spireMapOverhaul.zones.windy.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class WindyDustEffect extends AbstractGameEffect {
    private TextureAtlas.AtlasRegion img = getImg();

    private float x = MathUtils.random(-Settings.WIDTH/2, Settings.WIDTH/2);

    private float y = MathUtils.random(-100.0F, 600.0F) * Settings.scale + AbstractDungeon.floorY;

    private float vX = MathUtils.random(300.0F, 500.0F) * Settings.scale;

    private float vY = MathUtils.random(-12.0F, 30.0F) * Settings.scale;

    private float aV;

    private float baseAlpha;

    public WindyDustEffect(boolean extraFast) {
        this.startingDuration = MathUtils.random(6f, 10f);
        this.duration = this.startingDuration;
        this.img = this.getImg();
        this.scale = Settings.scale * MathUtils.random(0.3F, 0.9F);
        float colorTmp = MathUtils.random(0.4F, 0.8F);
        if(extraFast){
            this.vX *= 1.75f;
            this.startingDuration /= 1.75f;
            this.duration = this.startingDuration;
            this.scale *= 1.5f;
            colorTmp = MathUtils.random(0.0F, 0.2F);
        }
        this.color = new Color(colorTmp, colorTmp, colorTmp, 0.0F);
        this.baseAlpha = 1.0F - colorTmp;
        this.color.a = 0.0F;
        this.rotation = MathUtils.random(0.0F, 360.0F);
        this.aV = MathUtils.random(-120.0F, 120.0F);
    }

    private TextureAtlas.AtlasRegion getImg() {
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
        }
        return ImageMaster.DUST_6;
    }

    public void update() {
        this.rotation += this.aV * Gdx.graphics.getDeltaTime();
        this.duration -= Gdx.graphics.getDeltaTime();
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
            this.isDone = true;
        if (this.duration > this.startingDuration * 0.75f) {
            float tmp = 1 - (this.duration - this.startingDuration * 0.75f) / (this.startingDuration * 0.25f);
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, tmp) * this.baseAlpha;
        } else if (this.duration < this.startingDuration * 0.5f){
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / (this.startingDuration * 0.5F)) * this.baseAlpha;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw((TextureRegion)this.img, this.x, this.y, this.img.offsetX, this.img.offsetY, this.img.packedWidth, this.img.packedHeight, this.scale, this.scale, this.rotation);
    }

    public void dispose() {}
}
