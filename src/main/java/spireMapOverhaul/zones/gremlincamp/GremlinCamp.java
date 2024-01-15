package spireMapOverhaul.zones.gremlincamp;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.AngerPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.ActUtil;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zones.gremlincamp.monsters.GremlinBodyguard;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class GremlinCamp extends AbstractZone implements EncounterModifyingZone, CombatModifyingZone, CampfireModifyingZone {
    public static final String ID = "GremlinCamp";
    private static String[] encounter_names = null;

    public GremlinCamp() {
        super(ID, Icons.MONSTER, Icons.REST, Icons.EVENT);
        this.width = 3;
        this.maxWidth = 4;
        this.height = 3;
        this.maxHeight = 4;
    }

    public static final String GET_DOWN_MR_PRESIDENT = makeID("GremlinPrez");
    public static final String BRAWl_BROS = makeID("GremlinBrawlers");
    public static final String GREMLIN_GANG = makeID("GremlinGang");

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        if(encounter_names == null) encounter_names = CardCrawlGame.languagePack.getUIString(makeID("GCEncounterNames")).TEXT;
        ArrayList<ZoneEncounter> encs = new ArrayList<>();
        encs.add(new ZoneEncounter(GET_DOWN_MR_PRESIDENT, 1, () ->
                new MonsterGroup(new AbstractMonster[] {
                        new GremlinBodyguard(-200f, 10),
                        new GremlinWizard(0, 0),
                        new GremlinBodyguard(200f, -10),
                }), encounter_names[1])
        );
        encs.add(new ZoneEncounter(BRAWl_BROS, 1, () ->
                new MonsterGroup(new AbstractMonster[] {
                        new GremlinFat(-100f, 10),
                        new GremlinWarrior(100f, -10),
                }), encounter_names[0])
        );
        encs.add(new ZoneEncounter(GREMLIN_GANG, 1, () ->
                (MonsterGroup) ReflectionHacks.privateStaticMethod(MonsterHelper.class, "spawnGremlins").invoke(),
                MonsterHelper.MIXED_COMBAT_NAMES[0])
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
    public void atPreBattle() {
        if(GET_DOWN_MR_PRESIDENT.equals(AbstractDungeon.lastCombatMetricKey)) {
            // President starts with 1 Buffer
            for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if(GremlinWizard.ID.equals(m.id)) {
                    Wiz.atb(new ApplyPowerAction(m, null, new BufferPower(m, 1), 1, true));
                    break;
                }
            }
        } else if(BRAWl_BROS.equals(AbstractDungeon.lastCombatMetricKey)) {
            // Brawlers have 50% more HP and Fat has Nob's Anger
            for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                Wiz.atb(new IncreaseMaxHpAction(m, 0.5f, false));
                if(GremlinFat.ID.equals(m.id)) {
                    Wiz.atb(new ApplyPowerAction(m, null, new RitualPower(m, 2, false), 2, true));
                }
            }
        }
    }

    @Override
    public void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {
        for(AbstractCampfireOption c : buttons) {
            if(c instanceof RestOption && c.usable) {
                c.usable = false;
                ((RestOption) c).updateUsability(false);
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

    //I think the encounters might be a bit too hard for the first battle
    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
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
