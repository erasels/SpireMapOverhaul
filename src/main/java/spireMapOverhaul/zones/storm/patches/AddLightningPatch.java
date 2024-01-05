package spireMapOverhaul.zones.storm.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.ImpactSparkEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.storm.StormUtil;
import spireMapOverhaul.zones.storm.vfx.AlwaysBehindLightningEffect;

import static spireMapOverhaul.util.Wiz.atb;


public class AddLightningPatch {

    @SpirePatch(clz = AbstractRoom.class, method = SpirePatch.CLASS)
    public static class AbstractRoomFields {
        public static SpireField<Float> timeToStrike = new SpireField<>(() -> 4.0f);
        public static SpireField<Float> timeSinceStrike = new SpireField<>(() -> 1.0f);
        public static SpireField<AbstractCreature> conduitTarget = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class GreaseLightning {

        private static float vfxTimer;
        private static float timeScaleStart = 0.1F;
        private static float timeScaleEnd = 0.3F;
        @SpirePrefixPatch()
        public static void Prefix() {
            if(StormUtil.isInStormZone()) {
                //Lightning Strikes
                if (AbstractRoomFields.timeToStrike.get(AbstractDungeon.getCurrRoom()) < 0.0f) {
                    float rand_y = MathUtils.random(((float) Settings.HEIGHT / 2) - 50.0f * Settings.scale, ((float) Settings.HEIGHT / 2) - 350.0f * Settings.scale);
                    boolean renderBehind = rand_y > (float) Settings.HEIGHT / 2 - 250.0f;
                    atb(new VFXAction(new AlwaysBehindLightningEffect(MathUtils.random(Settings.WIDTH), rand_y, renderBehind)));
                    if(Settings.AMBIANCE_ON) {
                        atb(new SFXAction(SpireAnniversary6Mod.THUNDER_KEY, 0.2f));
                    }
                    AbstractRoomFields.timeToStrike.set(AbstractDungeon.getCurrRoom(), MathUtils.random(3.5f, 10.0f));
                    AbstractRoomFields.timeSinceStrike.set(AbstractDungeon.getCurrRoom(), 0.0f);
                }
                AbstractRoomFields.timeToStrike.set(AbstractDungeon.getCurrRoom(), AbstractRoomFields.timeToStrike.get(AbstractDungeon.getCurrRoom()) - Gdx.graphics.getDeltaTime());
                AbstractRoomFields.timeSinceStrike.set(AbstractDungeon.getCurrRoom(), AbstractRoomFields.timeSinceStrike.get(AbstractDungeon.getCurrRoom()) + Gdx.graphics.getDeltaTime());


                //Conduit power vfx that happens during turn
                AbstractCreature conduitTarget = AbstractRoomFields.conduitTarget.get(AbstractDungeon.getCurrRoom());
                if(conduitTarget != null) {
                    vfxTimer -= Gdx.graphics.getDeltaTime();
                    if (vfxTimer < 0.0f) {
                        AbstractDungeon.topLevelEffectsQueue.add(new ImpactSparkEffect(conduitTarget.drawX +
                                MathUtils.random(-20.0F, 20.0f) * Settings.scale, conduitTarget.drawY +
                                MathUtils.random(-20.0F, 20.0f) * Settings.scale));

                        vfxTimer = MathUtils.random(timeScaleStart, timeScaleEnd);
                        if(!AbstractDungeon.actionManager.turnHasEnded) {
                            timeScaleStart = 0.1f;
                            timeScaleEnd = 0.3f;
                        } else {
                            vfxTimer = 0.01f;
                        }
                    }
                }
            }
        }
    }
}
