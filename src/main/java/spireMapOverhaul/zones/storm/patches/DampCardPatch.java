package spireMapOverhaul.zones.storm.patches;

import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import spireMapOverhaul.zones.storm.cardmods.DampModifier;
import spireMapOverhaul.zones.storm.cardmods.ElectricModifier;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;
import static spireMapOverhaul.zones.storm.StormUtil.initDripShader;

public class DampCardPatch {
    @SpirePatch(clz = AbstractCard.class, method = "render", paramtypez = SpriteBatch.class)
    public static class DripDripDrip {
        public static ShaderProgram dripShader = null;
        private static final FrameBuffer fbo = StormUtil.createBuffer();

        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCard __instance, SpriteBatch spriteBatch) {
            if(dripShader == null) {
                dripShader = initDripShader(dripShader);
            }
            if (!Settings.hideCards) {
                if (CardModifierManager.hasModifier(__instance, DampModifier.ID)) {
                    TextureRegion t = cardToTextureRegion(__instance, spriteBatch);
                    spriteBatch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
                    ShaderProgram oldShader = spriteBatch.getShader();
                    spriteBatch.setShader(dripShader);
                    dripShader.setUniformf("u_time", SpireAnniversary6Mod.time);
                    dripShader.setUniformf("u_dripAmount1", 0.75f);
                    dripShader.setUniformf("u_dripAmount2", 0.5f);
                    dripShader.setUniformf("u_dripSpeed", 0.2f);
                    dripShader.setUniformf("u_dripStrength", 0.2f);

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
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "render", paramtypez = SpriteBatch.class)
    public static class DripDripDripSCV {
        public static ShaderProgram dripShader = null;
        private static ShaderProgram oldShader;

        @SpireInsertPatch(locator = DampCardPatch.DripDripDripSCV.Locator.class)
        public static void ApplyShader(SingleCardViewPopup __instance, SpriteBatch sb) {
            if(dripShader == null) {
                dripShader = initDripShader(dripShader);
            }
            AbstractCard card = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
            if (CardModifierManager.hasModifier(card, DampModifier.ID)) {
                oldShader = sb.getShader();
                sb.setShader(dripShader);
                dripShader.setUniformf("u_time", SpireAnniversary6Mod.time);
            }
        }

        @SpireInsertPatch(locator = DampCardPatch.DripDripDripSCV.LocatorTwo.class)
        public static void RemoveShader(SingleCardViewPopup __instance, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
            if (CardModifierManager.hasModifier(card, DampModifier.ID)) {
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
    }
}
