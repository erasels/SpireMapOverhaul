package spireMapOverhaul.zones.humidity.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class FakeDivinityStancePower extends AbstractSMOPower implements InvisiblePower {
    public static String POWER_ID = SpireAnniversary6Mod.makeID("FakeDivinityStancePower");
    public static String NAME = "Divinity Stance";
    public static String ZONE_ID = HumidityZone.ID;
    protected float particleTimer;
    protected float particleTimer2;

    public FakeDivinityStancePower(AbstractCreature owner) {
        super(POWER_ID, NAME, ZONE_ID, PowerType.BUFF, false, owner, 0);
        particleTimer = 0.0F;
        particleTimer2 = 0.0F;
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 3.0F : damage;
    }

    @Override
    public void updateParticles() {
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.2F;
                DivinityParticleEffect dpe = new DivinityParticleEffect();
                float x = ReflectionHacks.getPrivate(dpe, DivinityParticleEffect.class, "x");
                float y = ReflectionHacks.getPrivate(dpe, DivinityParticleEffect.class, "y");
                x += (owner.hb.cX - Wiz.adp().hb.cX);
                y += (owner.hb.cY - Wiz.adp().hb.cY);
                ReflectionHacks.setPrivate(dpe, DivinityParticleEffect.class, "x", x);
                ReflectionHacks.setPrivate(dpe, DivinityParticleEffect.class, "y", y);
                AbstractDungeon.effectsQueue.add(dpe);
            }
        }

        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
            StanceAuraEffect sae = new StanceAuraEffect("Divinity");
            float x = ReflectionHacks.getPrivate(sae, StanceAuraEffect.class, "x");
            float y = ReflectionHacks.getPrivate(sae, StanceAuraEffect.class, "y");
            x += (owner.hb.cX - Wiz.adp().hb.cX);
            y += (owner.hb.cY - Wiz.adp().hb.cY);
            ReflectionHacks.setPrivate(sae, StanceAuraEffect.class, "x", x);
            ReflectionHacks.setPrivate(sae, StanceAuraEffect.class, "y", y);
            AbstractDungeon.effectsQueue.add(sae);
        }
    }
}
