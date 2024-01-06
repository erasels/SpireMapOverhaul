package spireMapOverhaul.zones.storm.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import spireMapOverhaul.zones.storm.StormUtil;

public class TogglePlayerElecticShaderAction extends AbstractGameAction {

    private static final float EFFECT_DUR = 0.5f;
    public TogglePlayerElecticShaderAction() {
        this.duration = EFFECT_DUR;
    }

    @Override
    public void update() {
        if(duration == EFFECT_DUR) {
            StormUtil.activePlayerLightning = true;
        }
        StormUtil.brightTime = duration;
        if(duration < 0.0f) {
            StormUtil.activePlayerLightning = false;
            isDone = true;
        }
        duration -= Gdx.graphics.getRawDeltaTime();
    }
}
