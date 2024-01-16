package spireMapOverhaul.zoneInterfaces;

import basemod.BaseMod;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface EncounterModifyingZone {
    /**
     * Defines the set of normal encounters for the zone, replacing all normal combats.
     * If this is non-null, normal encounters in the zone will be randomly selected from this list.
     * No normal encounter will be fought twice in a row; if only one is defined, the zone will fall back to the
     * encounters defined by the current act.
     * @return The list of normal encounters for the zone.
     */
    default List<ZoneEncounter> getNormalEncounters() { return null; }
    /**
     * Defines the set of elite encounters for the zone, replacing all elite combats.
     * If this is non-null, elite encounters in the zone will be randomly selected from this list.
     * No elite encounter will be fought twice in a row; if only one is defined, the zone will fall back to the
     * encounters defined by the current act.
     * @return The list of elite encounters for the zone.
     */
    default List<ZoneEncounter> getEliteEncounters() { return null; }

    /**
     * Gets additional monsters that should be added to encounters in the zone (in addition to the enemies already present).
     * If this is non-null, the monsters will be added to the current encounter.
     * Note that as part of this method, you can check the current act (use ActUtil.getRealActNum()), the room type
     * (e.g. AbstractDungeon.getCurrRoom instanceof MonsterRoomElite), etc.
     * The x position of the monster is automatically determined and will be to the left of all the enemies already present,
     * with space automatically allocated based on the size of the monster's hitbox. (Any x position set as part of the
     * monster's constructor will be overwritten.)
     * Keep the number of size of additional monsters small to avoid them taking up so much space that they overlap with
     * the player when added to fights that already have many enemies.
     * @return The monsters to add to the encounter.
     */
    default List<AbstractMonster> getAdditionalMonsters() { return null; }

    /**
     * Defines a new encounter in a zone.
     * An encounter requires an ID, which should be the same as the monster ID for single enemy encounters. This is
     * needed by BaseMod (and can be used to fight your new encounter from the console with the fight command).
     * An encounter also requires an act number, and will only show up in that act.
     * The enemies in an encounter are defined by a function that returns either a single monster or a MonsterGroup.
     * An encounter may have a special name. If no name is provided, the encounter will be named based on the enemies.
     */
    public static class ZoneEncounter {
        private String id;
        private int actNum;
        private Supplier<MonsterGroup> f;
        private String name;

        public ZoneEncounter(String id, int actNum, BaseMod.GetMonster f) {
            this(id, actNum, () -> new MonsterGroup(f.get()), null);
        }

        public ZoneEncounter(String id, int actNum, BaseMod.GetMonsterGroup f) {
            this(id, actNum, f, null);
        }

        public ZoneEncounter(String id, int actNum, BaseMod.GetMonster f, String name) {
            this(id, actNum, () -> new MonsterGroup(f.get()), name);
        }

        public ZoneEncounter(String id, int actNum, BaseMod.GetMonsterGroup f, String name) {
            this.id = id;
            this.actNum = actNum;
            this.f = f::get;
            this.name = name;
        }

        public String getID() {
            return id;
        }

        public int getActNum() {
            return actNum;
        }

        public Supplier<MonsterGroup> getMonsterSupplier() {
            return f;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Lets you change the encounter however you want.
     * You should not need to use this unless you're doing something unusual.
     * @param monsterGroup The encounter.
     * @param encounterID The id of the encounter
     * @return The changed encounter.
     */
    default MonsterGroup changeEncounter(MonsterGroup monsterGroup, String encounterID) { return monsterGroup; }

    /**
     * Registers the encounters in the zone with BaseMod.
     * You should not need to override this unless you're doing something unusual.
     * If you do override this, make sure you either call EncounterModifyingZone.super.registerEncounters() or
     * register everything defined in getNormalEncounters and getEliteEncounters yourself.
     */
    default void registerEncounters() {
        List<ZoneEncounter> normalEncounters = this.getNormalEncounters();
        List<ZoneEncounter> eliteEncounters = this.getEliteEncounters();
        List<ZoneEncounter> encounters = new ArrayList<>();
        if (normalEncounters != null) {
            encounters.addAll(normalEncounters);
        }
        if (eliteEncounters != null) {
            encounters.addAll(eliteEncounters);
        }

        for (ZoneEncounter ze : encounters) {
            BaseMod.addMonster(ze.getID(), ze.getName(), () -> ze.getMonsterSupplier().get());
        }
    }
}
