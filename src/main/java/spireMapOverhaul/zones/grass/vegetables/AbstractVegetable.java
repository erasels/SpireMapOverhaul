package spireMapOverhaul.zones.grass.vegetables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.SmokePuffEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.actions.CallbackAction;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.grass.GrassZone;
import spireMapOverhaul.zones.grass.actions.ThrowVegetableAction;
import spireMapOverhaul.zones.grass.actions.ThrowVegetableEffect;

import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class AbstractVegetable {
    private static final float SIZE = Settings.scale * AbstractRelic.RAW_W;
    private static final float SPAWN_START = 0.40f;
    private static final float FLASH_SIZE = 2.5f;
    private static Texture GRASS;
    private static final UIStrings STRINGS = CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID(GrassZone.ID));
    protected static final String GRASS_IMAGE = SpireAnniversary6Mod.makeImagePath("ui/Grass/Grass.png");
    private boolean pulled;
    private boolean clickable;
    private float alpha;
    private float flash;
    private float scale;
    private float targetScale;
    protected Hitbox hb;
    protected Texture image;
    protected Color color;
    protected PowerTip mainTip;
    public final ArrayList<PowerTip> tips;
    public final AbstractVegetableData data;
    public int level;

    public AbstractVegetable(AbstractVegetableData data) {
        this.image = getGrassTexture();

        this.data = data;
        this.color = Color.WHITE.cpy();

        this.tips = new ArrayList<>();
        mainTip = new PowerTip(data.strings.NAME, getBodyText());
        this.tips.add(mainTip);

        float sc = getScaleMult();
        targetScale = scale = Settings.scale * sc;
        this.hb = new Hitbox(sc * SIZE, sc * SIZE);
        clickable = true;
    }

    protected static Texture getGrassTexture() {
        if (GRASS == null) {
            GRASS = TexLoader.getTexture(GRASS_IMAGE);
        }
        return GRASS;
    }

    public boolean canUpgrade() {
        return level < data.maxUpgradeLevel;
    }

    protected String getBodyText() {
        StringJoiner sj = new StringJoiner(" NL ");
        sj.add(STRINGS.TEXT[4] + level + "/" + data.maxUpgradeLevel);
        if (level > 0) {
            sj.add(getDescription());
            sj.add(STRINGS.TEXT[5]);
        } else {
            int levelHack = level;
            level = 1;
            sj.add(getDescription());
            level = levelHack;
            sj.add(STRINGS.TEXT[6]);
        }
        return sj.toString();
    }

    public String getDescription() {
        return data.strings.DESCRIPTION[0]
                .replace("{0}", String.valueOf(getEffectAmount()))
                .replace("{1}", String.valueOf(getHits()));
    }

    public int getHits() {
        return 1;
    }

    protected float getScaleMult() {
        return 0.45f + 0.15f * level;
    }

    public AbstractCreature getTarget() {
        return Wiz.getRandomEnemy();
    }

    public boolean isClickable() {
        return clickable && level > 0;
    }

    public boolean isPulled() {
        return pulled;
    }

    public AbstractGameAction launch() {
        AbstractDungeon.effectsQueue.add(new SmokePuffEffect(hb.cX, hb.cY));
        AbstractCreature randomMonster = getTarget();
        ThrowVegetableAction action = new ThrowVegetableAction(this, randomMonster, getHits());
        Wiz.att(action);
        Wiz.att(new VFXAction(new ThrowVegetableEffect(this, hb.cX, hb.cY, randomMonster.hb.cX, randomMonster.hb.cY), 0.4F));
        pulled = true;
        return action;
    }

    public void onClick() {
        clickable = false;
        Wiz.atb(CallbackAction.wait((__) -> launch()));
    }

    public void onSpawn(int size) {
        hb.translate(Settings.WIDTH * (SPAWN_START + size * 0.05f), AbstractDungeon.player != null ? AbstractDungeon.player.hb.y + Settings.scale * MathUtils.random(-20, 20) : Settings.HEIGHT * MathUtils.random(0.2f, 0.3f));
    }

    public void render(SpriteBatch sb) {
        color.a = alpha;
        sb.setColor(color);
        int w = image.getWidth();
        int h = image.getHeight();
        float halfWidth = w / 2.0F;
        float halfHeight = h / 2.0F;
        sb.draw(this.image, hb.x, hb.y, 0, 0, w, h, scale, scale, 0, 0, 0, w, h, false, false);
        if (flash > 1) {
            float fScale = FLASH_SIZE / flash * scale;
            color.a = MathUtils.lerp(0, 1, flash - 1);
            sb.setColor(color);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            sb.draw(this.image, hb.x, hb.y, halfWidth, halfHeight, w, h, fScale, fScale, 0, 0, 0, w, h, false, false);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
        hb.render(sb);
        if (hb.hovered && !AbstractDungeon.isScreenUp && !CardCrawlGame.isPopupOpen) {
            TipHelper.queuePowerTips(hb.x, hb.y, tips);
        }
    }

    public void update() {
        hb.update();
        if (this.hb.hovered && InputHelper.justClickedLeft && Wiz.canAcceptInput() && isClickable()) {
            this.onClick();
        }
        if (alpha < 1) {
            alpha = MathUtils.lerp(alpha, 1, Gdx.graphics.getDeltaTime() * 10);
            if (alpha > 1) {
                alpha = 1;
            }
        }
        if (flash > 1) {
            flash -= Gdx.graphics.getDeltaTime() * FLASH_SIZE;
        }
        if (scale < targetScale) {
            scale += Gdx.graphics.getDeltaTime() * FLASH_SIZE;
        }
    }

    public void upgrade(int amount) {
        level = Math.min(data.maxUpgradeLevel, level + amount);
        mainTip.body = getBodyText();
        float sc = getScaleMult();
        targetScale = Settings.scale * sc;
        hb.resize(sc * SIZE, sc * SIZE);
        flash = FLASH_SIZE;
    }

    public abstract int getEffectAmount();

    public abstract AbstractGameAction getHitAction(AbstractCreature target);
}
