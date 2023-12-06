package spireMapOverhaul.util;

import basemod.Pair;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ZoneShapeMaker {

    private static final float OFFSET_X = Settings.isMobile ? 496.0F * Settings.xScale : 560.0F * Settings.xScale;
    private static final float OFFSET_Y = 180.0F * Settings.scale;
    private static final float SPACING_X = Settings.isMobile ? (int)(Settings.xScale * 64.0F) * 2.2F : (int)(Settings.xScale * 64.0F) * 2.0F;

    private static ShaderProgram shader = new ShaderProgram(Gdx.files.internal("anniv6Resources/shaders/shapeMaker/vertex.vs"),
                                                            Gdx.files.internal("anniv6Resources/shaders/shapeMaker/fragment.fs"));

    private static FrameBuffer commonBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

    private static final TextureRegion CIRCLE = new TextureRegion(TexLoader.getTexture(SpireAnniversary6Mod.makeImagePath("ui/whiteCircle.png")));

    public static TextureRegion makeShape(AbstractZone zone, List<MapRoomNode> nodes, float anchorX, float anchorY, SpriteBatch sb) {
        if (zone.zoneFb == null) {
            zone.zoneFb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        }


        //draw base circles
        sb.flush();
        commonBuffer.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glColorMask(true, true, true, true);

        for (MapRoomNode n : nodes) {
            float d = getClosestNodeAndDistance(n, (no) -> !nodes.contains(no)).getValue();
            float circleScale = Math.min(d/(300f), 1f);
            sb.draw(CIRCLE,
                    n.hb.cX - (CIRCLE.getTexture().getWidth() * 0.5f) + 300 - anchorX,
                    n.hb.cY - (CIRCLE.getTexture().getHeight() * 0.5f) + 300 - anchorY,
                    CIRCLE.getTexture().getWidth() / 2f,
                    CIRCLE.getTexture().getHeight() / 2f,
                    CIRCLE.getTexture().getWidth(),
                    CIRCLE.getTexture().getHeight(),
                    Settings.scale * circleScale,
                    Settings.scale * circleScale,
                    0f);
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
        //setShader
        sb.draw(resultRegion, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
        sb.flush();
        zone.zoneFb.end();
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
