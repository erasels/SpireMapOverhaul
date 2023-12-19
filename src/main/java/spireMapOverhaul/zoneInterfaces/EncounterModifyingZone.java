package spireMapOverhaul.zoneInterfaces;

import basemod.BaseMod;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

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
     * Defines a new encounter in a zone.
     * An encounter requires an ID, which should be the same as the monster ID for single enemy encounters. This is
     * needed by BaseMod (and can be used to fight your new encounter from the console with the fight command).
     * The enemies in an encounter are defined by a function that returns either a single monster or a MonsterGroup.
     * An encounter may have a special name. If no name is provided, the encounter will be named based on the enemies.
     */
    public static class ZoneEncounter {
        private String id;
        private Supplier<MonsterGroup> f;
        private String name;

        public ZoneEncounter(String id, BaseMod.GetMonster f) {
            this(id, () -> new MonsterGroup(f.get()), null);
        }

        public ZoneEncounter(String id, BaseMod.GetMonsterGroup f) {
            this(id, f, null);
        }

        public ZoneEncounter(String id, BaseMod.GetMonster f, String name) {
            this(id, () -> new MonsterGroup(f.get()), name);
        }

        public ZoneEncounter(String id, BaseMod.GetMonsterGroup f, String name) {
            this.id = id;
            this.f = f::get;
            this.name = name;
        }

        public String getID() {
            return id;
        }

        public Supplier<MonsterGroup> getMonsterSupplier() {
            return f;
        }

        public String getName() {
            return name;
        }
    }
}
