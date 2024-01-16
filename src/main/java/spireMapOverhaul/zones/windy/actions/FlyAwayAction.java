package spireMapOverhaul.zones.windy.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.windy.effects.LoseGoldTextEffect;
import spireMapOverhaul.zones.windy.patches.GoldRewardReductionPatch;
import spireMapOverhaul.zones.windy.patches.MonsterRenderDisplacementPatch;

public class FlyAwayAction extends AbstractGameAction {
    public final float MAX_DUR = 3.0f;
    public float ogX, ogY;
    public float targetHeight;

    public FlyAwayAction(AbstractMonster target){
        this.target = target;
        target.hideHealthBar();
        this.duration = 0;
        targetHeight = target.hb_h;
        ogX = target.drawX;
        ogY = target.drawY;
    }

    @Override
    public void update() {
        if(this.duration >= MAX_DUR){
            addToTop(new InstantKillAction(target));
            GoldRewardReductionPatch.combatGoldReduction += target.currentHealth;
            AbstractDungeon.effectsQueue.add(new LoseGoldTextEffect(target.currentHealth, target));
            target.drawX = Settings.WIDTH * 2; //Prevent death based animations from playing on screen
            this.isDone = true;
            return;
        }

        this.duration += Gdx.graphics.getDeltaTime();
        float ratio = this.duration / MAX_DUR;
        float degree = 900 * ratio * ratio;

        MonsterRenderDisplacementPatch.MonsterDisplacement.rotation.set(target, degree);
        MonsterRenderDisplacementPatch.MonsterDisplacement.xOffset.set(target, ratio * ratio * ratio * 1600f * Settings.scale);
        MonsterRenderDisplacementPatch.MonsterDisplacement.yOffset.set(target, ratio * ratio * 500f * Settings.scale);
    }
}
