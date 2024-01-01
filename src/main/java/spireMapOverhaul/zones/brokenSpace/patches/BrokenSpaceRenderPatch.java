package spireMapOverhaul.zones.brokenSpace.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.shop.StoreRelic;
import javassist.CtBehavior;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.zones.brokenSpace.BrokenSpaceZone;

import java.util.logging.Logger;

import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;
import static spireMapOverhaul.util.Wiz.adp;

public class BrokenSpaceRenderPatch {
    public static ShaderProgram brokenSpaceShader;

    private static FrameBuffer fbo;

    private static float shaderTimer = 0.0F;

    private static Logger logger = Logger.getLogger(BrokenSpaceRenderPatch.class.getName());

    @SpirePatch(
            clz = RewardItem.class,
            method = "render"
    )
    public static class RenderBrokenSpaceRewards {
        @SpirePrefixPatch
        public static void addShader(RewardItem __instance, SpriteBatch sb) {
            if (shouldRenderBrokenSpaceShader(__instance)) {
                sb.flush();
                fbo.begin();
                Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                Gdx.gl.glColorMask(true, true, true, true);
            }
        }

        @SpirePostfixPatch
        public static void removeShader(RewardItem __instance, SpriteBatch sb) {
            if (shouldRenderBrokenSpaceShader(__instance)) {
                fbo.end();

                TextureRegion region = new TextureRegion(fbo.getColorBufferTexture());
                region.flip(false, true);


                sb.setShader(brokenSpaceShader);
                brokenSpaceShader.setUniformf("u_time", shaderTimer);
                shaderTimer += Gdx.graphics.getDeltaTime();
                sb.draw(region, 0, 0);
                sb.setShader(null);


            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "draw");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCard"
    )
    public static class RenderBrokenSpaceCards {
        @SpirePrefixPatch
        public static void addShader(AbstractCard __instance, SpriteBatch sb) {
            if (shouldRenderBrokenSpaceShader(__instance)) {

                sb.flush();
                fbo.begin();
                Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT );
                Gdx.gl.glColorMask(true, true, true, true);
            }
        }

        @SpirePostfixPatch
        public static void removeShader(AbstractCard __instance, SpriteBatch sb) {
            if (shouldRenderBrokenSpaceShader(__instance)) {
                fbo.end();

                TextureRegion region = new TextureRegion(fbo.getColorBufferTexture());
                region.flip(false, true);


                sb.setShader(brokenSpaceShader);
                brokenSpaceShader.setUniformf("u_time", shaderTimer);
                shaderTimer += Gdx.graphics.getDeltaTime();
                sb.draw(region, 0, 0);
                sb.setShader(null);

            }
        }
    }

    @SpirePatch(
            clz = StoreRelic.class,
            method = "render"
    )
    public static class RenderBrokenSpaceRelics {
        @SpirePrefixPatch
        public static void addShader(StoreRelic __instance, SpriteBatch sb) {
            if (shouldRenderBrokenSpaceShader(__instance.relic)) {
                sb.flush();
                fbo.begin();
                Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                Gdx.gl.glColorMask(true, true, true, true);
            }
        }

        @SpirePostfixPatch
        public static void removeShader(StoreRelic __instance, SpriteBatch sb) {
            if (shouldRenderBrokenSpaceShader(__instance.relic)) {
                fbo.end();

                TextureRegion region = new TextureRegion(fbo.getColorBufferTexture());
                region.flip(false, true);

                sb.setShader(brokenSpaceShader);
                brokenSpaceShader.setUniformf("u_time", shaderTimer);
                shaderTimer += Gdx.graphics.getDeltaTime();
                sb.draw(region, 0, 0);
                sb.setShader(null);
            }
        }
    }




    private static boolean shouldRenderBrokenSpaceShader(RewardItem __instance) {
        return inBrokenSpace() && (__instance.type == RewardItem.RewardType.CARD || __instance.type == RewardItem.RewardType.RELIC);

    }

    private static boolean shouldRenderBrokenSpaceShader(AbstractCard __instance) {
        return inBrokenSpace() && isUnnatural(__instance) && !adp().masterDeck.contains(__instance);
    }

    private static boolean shouldRenderBrokenSpaceShader(AbstractRelic __instance) {
        return inBrokenSpace() && isUnnatural(__instance) && !adp().relics.contains(__instance);
    }

    private static boolean isUnnatural(AbstractRelic __instance) {
        return (__instance.tier == AbstractRelic.RelicTier.SPECIAL || __instance.tier == AbstractRelic.RelicTier.BOSS) && BrokenSpaceZone.UnnaturalRelicField.unnatural.get(__instance);
    }

    private static boolean isUnnatural(AbstractCard __instance) {
        return __instance.color != adp().getCardColor() && BrokenSpaceZone.UnnaturalCardField.unnatural.get(__instance);
    }


    private static boolean inBrokenSpace() {
        return ZonePatches.currentZone() != null && ZonePatches.currentZone().id.equals("BrokenSpace");
    }



    static {
        brokenSpaceShader = new ShaderProgram(Gdx.files.internal(makeShaderPath("BrokenSpace/Glitch.vs")), Gdx.files.internal(makeShaderPath( "BrokenSpace/Glitch.fs")));
//        if (true) {
//            logger.warning("Broken Space shader: " + brokenSpaceShader.getLog());
//        }
        brokenSpaceShader.begin();


        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
    }
}