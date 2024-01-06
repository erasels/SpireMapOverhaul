package spireMapOverhaul.zones.storm.patches;

import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.storm.StormUtil;
import spireMapOverhaul.zones.storm.cardmods.ElectricModifier;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;

public class ElectricCardPatch {
    @SpirePatch(clz = AbstractCard.class, method = "render", paramtypez = SpriteBatch.class)
    public static class ZipZipZip {
        public static ShaderProgram electricShader = null;
        private static final FrameBuffer fbo = StormUtil.createBuffer();

        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCard __instance, SpriteBatch spriteBatch) {
            if(electricShader == null) {
                initDripShader();
            }
            if (!Settings.hideCards) {
                if (CardModifierManager.hasModifier(__instance, ElectricModifier.ID)) {
                    TextureRegion t = cardToTextureRegion(__instance, spriteBatch);
                    spriteBatch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
                    ShaderProgram oldShader = spriteBatch.getShader();
                    spriteBatch.setShader(electricShader);
                    electricShader.setUniformf("u_time", SpireAnniversary6Mod.time);
                    electricShader.setUniformf("u_bright_time", 0.5f);

                    spriteBatch.draw(t, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
                    spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                    spriteBatch.setShader(oldShader);
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }

        public static TextureRegion cardToTextureRegion(AbstractCard card, SpriteBatch sb) {
            sb.end();
            StormUtil.beginBuffer(fbo);
            sb.begin();
            IntBuffer buf_rgb = BufferUtils.newIntBuffer(16);
            IntBuffer buf_a = BufferUtils.newIntBuffer(16);
            Gdx.gl.glGetIntegerv(GL30.GL_BLEND_EQUATION_RGB, buf_rgb);
            Gdx.gl.glGetIntegerv(GL30.GL_BLEND_EQUATION_ALPHA, buf_a);

            Gdx.gl.glBlendEquationSeparate(buf_rgb.get(0), GL30.GL_MAX);
            Gdx.gl.glBlendEquationSeparate(GL30.GL_FUNC_ADD, GL30.GL_MAX);
            card.render(sb, false);
            Gdx.gl.glBlendEquationSeparate(GL30.GL_FUNC_ADD, GL30.GL_FUNC_ADD);
            Gdx.gl.glBlendEquationSeparate(buf_rgb.get(0), buf_a.get(0));

            sb.end();
            fbo.end();
            sb.begin();
            return StormUtil.getBufferTexture(fbo);
        }

        private static void initDripShader() {
            if (electricShader == null) {
                try {
                    electricShader = new ShaderProgram(
                            Gdx.files.internal(makeShaderPath("storm/electric/vertex.vs")),
                            Gdx.files.internal(makeShaderPath("storm/electric/fragment.fs"))
                    );
                    if (!electricShader.isCompiled()) {
                        System.err.println(electricShader.getLog());
                    }
                    if (!electricShader.getLog().isEmpty()) {
                        System.out.println(electricShader.getLog());
                    }
                } catch (GdxRuntimeException e) {
                    System.out.println("ERROR: Failed to init electric shader:");
                    e.printStackTrace();
                }
            }
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "render", paramtypez = SpriteBatch.class)
    public static class ZipZipZipSCV {
        public static ShaderProgram electricShader = null;

        private static ShaderProgram oldShader;

        @SpireInsertPatch(locator = ZipZipZipSCV.Locator.class)
        public static void ApplyShader(SingleCardViewPopup __instance, SpriteBatch sb) {
            if(electricShader == null) {
                initDripShader();
            }
            AbstractCard card = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
            if (CardModifierManager.hasModifier(card, ElectricModifier.ID)) {
                oldShader = sb.getShader();
                sb.setShader(electricShader);
                electricShader.setUniformf("u_time", SpireAnniversary6Mod.time);
                electricShader.setUniformf("u_bright_time", 0.5f);
            }
        }

        @SpireInsertPatch(locator = ZipZipZipSCV.LocatorTwo.class)
        public static void RemoveShader(SingleCardViewPopup __instance, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
            if (CardModifierManager.hasModifier(card, ElectricModifier.ID)) {
                sb.setShader(oldShader);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderCardBack");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        private static class LocatorTwo extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderArrows");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        private static void initDripShader() {
            if (electricShader == null) {
                try {
                    electricShader = new ShaderProgram(
                            Gdx.files.internal(makeShaderPath("storm/electric/vertex.vs")),
                            Gdx.files.internal(makeShaderPath("storm/electric/fragment.fs"))
                    );
                    if (!electricShader.isCompiled()) {
                        System.err.println(electricShader.getLog());
                    }
                    if (!electricShader.getLog().isEmpty()) {
                        System.out.println(electricShader.getLog());
                    }
                } catch (GdxRuntimeException e) {
                    System.out.println("ERROR: Failed to init electric shader:");
                    e.printStackTrace();
                }
            }
        }
    }
}
