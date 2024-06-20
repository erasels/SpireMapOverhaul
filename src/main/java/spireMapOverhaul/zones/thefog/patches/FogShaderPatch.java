package spireMapOverhaul.zones.thefog.patches;

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
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.thefog.TheFogZone;
import spireMapOverhaul.zones.thefog.util.MouseInfo;

import java.util.LinkedList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.rs;
import static spireMapOverhaul.SpireAnniversary6Mod.*;
import static spireMapOverhaul.util.Wiz.getCurZone;

@SuppressWarnings("unused")
public class FogShaderPatch {
    private static final float ZOOM = 2.0f;      // scale from 1 to 10
    private static final float INTENSITY = 3.0f; // scale from 1 to 5

    public static ShaderProgram fogShader;
    private static final LinkedList<MouseInfo> infos = new LinkedList<>();

    private static final FrameBuffer fbo;

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "render"
    )
    public static class RenderFogInCombat {
        @SpirePrefixPatch
        public static void addShader(AbstractDungeon __instance, SpriteBatch sb) {
            if (rs == AbstractDungeon.RenderScene.NORMAL && getCurZone() instanceof TheFogZone && getShaderConfig()) {
                StartFbo(sb);
            }
        }

        @SpireInsertPatch(rloc=26) // 46 if you want the player's hand to be foggeth
        public static void removeShader(AbstractDungeon __instance, SpriteBatch sb) {
            if (rs == AbstractDungeon.RenderScene.NORMAL && getCurZone() instanceof TheFogZone && getShaderConfig()) {
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

        sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
        sb.draw(region, 0f, 0f);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sb.setShader(null);
        sb.flush();
    }

    @SpirePatch2(clz = AbstractScene.class, method = "update")
    public static class Timer {
        public static void Prefix(AbstractScene __instance) {
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
            SpireAnniversary6Mod.logger.warn("Fog shader: " + fogShader.getLog());
        }
        fogShader.begin();

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
    }
}
