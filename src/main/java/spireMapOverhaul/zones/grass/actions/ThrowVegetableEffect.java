package spireMapOverhaul.zones.grass.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zones.grass.vegetables.AbstractVegetable;
import spireMapOverhaul.zones.grass.vegetables.AbstractVegetableData;

import java.util.ArrayList;

public class ThrowVegetableEffect extends AbstractGameEffect {
    private static Texture img;
    private float sX;
    private float sY;
    private float cX;
    private float cY;
    private float dX;
    private float dY;
    private float yOffset;
    private float bounceHeight;
    private static final float DUR = 0.6F;
    private boolean playedSfx = false;
    private ArrayList<Vector2> previousPos = new ArrayList<>();

    public ThrowVegetableEffect(AbstractVegetable vegetable, float srcX, float srcY, float destX, float destY) {
        super();
        img = TexLoader.getTexture(vegetable.data.imagePath);
        this.sX = srcX;
        this.sY = srcY;
        this.cX = this.sX;
        this.cY = this.sY;
        this.dX = destX;
        this.dY = destY;
        this.rotation = 0.0F;
        this.duration = 0.6F;
        this.color = Color.WHITE.cpy();
        if (this.sY > this.dY) {
            this.bounceHeight = 400.0F * Settings.scale;
        } else {
            this.bounceHeight = this.dY - this.sY + 400.0F * Settings.scale;
        }
    }

    public void update() {
        if (!this.playedSfx) {
            this.playedSfx = true;
            if (MathUtils.randomBoolean()) {
                CardCrawlGame.sound.playA("POTION_DROP_1", MathUtils.random(-0.3F, -0.2F));
            } else {
                CardCrawlGame.sound.playA("POTION_DROP_2", MathUtils.random(-0.3F, -0.2F));
            }

            if (MathUtils.randomBoolean()) {
                CardCrawlGame.sound.play("POTION_1");
            } else {
                CardCrawlGame.sound.play("POTION_2");
            }
        }

        this.cX = Interpolation.linear.apply(this.dX, this.sX, this.duration / 0.6F);
        this.cY = Interpolation.linear.apply(this.dY, this.sY, this.duration / 0.6F);
        this.previousPos.add(new Vector2(this.cX, this.cY + + this.yOffset));
        if (this.previousPos.size() > 20) {
            this.previousPos.remove(this.previousPos.get(0));
        }

        if (this.dX > this.sX) {
            this.rotation -= Gdx.graphics.getDeltaTime() * 1000.0F;
        } else {
            this.rotation += Gdx.graphics.getDeltaTime() * 1000.0F;
        }

        if (this.duration > 0.3F) {
            this.yOffset = Interpolation.circleIn.apply(this.bounceHeight, 0.0F, (this.duration - 0.3F) / 0.3F) * Settings.scale;
        } else {
            this.yOffset = Interpolation.circleOut.apply(0.0F, this.bounceHeight, this.duration / 0.3F) * Settings.scale;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        float width = img.getWidth();
        float height = img.getHeight();

        sb.setColor(this.color);
        sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for(int i = 5; i < this.previousPos.size(); ++i) {
            sb.draw(img, this.previousPos.get(i).x, this.previousPos.get(i).y, width / 2.0F, height / 2.0F, width, height, this.scale * i / 40f, this.scale * i / 40f, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
        }
        sb.draw(img, cX, cY + this.yOffset, width / 2.0F, height / 2.0F, width, height, this.scale, this.scale, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void dispose() {

    }
}
