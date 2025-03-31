package spireMapOverhaul.zones.humidity.shaders;

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
import spireMapOverhaul.zones.humidity.powers.HallucinatingPower;
import spireMapOverhaul.zones.thefog.util.MouseInfo;

import java.util.LinkedList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.rs;
import static spireMapOverhaul.SpireAnniversary6Mod.getShaderConfig;
import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;

@SuppressWarnings("unused")
public class HallucinationShader {
    private static final float ZOOM = 2.0f;      // scale from 1 to 10
    private static final float INTENSITY = 3.0f; // scale from 1 to 5

    public static ShaderProgram hallucinationShader;
    private static final LinkedList<MouseInfo> infos = new LinkedList<>();

    private static final FrameBuffer fbo;

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "render"
    )
    public static class RenderHueShift {
        @SpirePrefixPatch
        public static void addShader(AbstractDungeon __instance, SpriteBatch sb) {
            if(rs == AbstractDungeon.RenderScene.NORMAL && HallucinatingPower.isActive && getShaderConfig()) {
                StartFbo(sb);
            }
        }

        @SpireInsertPatch(rloc=26)
        public static void removeShader(AbstractDungeon __instance, SpriteBatch sb) {
            if(rs == AbstractDungeon.RenderScene.NORMAL && HallucinatingPower.isActive && getShaderConfig()) {
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

        sb.setShader(hallucinationShader);
        sb.setColor(Color.WHITE);

        hallucinationShader.setUniformf("hueShift", (float)Math.cos(HallucinatingPower.cycleTime/2));
        hallucinationShader.setUniformf("intensity", HallucinatingPower.displayedIntensity*1.0f);

        sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
        sb.draw(region, 0f, 0f);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sb.setShader(null);
        sb.flush();
    }


    static {
        hallucinationShader = new ShaderProgram(
                Gdx.files.internal(makeShaderPath("humidity/vertex.vs")).readString(),
                Gdx.files.internal(makeShaderPath("humidity/fragment.fs")).readString()
        );
        if (!hallucinationShader.isCompiled()) {
            SpireAnniversary6Mod.logger.warn("Hallucination shader: " + hallucinationShader.getLog());
        }
        hallucinationShader.begin();

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
    }
}
