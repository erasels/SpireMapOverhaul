package spireMapOverhaul.zones.brokenSpace.patches;

import basemod.BaseMod;
import basemod.patches.com.megacrit.cardcrawl.core.CardCrawlGame.ApplyScreenPostProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

import java.awt.*;
import java.util.logging.Logger;

import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;

public class BrokenSpaceRenderPatch {
    public static ShaderProgram brokenSpaceShader;

    private static FrameBuffer fbo;

    private static Logger logger = Logger.getLogger(BrokenSpaceRenderPatch.class.getName());

    @SpirePatch2(clz = AbstractDungeon.class, method = "render")
    public static class RenderPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(SpriteBatch sb){
            fbo.end();
            sb.end();
            Texture img = fbo.getColorBufferTexture();
            sb.begin();
            sb.setShader(brokenSpaceShader);
            sb.draw(img,0,0);
            sb.setShader((ShaderProgram)null);// 95
            sb.end();
            sb.begin();

        }

        private static class Locator extends SpireInsertLocator{

            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "render");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }


    }

    @SpirePatch2(clz = CardCrawlGame.class, method = "render")
    public static class PreRenderPatch {
        @SpireInsertPatch(locator = ApplyScreenPostProcessor.BeginLocator.class)
        public static void patch(SpriteBatch ___sb){
            fbo.begin();
            ___sb.setBlendFunction(770, 771);// 56
            Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);// 57
            Gdx.gl.glClear(17664);// 58
        }
    }


    static {
        brokenSpaceShader = new ShaderProgram(Gdx.files.internal(makeShaderPath("BrokenSpace/Glitch.vs")), Gdx.files.internal(makeShaderPath( "BrokenSpace/Glitch.fs")));
        if (!brokenSpaceShader.isCompiled()) {
            logger.warning("Broken Space shader not compiled: " + brokenSpaceShader.getLog());
        }

    }

    static {
        int width = Gdx.graphics.getWidth();// 134
        int height = Gdx.graphics.getHeight();// 135
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false, false);// 137
    }
}