package spireMapOverhaul.zones.grass.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zones.grass.vegetables.AbstractVegetable;

import java.util.ArrayList;

public class ThrowVegetableEffect extends AbstractGameEffect {
    private static final int MAX_POS = 10;
    private static Texture img;
    private final ArrayList<Vector2> previousPos = new ArrayList<>();
    private final float sX;
    private final float sY;
    private final float dX;
    private final float dY;
    private final float bounceHeight;
    private float cX;
    private float cY;
    private float vR;
    private float yOffset;
    private boolean playedSfx = false;

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
        this.duration = vegetable.data.duration;
        this.color = Color.WHITE.cpy();
        if (this.sY > this.dY) {
            this.bounceHeight = vegetable.data.bounce * Settings.scale;
        } else {
            this.bounceHeight = this.dY - this.sY + vegetable.data.bounce * Settings.scale;
        }
        if (this.dX > this.sX) {
            this.vR = -vegetable.data.rotation;
        }
        else {
            this.vR = vegetable.data.rotation;
        }
    }

    public void update() {
        if (!this.playedSfx) {
            this.playedSfx = true;
            CardCrawlGame.sound.playA("BLUNT_FAST", MathUtils.random(-0.3F, -0.2F));
        }

        this.cX = Interpolation.linear.apply(this.dX, this.sX, this.duration / 0.6F);
        this.cY = Interpolation.linear.apply(this.dY, this.sY, this.duration / 0.6F);
        this.previousPos.add(new Vector2(this.cX, this.cY + + this.yOffset));
        if (this.previousPos.size() > MAX_POS) {
            this.previousPos.remove(this.previousPos.get(0));
        }

        this.rotation += Gdx.graphics.getDeltaTime() * vR;

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

        sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for(int i = 0; i < this.previousPos.size(); i++) {
            this.color.a = 0.1f * i;
            sb.setColor(this.color);
            sb.draw(img, this.previousPos.get(i).x, this.previousPos.get(i).y, width / 2.0F, height / 2.0F, width, height, this.scale * i / 10f, this.scale * i / 10f, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
        }
        this.color.a = 1;
        sb.setColor(this.color);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sb.draw(img, cX, cY + this.yOffset, width / 2.0F, height / 2.0F, width, height, this.scale, this.scale, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
    }

    @Override
    public void dispose() {

    }
}
