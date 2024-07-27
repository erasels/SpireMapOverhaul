package spireMapOverhaul.zones.gremlincamp;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.GremlinMatchGame;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zones.gremlincamp.monsters.GremlinBodyguard;
import spireMapOverhaul.zones.gremlincamp.monsters.GremlinCook;
import spireMapOverhaul.zones.gremlincamp.monsters.GremlinDog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class GremlinCamp extends AbstractZone implements EncounterModifyingZone, CombatModifyingZone, CampfireModifyingZone, ModifiedEventRateZone {
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
    public static final String BRAWL_BROS = makeID("GremlinBrawlers");
    public static final String GREMLIN_GANG = makeID("GremlinGang");
    public static final String GREMLIN_DOGGOS = makeID("GremlinDoggos");

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        if (encounter_names == null)
            encounter_names = CardCrawlGame.languagePack.getUIString(makeID("GCEncounterNames")).TEXT;
        ArrayList<ZoneEncounter> encs = new ArrayList<>();
        encs.add(new ZoneEncounter(GET_DOWN_MR_PRESIDENT, 1, () ->
                new MonsterGroup(new AbstractMonster[]{
                        new GremlinBodyguard(-200f, 10),
                        new GremlinWizard(0, 0),
                        new GremlinBodyguard(200f, -10)
                }), encounter_names[1])
        );
        encs.add(new ZoneEncounter(BRAWL_BROS, 1, () ->
                new MonsterGroup(new AbstractMonster[]{
                        new GremlinFat(-100f, 10),
                        new GremlinWarrior(100f, -10)
                }), encounter_names[0])
        );
        encs.add(new ZoneEncounter(GREMLIN_GANG, 1, () ->
                (MonsterGroup) ReflectionHacks.privateStaticMethod(MonsterHelper.class, "spawnGremlins").invoke(),
                MonsterHelper.MIXED_COMBAT_NAMES[0])
        );
        encs.add(new ZoneEncounter(GREMLIN_DOGGOS, 1, () ->
                new MonsterGroup(new AbstractMonster[]{
                        new GremlinDog(-250f, -10),
                        new GremlinDog(100f, 17)
                }), encounter_names[3])
        );
        return encs;
    }

    public static final String NOBBERS = makeID("ClassicNob");
    public static final String WHO_LET_EM_COOK = makeID("GremlinCookGang");

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        ArrayList<ZoneEncounter> encs = new ArrayList<>();
        encs.add(new ZoneEncounter(NOBBERS, 1, () -> new MonsterGroup(new AbstractMonster[] {new GremlinNob(0, 0)}), GremlinNob.NAME));
        encs.add(new ZoneEncounter(WHO_LET_EM_COOK, 1, () -> {
                    AbstractMonster grem1 = getGremlin(-200, -7, "");
                    return new MonsterGroup(new AbstractMonster[]{
                            grem1,
                            getGremlin(-10, 8, grem1.id),
                            new GremlinCook(200, -1)
                    });
                }, encounter_names[2])
        );
        return encs;
    }

    private AbstractMonster getGremlin(float x, float y, String prev) {
        ArrayList<String> gremlinPool = new ArrayList<>();
        gremlinPool.add(GremlinWarrior.ID);
        gremlinPool.add(GremlinThief.ID);
        gremlinPool.add(GremlinFat.ID);
        gremlinPool.add(GremlinBodyguard.ID);
        gremlinPool.removeIf(id -> id.equals(prev));
        String id = Wiz.getRandomItem(gremlinPool, AbstractDungeon.miscRng);
        if (GremlinBodyguard.ID.equals(id)) {
            return new GremlinBodyguard(x, y);
        } else {
            return MonsterHelper.getGremlin(id, x, y);
        }
    }

    @Override
    public void atPreBattle() {
        if (AbstractDungeon.getCurrRoom().monsters == null) {
            return;
        }
        if (GET_DOWN_MR_PRESIDENT.equals(AbstractDungeon.lastCombatMetricKey)) {
            // President starts with 1 Buffer
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (GremlinWizard.ID.equals(m.id)) {
                    Wiz.atb(new ApplyPowerAction(m, null, new BufferPower(m, 1), 1, true));
                    break;
                }
            }
        } else if (BRAWL_BROS.equals(AbstractDungeon.lastCombatMetricKey)) {
            // Brawlers have 50% more HP and Fat has Nob's Anger
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                Wiz.atb(new IncreaseMaxHpAction(m, 0.5f, false));
                if (GremlinFat.ID.equals(m.id)) {
                    RitualPower p = new RitualPower(m, 2, false);
                    ReflectionHacks.setPrivate(p, RitualPower.class, "skipFirst", false);
                    Wiz.atb(new ApplyPowerAction(m, null, p, 2, true));
                }
            }
        } else if (WHO_LET_EM_COOK.equals(AbstractDungeon.lastCombatMetricKey)) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!GremlinCook.ID.equals(m.id)) {
                    // Calculate the increase to make the max HP double or up to 40
                    int currentMaxHp = m.maxHealth;
                    int desiredMaxHp = Math.min(currentMaxHp * 2, 40);
                    int hpIncrease = desiredMaxHp - currentMaxHp;
                    hpIncrease = Math.max(hpIncrease, 0);

                    // Convert hpIncrease to float percentage of the current max HP
                    float increasePercentage = ((float) hpIncrease / currentMaxHp);
                    Wiz.atb(new IncreaseMaxHpAction(m, increasePercentage, false));
                }
            }
        }
    }

    @Override
    public void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {
        for (AbstractCampfireOption c : buttons) {
            if (c instanceof RestOption && c.usable) {
                c.usable = false;
                ((RestOption) c).updateUsability(false);
                break;
            }
        }
        buttons.add(new ScavengeOption());
    }

    @Override
    public Set<String> addSpecificEvents() {
        Set<String> events = new HashSet<>();
        events.add(GremlinMatchGame.ID);
        events.add(GremlinWheelGame.ID);
        return events;
    }

    @Override
    public float zoneSpecificEventRate() {
        return 1f;
    }

    @Override
    public void replaceRooms(Random rng) {
        //Replace all shops with event rooms
        for (MapRoomNode node : this.nodes) {
            if (node.room instanceof ShopRoom) {
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
