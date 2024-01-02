package spireMapOverhaul.zones.brokenSpace.relics;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.brokenSpace.BrokenSpaceZone;

import java.util.ArrayList;
import java.util.logging.Logger;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;

public abstract class BrokenRelic extends AbstractSMORelic {
    private static ShaderProgram brokenSpaceShader;
    private static final FrameBuffer fbo;

    private static final Logger logger = Logger.getLogger(BrokenRelic.class.getName());
    private static AbstractRelic originalRelic;
    private String origID;
    private static ArrayList<String> loadedRelics = new ArrayList<>();
    private static float shaderTimer = 0.0F;
    private float timerOffset = (float) (Math.random() * 1000f);


    public BrokenRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx, String origID) {
        super(makeID(setId), tier, sfx);


        this.origID = origID;
        if (loadedRelics.contains(origID)) {
            originalRelic = RelicLibrary.getRelic(origID);
            img = originalRelic.img;
            largeImg = originalRelic.largeImg;
            outlineImg = originalRelic.outlineImg;
            flavorText = getFlavorText();
        }
        BrokenSpaceZone.addBrokenRelic(makeID(setId));


    }


    private String getFlavorText() {
        int len = Math.min(originalRelic.flavorText.length(), 50);
        // fill with random characters
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        return sb.toString();

    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        GlitchRotation();
        firstRenderCheck();
        StartFbo(sb);
        super.renderInTopPanel(sb);
        StopFbo(sb, true);
    }

    @Override
    public void renderWithoutAmount(SpriteBatch sb, Color c) {
        GlitchRotation();
        firstRenderCheck();
        StartFbo(sb);
        super.renderWithoutAmount(sb, c);
        StopFbo(sb, false);
    }

    private float getRotation() {
        return ReflectionHacks.getPrivate(this, AbstractRelic.class, "rotation");
    }

    private void setRotation(float rotation) {
        ReflectionHacks.setPrivate(this, AbstractRelic.class, "rotation", rotation);
    }

    private boolean flipRotation = false;

    private void GlitchRotation() {
        float rotation = getRotation();
        float glitch = (float) (Math.random() * 1f);

        if (flipRotation) {
            glitch *= -1;
        }
        setRotation(rotation + glitch);

        //low chance to reset rotation to something random
        if (Math.random() < 0.03f) {
            flipRotation = Math.random() > 0.5f;
            setRotation((float) (Math.random() * 360f));
        }

    }


    @Override
    public void render(SpriteBatch sb, boolean renderAmount, Color outlineColor) {

        GlitchRotation();
        firstRenderCheck();

        StartFbo(sb);
        super.render(sb, renderAmount, outlineColor);
        StopFbo(sb, renderAmount);

    }

    private void firstRenderCheck() {
        if (!loadedRelics.contains(origID)) {
            loadedRelics.add(origID);
            originalRelic = RelicLibrary.getRelic(origID);
            img = originalRelic.img;
            largeImg = originalRelic.largeImg;
            outlineImg = originalRelic.outlineImg;
            flavorText = getFlavorText();

        }
    }

//    @Override
//    public void update() {
//        flavorText = getFlavorText();
//        super.update();
//    }

    public void StartFbo(SpriteBatch sb) {
        sb.end();

        fbo.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sb.begin();
    }

    public void StopFbo(SpriteBatch sb, float strength, boolean renderCounter) {
        sb.flush();
        fbo.end();


        TextureRegion region = new TextureRegion(fbo.getColorBufferTexture());
        region.flip(false, true);


        sb.setShader(brokenSpaceShader);
        sb.setColor(Color.WHITE);
        brokenSpaceShader.setUniformf("u_time", shaderTimer * 2 + timerOffset);
        brokenSpaceShader.setUniformf("u_strength", strength);

        sb.draw(region, 0, 0);
        sb.flush();
        sb.setShader(null);
        //renderCounter(sb, renderCounter);

    }

    public void StopFbo(SpriteBatch sb, boolean renderCounter) {
        StopFbo(sb, 1.0F, renderCounter);
    }

    @SpirePatch(
            clz = CardCrawlGame.class,
            method = "render"
    )
    public static class UpdateShaderTimer {
        @SpirePostfixPatch
        public static void updateShaderTimer() {

            shaderTimer += Gdx.graphics.getDeltaTime();

        }

    }

    @SpirePatch2(
            clz = SingleRelicViewPopup.class,
            method = "renderRelicImage"
    )
    public static class RenderLargeImage {
        @SpirePrefixPatch
        public static void renderLargeImage(SingleRelicViewPopup __instance, SpriteBatch sb) {
            AbstractRelic relic = ReflectionHacks.getPrivate(__instance, SingleRelicViewPopup.class, "relic");

            if (relic instanceof BrokenRelic) {
                ((BrokenRelic) relic).StartFbo(sb);
            }
        }

        @SpirePostfixPatch
        public static void renderLargeImage2(SingleRelicViewPopup __instance, SpriteBatch sb) {
            AbstractRelic relic = ReflectionHacks.getPrivate(__instance, SingleRelicViewPopup.class, "relic");

            if (relic instanceof BrokenRelic) {
                ((BrokenRelic) relic).StopFbo(sb, 3f, false);
            }
        }
    }


    static {
        brokenSpaceShader = new ShaderProgram(Gdx.files.internal(makeShaderPath("BrokenSpace/Glitch.vs")), Gdx.files.internal(makeShaderPath("BrokenSpace/Glitch.fs")));
        if (!brokenSpaceShader.isCompiled()) {
            logger.info("BrokenSpaceShader not compiled.");
        }
        brokenSpaceShader.begin();
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
    }
}