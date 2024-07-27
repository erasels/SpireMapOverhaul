//code base derived from storm zone's electric card patch

package spireMapOverhaul.zones.windy.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.BufferUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.storm.StormUtil;

import java.nio.IntBuffer;

public class MonsterRenderDisplacementPatch {
    @SpirePatch(clz = AbstractMonster.class, method = "render", paramtypez = SpriteBatch.class)
    public static class MonsterDisplacer {
        private static final FrameBuffer fbo = StormUtil.createBuffer();
        private static IntBuffer buf_rgb;
        private static IntBuffer buf_a;

        @SpirePrefixPatch
        public static void Prefix(AbstractMonster __instance, SpriteBatch sb) {
            if(MonsterDisplacement.rotation.get(__instance) != 0.0f){ //old check but still works, render buffer only triggers when FlyAwayAction is active
                sb.end();
                StormUtil.beginBuffer(fbo);
                sb.begin();
                buf_rgb = BufferUtils.newIntBuffer(16);
                buf_a = BufferUtils.newIntBuffer(16);
                Gdx.gl.glGetIntegerv(GL30.GL_BLEND_EQUATION_RGB, buf_rgb);
                Gdx.gl.glGetIntegerv(GL30.GL_BLEND_EQUATION_ALPHA, buf_a);

                Gdx.gl.glBlendEquationSeparate(buf_rgb.get(0), GL30.GL_MAX);
                Gdx.gl.glBlendEquationSeparate(GL30.GL_FUNC_ADD, GL30.GL_MAX);
                MonsterDisplacement.activatedBuffer.set(__instance, true);
            }
        }

        @SpirePostfixPatch
        public static void Postfix(AbstractMonster __instance, SpriteBatch sb){
            if(MonsterDisplacement.activatedBuffer.get(__instance)){
                Gdx.gl.glBlendEquationSeparate(GL30.GL_FUNC_ADD, GL30.GL_FUNC_ADD);
                Gdx.gl.glBlendEquationSeparate(buf_rgb.get(0), buf_a.get(0));

                sb.end();
                fbo.end();
                sb.begin();
                TextureRegion region = StormUtil.getBufferTexture(fbo);

                sb.setColor(Color.WHITE.cpy());
                sb.draw(region,MonsterDisplacement.xOffset.get(__instance),MonsterDisplacement.yOffset.get(__instance), __instance.hb.cX, __instance.hb.cY, region.getRegionWidth(), region.getRegionHeight(), 1f, 1f, MonsterDisplacement.rotation.get(__instance));
                MonsterDisplacement.activatedBuffer.set(__instance, false);
            }
        }
    }

    @SpirePatch(
            clz=AbstractMonster.class,
            method=SpirePatch.CLASS
    )
    public static class MonsterDisplacement {
        public static SpireField<Float> rotation = new SpireField<>(() -> 0f);
        public static SpireField<Float> xOffset = new SpireField<>(() -> 0f);
        public static SpireField<Float> yOffset = new SpireField<>(() -> 0f);
        public static SpireField<Boolean> activatedBuffer = new SpireField<>(() -> false);
    }
}
