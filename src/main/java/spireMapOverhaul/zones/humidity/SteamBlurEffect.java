package spireMapOverhaul.zones.humidity;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.SmokeBlurEffect;

public class SteamBlurEffect extends SmokeBlurEffect {
    private final float HUMIDIBOT_SCALE=1.0f;

    private float x;
    private float y;
    private float vX;
    private float vY;
    private float aX;
    private float gravity;
    private float targetScale;
    private float startDur;
    private float alphaMultiplier;

    public SteamBlurEffect(float x, float y) {
        super(x,y);
        float brightness=MathUtils.random(0.75f,1.0f);
        this.color.r=brightness;
        this.color.g=brightness;
        this.color.b=brightness;
        this.vX=MathUtils.random(0.8f*Settings.scale,1.2f* Settings.scale);
        this.vY=MathUtils.random(0.8f*Settings.scale,1.2f* Settings.scale);
        this.aX=MathUtils.random(-0.5f*Settings.scale,0.0f*Settings.scale);
        this.gravity=MathUtils.random(-1*Settings.scale,-0.5f*Settings.scale);
        TextureAtlas.AtlasRegion img = ReflectionHacks.getPrivate(this, SmokeBlurEffect.class,"img");
        this.duration = MathUtils.random(2.0F, 2.5F);
        this.startDur=this.duration;
        this.targetScale=MathUtils.random(0.01f,0.2f);

        this.vX*=HUMIDIBOT_SCALE;
        this.vY*=HUMIDIBOT_SCALE;
        this.aX*=HUMIDIBOT_SCALE;
        this.gravity*=HUMIDIBOT_SCALE;
        this.scale*=HUMIDIBOT_SCALE;
        this.targetScale*=HUMIDIBOT_SCALE;


        this.x=x;
        this.y=y;
        this.x-=(float)img.packedWidth/2;
        this.y-=(float)img.packedHeight/2;
        this.alphaMultiplier=MathUtils.random(0.01f,0.05f);
        this.color.a=1f*alphaMultiplier;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        this.x += this.vX;
        this.vX+=this.aX*Gdx.graphics.getDeltaTime();
        if(this.vX<0)this.vX=0;
        this.y += this.vY;
        this.vY+=this.gravity*Gdx.graphics.getDeltaTime();
        float aV= ReflectionHacks.getPrivate(this, SmokeBlurEffect.class,"aV");
        this.rotation += aV * Gdx.graphics.getDeltaTime();
        this.scale = Interpolation.exp10Out.apply(0.01F, this.targetScale, 1.0F - this.duration / this.startDur);
        if (this.duration < 0.33F) {
            this.color.a = this.alphaMultiplier*this.duration * 3.0F/10;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        TextureAtlas.AtlasRegion img = ReflectionHacks.getPrivate(this, SmokeBlurEffect.class,"img");
        sb.draw(img, this.x, this.y, (float)img.packedWidth / 2.0F, (float)img.packedHeight / 2.0F, (float)img.packedWidth, (float)img.packedHeight, this.scale, this.scale, this.rotation);
    }

}
