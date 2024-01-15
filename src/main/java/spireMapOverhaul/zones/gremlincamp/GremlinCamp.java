package spireMapOverhaul.zones.gremlincamp;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.monsters.exordium.GremlinTsundere;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWizard;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.ActUtil;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class GremlinCamp extends AbstractZone implements EncounterModifyingZone, CampfireModifyingZone {
    public static final String ID = "GremlinCamp";

    public GremlinCamp() {
        super(ID, Icons.MONSTER, Icons.REST, Icons.EVENT);
        this.width = 3;
        this.maxWidth = 4;
        this.height = 3;
        this.maxHeight = 4;
    }

    public static final String GET_DOWN_MR_PRESIDENT = makeID("PoTGC");

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        ArrayList<ZoneEncounter> encs = new ArrayList<>();
        encs.add(new ZoneEncounter(GET_DOWN_MR_PRESIDENT, 1, () ->
                new MonsterGroup(new AbstractMonster[] {
                        new GremlinTsundere(-200f, 10),
                        new GremlinWizard(0, 0),
                        new GremlinTsundere(200f, -10),
                }), GremlinNob.NAME)
        );
        return encs;
    }


    public static final String NOBBERS = makeID("ClassicNob");
    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        ArrayList<ZoneEncounter> encs = new ArrayList<>();
        encs.add(new ZoneEncounter(NOBBERS, 1, () -> new MonsterGroup(new AbstractMonster[] {new GremlinNob(0, 0)}), GremlinNob.NAME));
        return encs;
    }

    @Override
    public MonsterGroup changeEncounter(MonsterGroup monsterGroup, String encounterID) {
        // President has 1 buffer
        if(GET_DOWN_MR_PRESIDENT.equals(encounterID)) {
            AbstractMonster prez = monsterGroup.getMonster(GremlinWizard.ID);
            Wiz.applyToEnemy(prez, new BufferPower(prez, 1));
        }
        return monsterGroup;
    }

    @Override
    public void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {
        for(AbstractCampfireOption c : buttons) {
            if(c instanceof RestOption && c.usable) {
                c.usable = false;
                ((RestOption) c).updateUsability(false); //TODO: Check text
                break;
            }
        }
    }

    @Override
    public void replaceRooms(Random rng) {
        //Replace all shops with event rooms
        for (MapRoomNode node : this.nodes) {
            if(node.room instanceof ShopRoom) {
                node.setRoom(new EventRoom());
            }
        }
    }

    @Override
    public AbstractZone copy() {
        return new GremlinCamp();
    }

    @Override
    public Color getColor() {
        return Color.MAROON.cpy();
    }

    @Override
    public boolean canSpawn() {
        return isAct(1);
    }
}
