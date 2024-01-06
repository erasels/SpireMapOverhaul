package spireMapOverhaul.zones.storm.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ImpactSparkEffect;

import static spireMapOverhaul.SpireAnniversary6Mod.modID;

public class AlwaysBehindLightningEffect extends AbstractGameEffect {
    private float x;
    private float y;
    public static Texture LIGHTNING = new Texture(modID + "Resources/images/vfx/lightning.png");


    public AlwaysBehindLightningEffect(float x, float y, boolean renderBehind) {
        this.x = x;
        this.y = y;
        this.color = new Color(1.0f, 251.0f/255.0f, 139.0f/255.0f, 1.0f);
        this.duration = 0.5F;
        this.startingDuration = 0.5F;
        this.renderBehind = renderBehind;
    }

    public void update() {
        if (duration == startingDuration) {
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.MED, false);
            for (int i = 0; i < 15; i++)
                AbstractDungeon.topLevelEffectsQueue.add(new ImpactSparkEffect(x +

                        MathUtils.random(-20.0F, 20.0F) * Settings.scale + 150.0F * Settings.scale, y +
                        MathUtils.random(-20.0F, 20.0F) * Settings.scale));
        }
        duration -= Gdx.graphics.getRawDeltaTime();
        if (duration < 0.0F)
            isDone = true;
        color.a = Interpolation.bounceIn.apply(duration * 2.0F);
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(color);
        sb.draw(LIGHTNING, x, y);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {}
}
