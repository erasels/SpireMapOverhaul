package spireMapOverhaul.zones.frostlands;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.*;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.*;
import spireMapOverhaul.zones.frostlands.monsters.*;
import spireMapOverhaul.zones.frostlands.powers.FrigidPower;
import spireMapOverhaul.zones.frostlands.relics.Frostcoal;
import spireMapOverhaul.zones.frostlands.relics.OldHat;
import spireMapOverhaul.zones.frostlands.relics.SpruceCharm;
import spireMapOverhaul.zones.frostlands.vfx.SnowEffect;
import spireMapOverhaul.zones.invasion.InvasionUtil;
import spireMapOverhaul.zones.invasion.cards.HandOfTheAbyss;
import spireMapOverhaul.zones.manasurge.powers.ManaSurgePower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.modID;

public class FrostlandsZone extends AbstractZone implements CombatModifyingZone, EncounterModifyingZone, RewardModifyingZone, ShopModifyingZone, RenderableZone, ModifiedEventRateZone{
    public static final String ID = "Frostlands";
    private static final String STEWARD = SpireAnniversary6Mod.makeID("STEWARD");
    java.util.Random rand;
    private static final int ELITE_EXTRA_GOLD = 100;

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
}
