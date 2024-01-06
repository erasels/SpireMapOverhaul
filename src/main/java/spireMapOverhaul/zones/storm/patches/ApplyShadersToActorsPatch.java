package spireMapOverhaul.zones.storm.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.esotericsoftware.spine.SkeletonMeshRenderer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.zones.storm.StormUtil;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.time;
import static spireMapOverhaul.zones.storm.StormUtil.*;

public class ApplyShadersToActorsPatch {
    private static ShaderProgram darkShader = null;
    private static ShaderProgram electricShader = null;

    @SpirePatch(clz = AbstractPlayer.class, method = "renderPlayerImage")
    public static class ApplyPlayerShaders {
        private static FrameBuffer buffer;
        private static TextureRegion playerTexture;

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SkeletonMeshRenderer.class, "draw");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void startBuffer(AbstractPlayer __instance, SpriteBatch sb) {
            if(StormUtil.isInStormZone() || StormUtil.activePlayerLightning) {
                sb.flush();
                if (buffer == null) {
                    buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
                }
                buffer.begin();
                Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                Gdx.gl.glColorMask(true, true, true, true);
            }
        }

        private static class LocatorTwo extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "begin");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = LocatorTwo.class)
        public static void endBufferAndDraw(AbstractPlayer __instance, SpriteBatch sb) {
            if (StormUtil.activePlayerLightning || StormUtil.isInStormZone()) {
                sb.flush();
                buffer.end();
                if (playerTexture == null) {
                    playerTexture = new TextureRegion(buffer.getColorBufferTexture());
                    playerTexture.flip(false, true);
                } else {
                    playerTexture.setTexture(buffer.getColorBufferTexture());
                }
                if(StormUtil.activePlayerLightning) {
                    electricShader = initElectricShader(electricShader);
                    sb.begin();
                    sb.setShader(electricShader);
                    electricShader.setUniformf("u_time", time);
                    electricShader.setUniformf("u_bright_time", StormUtil.brightTime);
                } else {
                    darkShader = initDarkShader(darkShader);
                    sb.begin();
                    sb.setShader(darkShader);
                    darkShader.setUniformf("u_time", timeSinceStrike);
                }
                sb.draw(playerTexture, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
                sb.setShader(null);
                sb.end();
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "render")
    public static class DarkenMonster {
        private static FrameBuffer buffer;
        private static TextureRegion playerTexture;

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SkeletonMeshRenderer.class, "draw");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void startBuffer(AbstractMonster __instance, SpriteBatch sb) {
            if (StormUtil.isInStormZone()) {
                sb.flush();
                if (buffer == null) {
                    buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
                }
                buffer.begin();
                Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                Gdx.gl.glColorMask(true, true, true, true);
            }
        }

        private static class LocatorTwo extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "begin");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = LocatorTwo.class)
        public static void endBufferAndDraw(AbstractMonster __instance, SpriteBatch sb) {
            if (StormUtil.isInStormZone()) {
                sb.flush();
                buffer.end();
                if (playerTexture == null) {
                    playerTexture = new TextureRegion(buffer.getColorBufferTexture());
                    playerTexture.flip(false, true);
                } else {
                    playerTexture.setTexture(buffer.getColorBufferTexture());
                }
                darkShader = initDarkShader(darkShader);
                sb.begin();
                sb.setShader(darkShader);
                sb.draw(playerTexture, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
                sb.setShader(null);
                sb.end();
            }
        }
    }
}
