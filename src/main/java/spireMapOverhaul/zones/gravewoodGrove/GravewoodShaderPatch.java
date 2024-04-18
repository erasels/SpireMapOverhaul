package spireMapOverhaul.zones.gravewoodGrove;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.SpireAnniversary6Mod;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.rs;
import static spireMapOverhaul.SpireAnniversary6Mod.getShaderConfig;
import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;
import static spireMapOverhaul.util.Wiz.getCurZone;

public class GravewoodShaderPatch {
    public static ShaderProgram desaturationShader;
    private static final FrameBuffer fbo;

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class RenderDesaturationInCombat {
        @SpirePrefixPatch
        public static void addShader(AbstractDungeon instance, SpriteBatch sb) {
            if (rs == AbstractDungeon.RenderScene.NORMAL && getCurZone() instanceof GravewoodGroveZone && getShaderConfig()) {
                StartFbo(sb);
            }
        }

        @SpireInsertPatch(rloc = 26)
        public static void removeShader(AbstractDungeon instance, SpriteBatch sb) {
            if (rs == AbstractDungeon.RenderScene.NORMAL && getCurZone() instanceof GravewoodGroveZone && getShaderConfig()) {
                StopFbo(sb);
            }
        }
    }

    public static void StartFbo(SpriteBatch sb) {
        sb.flush();
        fbo.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static void StopFbo(SpriteBatch sb) {
        sb.flush();
        fbo.end();
        TextureRegion region = new TextureRegion(fbo.getColorBufferTexture());
        region.flip(false, true);
        sb.setShader(desaturationShader);
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
        sb.draw(region, 0f, 0f);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sb.setShader(null);
        sb.flush();
    }

    static {
        desaturationShader = new ShaderProgram(
                Gdx.files.internal(makeShaderPath("gravewoodGrove/vertex.vs")).readString(),
                Gdx.files.internal(makeShaderPath("gravewoodGrove/fragment.fs")).readString()
        );
        if (!desaturationShader.isCompiled()) {
            SpireAnniversary6Mod.logger.warn("Desaturation shader: " + desaturationShader.getLog());
        }
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
    }
}