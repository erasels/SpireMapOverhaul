package spireMapOverhaul.zones.gremlinTown.actions;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.att;


public class RockThrowAction extends AbstractGameAction {
    private final AbstractMonster m;
    private static final float DURATION = 0.35F;
    private static final Texture CUCUMBER_IMAGE =
            new Texture("images/relics/test3.png");
    private final AbstractPlayer p = adp();
    private final DamageInfo info;

    public RockThrowAction(AbstractMonster monster, DamageInfo info) {
        this.m = monster;
        this.info = info;
        actionType = ActionType.DAMAGE;
        duration = DURATION;
    }

    public void update() {
        if (m == null) {
            isDone = true;
            return;
        }

        float targetX = 0f;
        float targetY = 0f;
        if (duration == DURATION) {
            targetX = p.hb.cX + MathUtils.random(-25.0f*Settings.xScale, 25.0f*Settings.xScale);
            targetY = p.hb.cY + MathUtils.random(-25.0f*Settings.yScale, 25.0f*Settings.yScale);
            float targetX2 = targetX + MathUtils.random(-400.0f*Settings.xScale, 400.0f*Settings.xScale);
            float targetY2 = targetY + MathUtils.random(-400.0f*Settings.yScale, 400.0f*Settings.yScale);
            AbstractGameEffect rockEffect = new VfxBuilder(CUCUMBER_IMAGE, m.hb.cX, m.hb.cY, DURATION)
                    .moveX(m.hb.cX, targetX, VfxBuilder.Interpolations.LINEAR)
                    .moveY(m.hb.cY, targetY, VfxBuilder.Interpolations.LINEAR)
                    .rotate(720.0f)
                    .andThen(0.5f)
                    .moveX(targetX, targetX2, VfxBuilder.Interpolations.LINEAR)
                    .moveY(targetY, targetY2, VfxBuilder.Interpolations.LINEAR)
                    .rotate(360.0f)
                    .fadeOut(0.5f)
                    .build();

            AbstractDungeon.topLevelEffects.add(rockEffect);
        }

        tickDuration();

        if (duration <= 0F && p != null && p.currentHealth > 0)
            att(new DamageAction(adp(), info, AttackEffect.BLUNT_HEAVY));
    }
}