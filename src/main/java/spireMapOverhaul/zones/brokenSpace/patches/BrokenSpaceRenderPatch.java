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
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import javassist.CtBehavior;
import spireMapOverhaul.patches.ZonePatches;

import java.util.logging.Logger;

import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;

public class BrokenSpaceRenderPatch {
    public static ShaderProgram brokenSpaceShader;

    private static FrameBuffer fbo;


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
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
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


    private static boolean shouldRenderBrokenSpaceShader(RewardItem __instance) {
        return shouldRenderBrokenSpaceShader() && (__instance.type == RewardItem.RewardType.CARD || __instance.type == RewardItem.RewardType.RELIC);

    }

    private static boolean shouldRenderBrokenSpaceShader(AbstractCard __instance) {
        return shouldRenderBrokenSpaceShader() && __instance.color != AbstractDungeon.player.getCardColor();
    }


    private static boolean shouldRenderBrokenSpaceShader() {
        return ZonePatches.currentZone() != null && ZonePatches.currentZone().id.equals("BrokenSpace");
    }



    static {
        brokenSpaceShader = new ShaderProgram(Gdx.files.internal(makeShaderPath("BrokenSpace/Glitch.vs")), Gdx.files.internal(makeShaderPath( "BrokenSpace/Glitch.fs")));
        if (true) {
            logger.warning("Broken Space shader: " + brokenSpaceShader.getLog());


        }

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
    }
}