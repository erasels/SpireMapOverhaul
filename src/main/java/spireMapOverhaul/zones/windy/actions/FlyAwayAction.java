package spireMapOverhaul.zones.windy.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import spireMapOverhaul.zones.windy.effects.LoseGoldTextEffect;
import spireMapOverhaul.zones.windy.effects.WindyDustEffect;
import spireMapOverhaul.zones.windy.patches.GoldRewardReductionPatch;
import spireMapOverhaul.zones.windy.patches.MonsterRenderDisplacementPatch;

import java.util.ArrayList;

public class FlyAwayAction extends AbstractGameAction {
    public final float MAX_DUR = 3.0f;
    public ArrayList<AbstractMonster> targets = new ArrayList<>();

    public FlyAwayAction(AbstractMonster target){
        target.hideHealthBar();

        for(AbstractGameAction a : AbstractDungeon.actionManager.actions){
            if(a instanceof FlyAwayAction){
                this.isDone = true;
                ((FlyAwayAction)a).targets.add(target);
                return;
            }
        }

        targets.add(target);
        this.duration = 0;
    }

    @Override
    public void update() {
        if(this.duration == 0){
            for (int i = 0; i < 100; i++) {
                AbstractDungeon.effectsQueue.add(new WindyDustEffect(true));
            }
        }

        if(this.duration >= MAX_DUR){
            for(AbstractMonster m: targets){
                //darklings why
                boolean temp = AbstractDungeon.getCurrRoom().cannotLose;
                AbstractDungeon.getCurrRoom().cannotLose = false;
                m.die();
                AbstractDungeon.getCurrRoom().cannotLose = temp;

                GoldRewardReductionPatch.combatGoldReduction += m.currentHealth;
                AbstractDungeon.effectsQueue.add(new LoseGoldTextEffect(m.currentHealth, m));
                m.drawX = Settings.WIDTH * 2; //Prevent death based animations from playing on screen
                if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                    AbstractDungeon.getCurrRoom().cannotLose = false;
                    AbstractDungeon.actionManager.cleanCardQueue();
                    AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
                    AbstractDungeon.effectList.add(new DeckPoofEffect((float)Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
                    AbstractDungeon.overlayMenu.hideCombatPanels();
                }
            }
            this.isDone = true;
            return;
        }

        this.duration += Gdx.graphics.getDeltaTime();
        float ratio = this.duration / MAX_DUR;
        float degree = 900 * ratio * ratio;

        for(AbstractMonster m: targets){
            MonsterRenderDisplacementPatch.MonsterDisplacement.rotation.set(m, degree);
            MonsterRenderDisplacementPatch.MonsterDisplacement.xOffset.set(m, ratio * ratio * ratio * 1600f * Settings.scale);
            MonsterRenderDisplacementPatch.MonsterDisplacement.yOffset.set(m, ratio * ratio * 500f * Settings.scale);
        }
    }
}
