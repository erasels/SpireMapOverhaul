/*
    Directly copied from decompiled base game's GainGoldTextEffect with only a few minor changes:
    - effect reads "-X Gold" instead of "+X Gold"
    - text is red
    - can be applied to monsters
 */
package spireMapOverhaul.zones.windy.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class LoseGoldTextEffect extends AbstractGameEffect {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static int totalGold;
    private int gold = 0;
    private boolean reachedCenter = false;
    private final float x;
    private float y;
    private final float destinationY;
    private static final float WAIT_TIME = 1.0F;
    private float waitTimer = 1.0F;
    private float fadeTimer = 1.0F;
    private static final float FADE_Y_SPEED;
    private static final float TEXT_DURATION = 3.0F;

    public LoseGoldTextEffect(int startingAmount, AbstractCreature c) {
        this.x = c.hb.cX;
        this.y = c.hb.cY;
        this.destinationY = this.y + 150.0F * Settings.scale;
        this.duration = 3.0F;
        this.startingDuration = 3.0F;
        this.reachedCenter = false;
        this.gold = startingAmount;
        totalGold = startingAmount;
        this.color = Color.RED.cpy();
    }

    public void update() {
        if (this.waitTimer > 0.0F) {
            this.gold = totalGold;
            if (!this.reachedCenter && this.y != this.destinationY) {
                this.y = MathUtils.lerp(this.y, this.destinationY, Gdx.graphics.getDeltaTime() * 9.0F);
                if (Math.abs(this.y - this.destinationY) < Settings.UI_SNAP_THRESHOLD) {
                    this.y = this.destinationY;
                    this.reachedCenter = true;
                }
            } else {
                this.waitTimer -= Gdx.graphics.getDeltaTime();
                if (this.waitTimer < 0.0F) {
                    this.gold = totalGold;
                } else {
                    this.gold = totalGold;
                }
            }
        } else {
            this.y += Gdx.graphics.getDeltaTime() * FADE_Y_SPEED;
            this.fadeTimer -= Gdx.graphics.getDeltaTime();
            this.color.a = this.fadeTimer;
            if (this.fadeTimer < 0.0F) {
                this.isDone = true;
            }
        }

    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, "- " + Integer.toString(this.gold) + TEXT[0], this.x, this.y, this.color);
        }

    }

    public void dispose() {
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("GainGoldTextEffect");
        TEXT = uiStrings.TEXT;
        totalGold = 0;
        FADE_Y_SPEED = 100.0F * Settings.scale;
    }
}
