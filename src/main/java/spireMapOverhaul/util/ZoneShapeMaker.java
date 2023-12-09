package spireMapOverhaul.util;

import basemod.Pair;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ZoneShapeMaker {

    private static final float OFFSET_X = Settings.isMobile ? 496.0F * Settings.xScale : 560.0F * Settings.xScale;
    private static final float OFFSET_Y = 180.0F * Settings.scale;
    private static final float SPACING_X = Settings.isMobile ? (int)(Settings.xScale * 64.0F) * 2.2F : (int)(Settings.xScale * 64.0F) * 2.0F;

    private static final int FB_HEIGHT_FACTOR = 1; //non-functional, haven't seen the problem it was supposed to solve in a while, which was a cutoff when the zones got too tall
    private static final float CIRCLE_SIZE_MULT = 0.6f; //adjusts how big the base circles are

    private static float FIRST_PASS_STEPSIZE = 10f;  //1 = max quality, x1 range, 2 = lower quality, x2 range
    private static float FIRST_PASS_SUBDIV = 6f;  //= range when multiplied by stepSize, lowers performance quadratically
    private static float FIRST_PASS_DIVIDER = 1.25f;   //higher = more range, more "amalgamation", but edges get rougher
    private static float FIRST_PASS_DARKENING = -1f; //if >0, keeps existing shape as slightly darker, if <0 makes it pure white, erasing previous details
    private static float SECOND_PASS_STEPSIZE = 8f;
    private static float SECOND_PASS_SUBDIV = 7f;
    private static float SECOND_PASS_DIVIDER = 1.25f;
    private static float SECOND_PASS_DARKENING = 0.9f;
    private static float SMOOTHING_STEPSIZE = 1f;
    private static float SMOOTHING_SUBDIV = 6f;
    private static float SMOOTHING_DIVIDER = 2f;
    private static float SMOOTHING_DARKENING = 0.8f;


    private static final ShaderProgram shader = new ShaderProgram(Gdx.files.internal("anniv6Resources/shaders/shapeMaker/vertex.vs"),
                                                            Gdx.files.internal("anniv6Resources/shaders/shapeMaker/fragment.fs"));
    private static final FrameBuffer commonBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * FB_HEIGHT_FACTOR, false);
    private static final TextureRegion CIRCLE = new TextureRegion(TexLoader.getTexture(SpireAnniversary6Mod.makeImagePath("ui/WhiteCircle.png")));

    public static TextureRegion makeShape(AbstractZone zone, List<MapRoomNode> nodes, float anchorX, float anchorY, SpriteBatch sb) {
        if (zone.zoneFb == null) {
            zone.zoneFb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * FB_HEIGHT_FACTOR, false);
        }
        sb.setColor(Color.WHITE);


        //draw base circles
        sb.flush();
        commonBuffer.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glColorMask(true, true, true, true);
        float yScale = (float)Gdx.graphics.getHeight() / (float)commonBuffer.getHeight();
        HashMap<MapRoomNode, Float> circleScales = new HashMap<>();
        for (MapRoomNode n : nodes) {
            float d = getClosestNodeAndDistance(n, (no) -> !nodes.contains(no)).getValue();
            float circleScale = Math.min(d/(300f) * CIRCLE_SIZE_MULT, 0.9f);
            circleScales.put(n, circleScale);
            float cX = (float)n.x * SPACING_X + OFFSET_X + n.offsetX;
            float cY = (float)n.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY + n.offsetY;
            //main circles
            sb.draw(CIRCLE,
                    cX - (CIRCLE.getTexture().getWidth() * 0.5f) - anchorX + Gdx.graphics.getWidth()/2f,
                    yScale * (cY - (CIRCLE.getTexture().getHeight() * 0.5f) - (anchorY) + Gdx.graphics.getHeight()/4f),
                    CIRCLE.getTexture().getWidth() / 2f,
                    CIRCLE.getTexture().getHeight() / 2f,
                    CIRCLE.getTexture().getWidth(),
                    CIRCLE.getTexture().getHeight(),
                    Settings.scale * circleScale,
                    Settings.scale * circleScale * yScale,
                    0f);
        }

        //in-between circles. Each node in the zone looks to add a circle between it and nodes from the same zone if they are adjacent
        //(to the right and up to avoid drawing the same thing multiple times)
        for (MapRoomNode n : nodes) {
            for (MapRoomNode m : nodes) {
                boolean isMAboveAndClose = (m.y == n.y+1) && (Math.abs(m.x - n.x) <= 1);
                boolean isMToTheRight = m.y == n.y && m.x == n.x+1;
                if (isMAboveAndClose || isMToTheRight) {
                    float circleScale = (circleScales.get(n) + circleScales.get(m)) / 2f;
                    float nX = (float)n.x * SPACING_X + OFFSET_X + n.offsetX;
                    float nY = (float)n.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY + n.offsetY;
                    float mX = (float)m.x * SPACING_X + OFFSET_X + m.offsetX;
                    float mY = (float)m.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY + m.offsetY;

                    sb.draw(CIRCLE,
                            (nX+mX)*0.5f - (CIRCLE.getTexture().getWidth() * 0.5f) - anchorX + Gdx.graphics.getWidth()/2f,
                            yScale * ((nY+mY)*0.5f - (CIRCLE.getTexture().getHeight() * 0.5f) - (anchorY) + Gdx.graphics.getHeight()/4f),
                            CIRCLE.getTexture().getWidth() / 2f,
                            CIRCLE.getTexture().getHeight() / 2f,
                            CIRCLE.getTexture().getWidth(),
                            CIRCLE.getTexture().getHeight(),
                            Settings.scale * circleScale,
                            Settings.scale * circleScale * yScale,
                            0f);
                }
            }
        }


        sb.flush();
        commonBuffer.end();
        TextureRegion resultRegion = new TextureRegion(commonBuffer.getColorBufferTexture());
        resultRegion.flip(false, true);

        //start making the actual shape
        zone.zoneFb.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glColorMask(true, true, true, true);
        //first shader step (high range and rounding, low quality)
        sb.setShader(shader);
        shader.setUniformf("sizeX", (float)commonBuffer.getWidth());
        shader.setUniformf("sizeY", (float)commonBuffer.getHeight());
        shader.setUniformf("stepSize", FIRST_PASS_STEPSIZE); //1 = max quality, x1 range, 2 = lower quality, x2 range
        shader.setUniformf("subDiv", FIRST_PASS_SUBDIV); //= range when multiplied by stepSize, lowers performance quadratically
        shader.setUniformf("thresholdDivider", FIRST_PASS_DIVIDER); //higher = more range, less effective at rounding concave angles (like when 2 circles meet)
        shader.setUniformf("darkening", FIRST_PASS_DARKENING);

        sb.draw(resultRegion, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT
                ,resultRegion.getRegionWidth()/2f,resultRegion.getRegionHeight()/2f,
                resultRegion.getRegionWidth(),resultRegion.getRegionHeight(),
                Settings.scale, Settings.scale * yScale, 0);
        sb.flush();
        zone.zoneFb.end();

        resultRegion.setTexture(zone.zoneFb.getColorBufferTexture());

        //second inflation with low rounding
        commonBuffer.begin();
        shader.setUniformf("stepSize", SECOND_PASS_STEPSIZE);
        shader.setUniformf("subDiv", SECOND_PASS_SUBDIV);
        shader.setUniformf("thresholdDivider", SECOND_PASS_DIVIDER);
        shader.setUniformf("darkening", SECOND_PASS_DARKENING);

        sb.draw(resultRegion, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT
                ,resultRegion.getRegionWidth()/2f,resultRegion.getRegionHeight()/2f,
                resultRegion.getRegionWidth(),resultRegion.getRegionHeight(),
                Settings.scale, Settings.scale * yScale, 0);
        sb.flush();
        commonBuffer.end();

        resultRegion.setTexture(commonBuffer.getColorBufferTexture());

        //smoothing step (low range, high quality)
        shader.setUniformf("stepSize", SMOOTHING_STEPSIZE);
        shader.setUniformf("subDiv", SMOOTHING_SUBDIV);
        shader.setUniformf("thresholdDivider", SMOOTHING_DIVIDER);
        shader.setUniformf("darkening", SMOOTHING_DARKENING);

        zone.zoneFb.begin();
        sb.draw(resultRegion, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT
                ,resultRegion.getRegionWidth()/2f,resultRegion.getRegionHeight()/2f,
                resultRegion.getRegionWidth(),resultRegion.getRegionHeight(),
                Settings.scale, Settings.scale * yScale, 0);
        sb.flush();
        zone.zoneFb.end();

        sb.setShader(null);
        resultRegion.setTexture(zone.zoneFb.getColorBufferTexture());
        return resultRegion;
    }


    public static Pair<MapRoomNode,Float> getClosestNodeAndDistance(MapRoomNode center, Predicate<MapRoomNode> filter) {
        int centerFloor = -1;
        int centerIndex = -1;
        for (ArrayList<MapRoomNode> floor : AbstractDungeon.map) {
            if (floor.contains(center)) {
                centerFloor = AbstractDungeon.map.indexOf(floor);
                centerIndex = floor.indexOf(center);
                break;
            }
        }
        float minDistance = 300f;
        MapRoomNode closestNode = null;
        //check floor before
        if (centerFloor != 0) {
            for (MapRoomNode n : AbstractDungeon.map.get(centerFloor -1)) {
                if (filter.test(n)) {
                    float d = nodeDistance(center, n);
                    if (d < minDistance) {
                        minDistance = d;
                        closestNode = n;
                    }
                }
            }
        }
        //check floor after
        if (centerFloor < AbstractDungeon.map.size() - 2) {
            for (MapRoomNode n : AbstractDungeon.map.get(centerFloor + 1)) {
                if (filter.test(n)) {
                    float d = nodeDistance(center, n);
                    if (d < minDistance) {
                        minDistance = d;
                        closestNode = n;
                    }
                }
            }
        }
        //check left and right nodes
        ArrayList<MapRoomNode> floor = AbstractDungeon.map.get(centerFloor);
        if (centerIndex < floor.size() - 2) {
            if (filter.test(floor.get(centerIndex + 1))) {
                float d = nodeDistance(center, floor.get(centerIndex + 1));
                if (d < minDistance) {
                    minDistance = d;
                    closestNode = floor.get(centerIndex + 1);
                }
            }
        }
        if (centerIndex > 0) {
            if (filter.test(floor.get(centerIndex - 1))) {
                float d = nodeDistance(center, floor.get(centerIndex - 1));
                if (d < minDistance) {
                    minDistance = d;
                    closestNode = floor.get(centerIndex - 1);
                }
            }
        }

        return new Pair<>(closestNode,minDistance);
    }

    public static float nodeDistance(MapRoomNode n1, MapRoomNode n2) {
        return (float) Math.sqrt((n1.hb.cX - n2.hb.cX)*(n1.hb.cX - n2.hb.cX) + (n1.hb.cY - n2.hb.cY)*(n1.hb.cY - n2.hb.cY));
    }
}
