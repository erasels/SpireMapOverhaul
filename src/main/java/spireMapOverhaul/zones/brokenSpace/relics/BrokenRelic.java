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
    private static final FrameBuffer fbo;
    private static final Logger logger = Logger.getLogger(BrokenRelic.class.getName());
    private static final ShaderProgram brokenSpaceShader;
    private static String giganticString = "??";
    private static AbstractRelic originalRelic;
    private static final ArrayList<String> loadedRelics = new ArrayList<>();
    private static float shaderTimer = 0.0F;

    static {
        brokenSpaceShader = new ShaderProgram(Gdx.files.internal(makeShaderPath("BrokenSpace/Glitch.vs")), Gdx.files.internal(makeShaderPath("BrokenSpace/Glitch.fs")));
        if (!brokenSpaceShader.isCompiled()) {
            logger.info("BrokenSpaceShader not compiled: " + brokenSpaceShader.getLog());
        }
        brokenSpaceShader.begin();
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);


    }

    private float textResetTimer = 0.0F;
    private final String origID;
    private final float timerOffset = (float) (Math.random() * 1000f);
    private boolean flipRotation = false;

    public BrokenRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx, String origID) {
        super(makeID(setId), tier, sfx);


        this.origID = origID;
        if (loadedRelics.contains(origID)) {
            originalRelic = RelicLibrary.getRelic(origID);
            img = originalRelic.img;
            largeImg = originalRelic.largeImg;
            outlineImg = originalRelic.outlineImg;
            flavorText = updateFlavorText();
        }
        BrokenSpaceZone.addBrokenRelic(makeID(setId));
    }

    public static void setupStrings() {
        //logger.info("start time: " + System.currentTimeMillis());
        ArrayList<String> temp = new ArrayList<>();
        RelicLibrary.blueList.forEach(r -> temp.add(r.flavorText));
        RelicLibrary.redList.forEach(r -> temp.add(r.flavorText));
        RelicLibrary.greenList.forEach(r -> temp.add(r.flavorText));
        RelicLibrary.shopList.forEach(r -> temp.add(r.flavorText));
        RelicLibrary.bossList.forEach(r -> temp.add(r.flavorText));
        RelicLibrary.starterList.forEach(r -> temp.add(r.flavorText));
        RelicLibrary.commonList.forEach(r -> temp.add(r.flavorText));
        RelicLibrary.uncommonList.forEach(r -> temp.add(r.flavorText));
        RelicLibrary.rareList.forEach(r -> temp.add(r.flavorText));

        for (int i = 0; i < 100; i++) {
            giganticString += temp.get((int) (Math.random() * temp.size()));
        }
        //logger.info("end time: " + System.currentTimeMillis());
    }

    public String generateFlavorText() {
        // get random text from gigantic string

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int start = (int) (Math.random() * giganticString.length());
            int end = start + (int) (Math.random() * 10) + 1;
            if (end > giganticString.length()) {
                end = giganticString.length();
            }
            sb.append(giganticString, start, end);
        }

        if (sb.length() > 100) {
            sb.setLength(100);
        }
        return sb.toString();


    }

    private String updateFlavorText() {
        // replace a random chunk of text with random text from the gigantic string of the same length

        int start = (int) (Math.random() * flavorText.length());
        int end = start + (int) (Math.random() * 10) + 5;
        if (end > flavorText.length()) {
            end = flavorText.length();
        }
        int idx = (int) (Math.random() * giganticString.length() - 1);
        int len = end - start;

        if (idx + len > giganticString.length()) {
            idx = giganticString.length() - (len + 1);
        }


        String toInsert = giganticString.substring(idx, idx + (end - start));

        return flavorText.substring(0, start) + toInsert + flavorText.substring(end);


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
    public void update() {
        super.update();
        textResetTimer += Gdx.graphics.getDeltaTime();

        if (textResetTimer > 1.0F) {
            flavorText = updateFlavorText();
            textResetTimer = 0.0F;
            if (CardCrawlGame.relicPopup.isOpen) {
                CardCrawlGame.relicPopup.close();
                CardCrawlGame.relicPopup.open(this);
            }
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
            flavorText = generateFlavorText();

        }
    }

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
        brokenSpaceShader.setUniformf("u_time", shaderTimer * 4 + timerOffset);
        brokenSpaceShader.setUniformf("u_strength", strength);

        sb.draw(region, 0, 0);
        sb.flush();
        sb.setShader(null);
        if (renderCounter) {
            renderCounter(sb, true);
        }


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

    @SpirePatch2(
            clz = SingleRelicViewPopup.class,
            method = "update"
    )
    public static class UpdateLargeImage {
        @SpirePostfixPatch
        public static void updateLargeImage(SingleRelicViewPopup __instance) {
            AbstractRelic r = ReflectionHacks.getPrivate(__instance, SingleRelicViewPopup.class, "relic");

            if (r instanceof BrokenRelic) {
                BrokenRelic relic = (BrokenRelic) r;

                relic.textResetTimer -= Gdx.graphics.getDeltaTime();

                if (relic.textResetTimer <= 0) {
                    relic.flavorText = relic.updateFlavorText();
                    relic.textResetTimer = (float) (Math.random() * .2);

                }
            }
        }
    }

    @SpirePatch2(
            clz = RelicLibrary.class,
            method = "initialize"
    )
    public static class SetupStringsPatch {
        @SpirePostfixPatch
        public static void setupStrings() {
            BrokenRelic.setupStrings();
        }
    }
}