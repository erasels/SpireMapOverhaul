package spireMapOverhaul.zones.wildfire.patches;

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
import spireMapOverhaul.zones.wildfire.Wildfire;

import java.nio.charset.StandardCharsets;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.rs;
import static spireMapOverhaul.SpireAnniversary6Mod.*;
import static spireMapOverhaul.util.Wiz.getCurZone;
import static spireMapOverhaul.zones.wildfire.Wildfire.isNvidiaCard;

@SuppressWarnings("unused")
public class FireShaderPatch {
    public static ShaderProgram fireShader = new ShaderProgram(SpriteBatch.createDefaultShader().getVertexShaderSource(), Gdx.files.internal(makeShaderPath("wildfire/fireShader.frag")).readString(String.valueOf(StandardCharsets.UTF_8)));
    private static final FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);

    static {
        fireShader.begin();
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "render"
    )
    public static class RenderFogInCombat {
        @SpirePrefixPatch
        public static void addShader(AbstractDungeon __instance, SpriteBatch sb) {
            if (rs == AbstractDungeon.RenderScene.NORMAL && isNvidiaCard && getCurZone() instanceof Wildfire && getShaderConfig()) {
                StartFbo(sb);
            }
        }

        @SpireInsertPatch(rloc=26) // 46 if you want the player's hand to be covered
        public static void removeShader(AbstractDungeon __instance, SpriteBatch sb) {
            if (rs == AbstractDungeon.RenderScene.NORMAL && isNvidiaCard && getCurZone() instanceof Wildfire && getShaderConfig()) {
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

        sb.setShader(fireShader);
        sb.setColor(Color.WHITE);
        fireShader.setUniformf("u_time", time);

        sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
        sb.draw(region, 0f, 0f);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sb.setShader(null);
        sb.flush();
    }
}
