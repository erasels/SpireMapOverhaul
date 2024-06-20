package spireMapOverhaul.zones.frostlands;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.*;
import spireMapOverhaul.zones.frostlands.monsters.Steward;
import spireMapOverhaul.zones.frostlands.powers.FrigidPower;
import spireMapOverhaul.zones.frostlands.vfx.SnowEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FrostlandsZone extends AbstractZone implements CombatModifyingZone, EncounterModifyingZone, RewardModifyingZone, ShopModifyingZone, RenderableZone, ModifiedEventRateZone{
    public static final String ID = "Frostlands";
    private Texture bg = TexLoader.getTexture(SpireAnniversary6Mod.makeBackgroundPath("frostlands/bg.png"));
    java.util.Random rand;

    public FrostlandsZone() {
        super(ID, Icons.MONSTER, Icons.EVENT);
        width = 1;
        height = 4;
        rand = new java.util.Random();
    }

    @Override
    public AbstractZone copy() {
        return new FrostlandsZone();
    }

    @Override
    public Color getColor() {
        return new Color(0.0f,0.35f, 0.5f, 0.5f);
    }

    @Override
    public boolean canSpawn() {
        return isAct(2);
    }

    @Override
    public void distributeRooms(Random rng, ArrayList<AbstractRoom> roomList) {
        for (MapRoomNode node : nodes) {
            switch (node.y - y) {
                case 0:
                case 2:
                    node.setRoom(roomOrDefault(roomList, (room)->room instanceof MonsterRoom, MonsterRoom::new));
                    break;
                case 1:
                    node.setRoom(roomOrDefault(roomList, (room)->room instanceof EventRoom, EventRoom::new));
                    break;
                case 3:
                    node.setRoom(new TreasureRoom());
                    break;
            }
        }
    }

    @Override
    public void atBattleStartPreDraw() {
        Wiz.applyToSelf(new FrigidPower(AbstractDungeon.player,1));
    }

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        return Arrays.asList(
            new ZoneEncounter(Steward.ID, 2, () -> new Steward(0.0f, 0.0f))
        );
    }

    @Override
    public void update() {
        if(rand.nextInt(20) == 0)
            Wiz.atb(new VFXAction(new SnowEffect()));
    }

    @Override
    public boolean canIncludeTreasureRow() {
        return false;
    }

    @Override
    public boolean canIncludeFinalCampfireRow() {
        return false;
    }

    @Override
    public boolean canIncludeEarlyRows() {
        return false;
    }

    @Override
    public boolean allowSideConnections() {
        return false;
    }

    @Override
    public boolean allowAdditionalPaths() {
        return false;
    }

    @Override
    public float zoneSpecificEventRate() {
        return 1f;
    }

    @Override
    public void postRenderCombatBackground(SpriteBatch sb) {
        sb.draw(bg, 0, 0, Settings.WIDTH, Settings.HEIGHT);
    }
}
