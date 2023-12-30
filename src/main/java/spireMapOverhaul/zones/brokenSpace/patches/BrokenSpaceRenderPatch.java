package spireMapOverhaul.zones.brokenSpace.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.logging.Logger;

public class BrokenSpaceRenderPatch {
    public static ShaderProgram brokenSpaceShader;

    private static Logger logger = Logger.getLogger(BrokenSpaceRenderPatch.class.getName());


    static {
        brokenSpaceShader = new ShaderProgram(Gdx.files.internal("shaders/BrokenSpace/Glitch.vs"), Gdx.files.internal("shaders/BrokenSpace/Glitch.fs"));
        if (!brokenSpaceShader.isCompiled()) {
            logger.warning("Broken Space shader not compiled: " + brokenSpaceShader.getLog());
        }

    }
}