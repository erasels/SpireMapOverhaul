package spireMapOverhaul.zones.storm;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.storm.cardmods.DampModifier;
import spireMapOverhaul.zones.storm.cardmods.ElectricModifier;

import java.util.stream.Collectors;

import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;
import static spireMapOverhaul.util.Wiz.getCurZone;

public class StormUtil {

    public static long rainSoundId;
    public static boolean activePlayerLightning;
    public static float brightTime;
    public static AbstractCreature conduitTarget;
    public static float timeToStrike;
    public static float timeSinceStrike;

    public static FrameBuffer createBuffer() {
        return createBuffer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static FrameBuffer createBuffer(int sizeX, int sizeY) {
        return new FrameBuffer(Pixmap.Format.RGBA8888, sizeX, sizeY, false, false);
    }

    public static void beginBuffer(FrameBuffer fbo) {
        fbo.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glColorMask(true, true, true, true);
    }

    public static TextureRegion getBufferTexture(FrameBuffer fbo) {
        TextureRegion texture = new TextureRegion(fbo.getColorBufferTexture());
        texture.flip(false, true);
        return texture;
    }

    public static boolean cardValidToMakeDamp(AbstractCard c) {
        return !CardModifierManager.hasModifier(c, DampModifier.ID) && !CardModifierManager.hasModifier(c, ElectricModifier.ID);
    }

    public static int countValidCardsInHandToMakeDamp() {
        return (int) AbstractDungeon.player.hand.group.stream().filter(StormUtil::cardValidToMakeDamp).count();
    }

    public static boolean isInStormZone() {
        return getCurZone() instanceof StormZone;
    }

    public static ShaderProgram initDripShader(ShaderProgram dripShader) {
        if (dripShader == null) {
            try {
                dripShader = new ShaderProgram(
                        Gdx.files.internal(makeShaderPath("storm/drip/vertex.vs")),
                        Gdx.files.internal(makeShaderPath("storm/drip/fragment.fs"))
                );
                if (!dripShader.isCompiled()) {
                    System.err.println(dripShader.getLog());
                }
                if (!dripShader.getLog().isEmpty()) {
                    System.out.println(dripShader.getLog());
                }
            } catch (GdxRuntimeException e) {
                System.out.println("ERROR: Failed to init electric shader:");
                e.printStackTrace();
            }
        }
        return dripShader;
    }

    public static ShaderProgram initDarkShader(ShaderProgram darkShader) {
        if (darkShader == null) {
            try {
                darkShader = new ShaderProgram(
                        Gdx.files.internal(makeShaderPath("storm/dark/vertex.vs")),
                        Gdx.files.internal(makeShaderPath("storm/dark/fragment.fs"))
                );
                if (!darkShader.isCompiled()) {
                    System.err.println(darkShader.getLog());
                }
                if (!darkShader.getLog().isEmpty()) {
                    System.out.println(darkShader.getLog());
                }
            } catch (GdxRuntimeException e) {
                System.out.println("ERROR: dark shader:");
                e.printStackTrace();
            }
        }
        return darkShader;
    }

    public static ShaderProgram initElectricShader(ShaderProgram electricShader) {
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
                System.out.println("ERROR: electric shader:");
                e.printStackTrace();
            }
        }
        return electricShader;
    }
}
