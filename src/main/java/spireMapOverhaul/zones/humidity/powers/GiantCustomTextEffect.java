package spireMapOverhaul.zones.humidity.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.combat.GiantTextEffect;

public class GiantCustomTextEffect extends GiantTextEffect {
    private final StringBuilder sBuilder = new StringBuilder();
    private int index;

    public GiantCustomTextEffect(float x, float y, String string) {
        super(x, y);
        ReflectionHacks.setPrivate(this, GiantTextEffect.class, "targetString", string);
    }

    public void render(SpriteBatch sb) {
        String targetString = ReflectionHacks.getPrivate(this, GiantTextEffect.class, "targetString");
        float x = ReflectionHacks.getPrivate(this, GiantTextEffect.class, "x");
        float y = ReflectionHacks.getPrivate(this, GiantTextEffect.class, "y");
        FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, targetString, x, y, this.color, 2.5F - this.duration / 4.0F + MathUtils.random(0.05F));
        sb.setBlendFunction(770, 1);
        FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, targetString, x, y, this.color, 0.05F + (2.5F - this.duration / 4.0F) + MathUtils.random(0.05F));
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
