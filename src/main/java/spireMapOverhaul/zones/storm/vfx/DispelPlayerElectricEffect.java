package spireMapOverhaul.zones.storm.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.EmptyStanceParticleEffect;

public class DispelPlayerElectricEffect extends AbstractGameEffect {
    private int numParticles = 10;

    private final float x;

    private final float y;

    public DispelPlayerElectricEffect(float x, float y) {
        this.duration = 0.0F;
        this.x = x;
        this.y = y;
    }

    public void update() {
        duration -= Gdx.graphics.getRawDeltaTime();
        if (duration < 0.0F) {
            AbstractDungeon.effectsQueue.add(new EmptyStanceParticleEffect(x, y));
            AbstractDungeon.effectsQueue.add(new EmptyStanceParticleEffect(x, y));
            AbstractDungeon.effectsQueue.add(new EmptyStanceParticleEffect(x, y));
            numParticles--;
            if (numParticles <= 0)
                isDone = true;
        }
    }

    public void render(SpriteBatch sb) {}

    public void dispose() {}
}