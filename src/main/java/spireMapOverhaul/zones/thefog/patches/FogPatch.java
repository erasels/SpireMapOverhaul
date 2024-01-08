package spireMapOverhaul.zones.thefog.patches;

import basemod.helpers.ScreenPostProcessorManager;
import basemod.interfaces.ScreenPostProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.scenes.TheBottomScene;
import spireMapOverhaul.zones.thefog.TheFogZone;
import spireMapOverhaul.zones.thefog.util.MouseInfo;

import java.util.LinkedList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;
import static spireMapOverhaul.SpireAnniversary6Mod.time;
import static spireMapOverhaul.util.Wiz.getCurZone;

// Credit to EricB who graciously donated this shader code to me
public class FogPatch {
    private static final float ZOOM = 2.0f;      // scale from 1 to 10
    private static final float INTENSITY = 3.0f; // scale from 1 to 5

    private static ShaderProgram shader = null;
    private static ScreenPostProcessor currentPostProcessor = null;
    private static LinkedList<MouseInfo> infos = new LinkedList<>();

    private static void initShader() {
        if (shader == null) {
            try {
                shader = new ShaderProgram(
                        Gdx.files.internal(makeShaderPath("fog/vertex.vs")),
                        Gdx.files.internal(makeShaderPath("fog/fragment.fs"))
                );
                if (!shader.isCompiled()) {
                    System.err.println(shader.getLog());
                }
                if (shader.getLog().length() > 0) {
                    System.out.println(shader.getLog());
                }
            } catch (GdxRuntimeException e) {
                System.out.println("ERROR: fog shader:");
                e.printStackTrace();
            }
        }
    }

    @SpirePatch(clz = TheBottomScene.class, method = "renderCombatRoomBg")
    public static class Shader {

        @SpirePrefixPatch
        public static void Prefix(TheBottomScene __instance, SpriteBatch sb) {
            if (currentPostProcessor != null) {
                ScreenPostProcessorManager.removePostProcessor(currentPostProcessor);
            }

            if (getCurZone() instanceof TheFogZone) {
                ScreenPostProcessor fog = createNewFogPostProcessor();
                ScreenPostProcessorManager.addPostProcessor(fog);
                currentPostProcessor = fog;
            }
        }

        @SpirePostfixPatch
        public static void Postfix(TheBottomScene __instance, SpriteBatch sb) {
            if(getCurZone() instanceof TheFogZone) {
                sb.setShader(null);
            }
        }
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


    public static ScreenPostProcessor createNewFogPostProcessor() {
        ScreenPostProcessor postProcessor = new ScreenPostProcessor() {
            private final String VERTEX_PATH = makeShaderPath("fog/vertex.vs");
            private final String FRAGMENT_PATH = makeShaderPath("fog/fragment.fs");
            private final ShaderProgram shaderEffect = new ShaderProgram(
                    Gdx.files.internal(VERTEX_PATH).readString(),
                    Gdx.files.internal(FRAGMENT_PATH).readString()
            );

            @Override
            public void postProcess(SpriteBatch sb, TextureRegion tex, OrthographicCamera orthographicCamera) {
                ShaderProgram oldShader = sb.getShader();
                if (!shaderEffect.isCompiled()) {
                    System.err.println(shaderEffect.getLog());
                }
                if (shaderEffect.getLog().length() > 0) {
                    System.out.println(shaderEffect.getLog());
                }
                sb.setShader(shaderEffect);
                int size = infos.size();
                float[] positions = new float[size * 3];
                for(int i = 0; i < size; i++) {
                    MouseInfo info = infos.get(i);
                    positions[3 * i] = info.x;
                    positions[3 * i + 1] = info.y;
                    positions[3 * i + 2] = time - info.time;
                }
                shaderEffect.setUniformf("u_time", time);
                shaderEffect.setUniform3fv("u_positions", positions, 0, size * 3);
                shaderEffect.setUniformi("u_size", size);
                shaderEffect.setUniformf("u_zoom", ZOOM);
                shaderEffect.setUniformf("u_intensity", INTENSITY);
                sb.draw(tex, 0f, 0f);
                sb.flush();
                sb.setShader(oldShader);
            }
        };
        return postProcessor;
    }
}