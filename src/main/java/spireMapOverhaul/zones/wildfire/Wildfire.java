package spireMapOverhaul.zones.wildfire;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zones.wildfire.cardmods.BurningMod;
import spireMapOverhaul.zones.wildfire.powers.BurningPower;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.*;

//Credit for zone icon: https://game-icons.net/1x1/lorc/burning-tree.html
public class Wildfire extends AbstractZone implements CombatModifyingZone, RewardModifyingZone {
    public static final String ID = "Wildfire";
    public static ShaderProgram mapShader = new ShaderProgram(SpriteBatch.createDefaultShader().getVertexShaderSource(), Gdx.files.internal(makeShaderPath("wildfire/mapShader.frag")).readString(String.valueOf(StandardCharsets.UTF_8)));
    static FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
    public Wildfire() {
        super(ID, Icons.MONSTER);
        this.width = 2;
        this.maxWidth = 3;
        this.height = 3;
        this.maxHeight = 4;
    }

    @Override
    public AbstractZone copy() {
        return new Wildfire();
    }

    @Override
    public Color getColor() {
        return Color.FIREBRICK.cpy();
    }

    @Override
    public void renderOnMap(SpriteBatch sb, float alpha) {
        if(getShaderConfig() && alpha > 0) {
            sb.flush();
            fbo.begin();

            Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            super.renderOnMap(sb, alpha);
            sb.flush();
            fbo.end();


            TextureRegion region = new TextureRegion(fbo.getColorBufferTexture());
            region.flip(false, true);

            sb.setShader(mapShader);
            sb.setColor(Color.WHITE);
            mapShader.setUniformf("u_time", time);

            sb.draw(region, 0, 0);
            sb.setShader(null);
            sb.flush();
        } else {
            super.renderOnMap(sb, alpha);
        }
    }

    @Override
    public void atTurnStart() {
        Wiz.applyToSelf(new BurningPower(Wiz.adp(), 1));
        //Make monsters apply to themselves to not trigger Sadistic Nature
        Wiz.forAllMonstersLiving(mon -> Wiz.atb(new ApplyPowerAction(mon, mon, new BurningPower(mon, 1))));
    }

    @Override
    public String getCombatText() {
        return TEXT[3];
    }

    @Override
    protected boolean canIncludeTreasureRow() {
        return false;
    }

    @Override
    protected boolean canIncludeFinalCampfireRow() {
        return false;
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
    }

    @Override
    public void distributeRooms(Random rng, ArrayList<AbstractRoom> roomList) {
        // This makes all rooms in the zone either monster rooms (which could be elites) or campfires.
        // See the comment in Monster Zoo's distributeRooms method for a more detailed explanation of this approach.
        int half = nodes.size() / 2;
        int i;
        for (i = 0; i < half; ++i) {
            nodes.get(i).setRoom(roomOrDefault(roomList, (room) -> room instanceof MonsterRoom || room instanceof RestRoom, MonsterRoom::new));
        }
        for (; i < nodes.size(); ++i) {
            nodes.get(i).setRoom(new MonsterRoom());
        }
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            if (card.cost != -2 && card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
                int amount = card.cost * 2;
                if (card.cost == 0) {
                    amount = 1;
                } else if (card.cost == -1) {
                    amount = 2;
                }
                CardModifierManager.addModifier(card, new BurningMod(amount));
            }
        }
    }
}
