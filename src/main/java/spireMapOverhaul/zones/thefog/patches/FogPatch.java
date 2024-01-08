package spireMapOverhaul.zones.thefog.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.scenes.TheBottomScene;
import spireMapOverhaul.zones.brokenSpace.patches.BrokenSpaceRenderPatch;
import spireMapOverhaul.zones.thefog.TheFogZone;
import spireMapOverhaul.zones.thefog.util.MouseInfo;

import java.util.LinkedList;
import java.util.logging.Logger;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.rs;
import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;
import static spireMapOverhaul.SpireAnniversary6Mod.time;
import static spireMapOverhaul.util.Wiz.getCurZone;

public class FogPatch {
    private static final float ZOOM = 2.0f;      // scale from 1 to 10
    private static final float INTENSITY = 3.0f; // scale from 1 to 5

    public static ShaderProgram fogShader;
    private static LinkedList<MouseInfo> infos = new LinkedList<>();

    private static final FrameBuffer fbo;


    private static final Logger logger = Logger.getLogger(BrokenSpaceRenderPatch.class.getName());

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "render"
    )
    public static class RenderFogInCombat {
        @SpirePrefixPatch
        public static void addShader(AbstractDungeon __instance, SpriteBatch sb) {
            if (rs == AbstractDungeon.RenderScene.NORMAL && getCurZone() instanceof TheFogZone) {
                StartFbo(sb);
            }
        }

        @SpireInsertPatch(rloc=26) // 46 if you want the player's hand to be foggeth
        public static void removeShader(AbstractDungeon __instance, SpriteBatch sb) {
            if (rs == AbstractDungeon.RenderScene.NORMAL && getCurZone() instanceof TheFogZone) {
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

        sb.setShader(fogShader);
        sb.setColor(Color.WHITE);
        int size = infos.size();
        float[] positions = new float[size * 3];
        for(int i = 0; i < size; i++) {
            MouseInfo info = infos.get(i);
            positions[3 * i] = info.x;
            positions[3 * i + 1] = info.y;
            positions[3 * i + 2] = time - info.time;
        }
        fogShader.setUniformf("u_time", time);
        fogShader.setUniform3fv("u_positions", positions, 0, size * 3);
        fogShader.setUniformi("u_size", size);
        fogShader.setUniformf("u_zoom", ZOOM);
        fogShader.setUniformf("u_intensity", INTENSITY);

        sb.draw(region, 0f, 0f);
        sb.setShader(null);
        sb.flush();
    }

    @SpirePatch2(clz = TheBottomScene.class, method = "update")
    public static class Timer {
        public static void Prefix(TheBottomScene __instance) {
            if (infos.size() > Settings.MAX_FPS) {
                infos.removeFirst();
            }
            infos.add(new MouseInfo(Gdx.input.getX(), Settings.HEIGHT - Gdx.input.getY(), time));
        }
    }

    static {
        fogShader = new ShaderProgram(
                Gdx.files.internal(makeShaderPath("fog/vertex.vs")).readString(),
                Gdx.files.internal(makeShaderPath("fog/fragment.fs")).readString()
        );
        if (!fogShader.isCompiled()) {
            logger.warning("Fog shader: " + fogShader.getLog());
        }
        fogShader.begin();

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
    }
}
