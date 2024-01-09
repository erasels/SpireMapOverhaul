package spireMapOverhaul.zones.voidseed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import spireMapOverhaul.zones.brokenspace.patches.BrokenSpaceRenderPatch;

import java.util.logging.Logger;

import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;

public class VoidSeedShaderManager {
    public static ShaderProgram voidSeedShader;

    private static final FrameBuffer fbo;
    private static final Logger logger = Logger.getLogger(BrokenSpaceRenderPatch.class.getName());
    public static float shaderTimer = 0.0F;


    public static void StartFbo(SpriteBatch sb) {
        sb.flush();
        fbo.begin();

        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static void StopFbo(SpriteBatch sb, float timerOffset, float timeScale, boolean useTex) {
        StopFbo(sb, timerOffset, timeScale, useTex, new Color(0.5f, 0.0f, 0.5f, 0.2f));
    }

    public static void StopFbo(SpriteBatch sb, float timerOffset, float timeScale, boolean useTex, Color color) {
        sb.flush();
        fbo.end();

        TextureRegion region = new TextureRegion(fbo.getColorBufferTexture());
        region.flip(false, true);

        sb.setShader(voidSeedShader);
        sb.setColor(Color.WHITE);
        voidSeedShader.setUniformf("u_time", shaderTimer * timeScale + timerOffset);
        voidSeedShader.setUniformi("LAYERS", 20);
        voidSeedShader.setUniformf("DEPTH", 1f);
        voidSeedShader.setUniformf("WIDTH", 0.3f);
        voidSeedShader.setUniformf("SPEED", -1.5f);
        voidSeedShader.setUniformf("u_color", color);
        voidSeedShader.setUniformf("u_tint", new Color(.9f, 0.5f, 0.9f, 1f));
        voidSeedShader.setUniformf("u_usetex", useTex ? 1f : 0f);

        sb.draw(region, 0, 0);
        sb.setShader(null);
        sb.flush();
    }


    static {
        voidSeedShader = new ShaderProgram(Gdx.files.internal(makeShaderPath("VoidSeed/voidvert.vs")), Gdx.files.internal(makeShaderPath("VoidSeed/voidfrag.fs")));
        if (!voidSeedShader.isCompiled()) {
            logger.warning("Void seed shader: " + voidSeedShader.getLog());
        }
        voidSeedShader.begin();


        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
    }
}
