package spireMapOverhaul.zones.divinitiesgaze.divinities;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.*;

public class DivineBeingManager {
  private static final Map<String, DivineBeing> DIVINITIES = new LinkedHashMap<>();
  private static final List<String> DIVINITY_IDS = new ArrayList<>();
  private static DivineBeing override = null;

  static {
    new AutoAdd(SpireAnniversary6Mod.modID)
        .packageFilter(SpireAnniversary6Mod.class)
        .any(BaseDivineBeing.class, (info, divinity) -> register(divinity));
  }

  public static void register(BaseDivineBeing divineBeing) {
    DIVINITIES.put(divineBeing.getDivinityId(), divineBeing);
    DIVINITY_IDS.add(divineBeing.getDivinityId());
  }

  public static DivineBeing getDivinityForEvent() {
    return getDivinity(AbstractDungeon.eventRng);
  }

  public static DivineBeing getDivinityForCombat() {
    return getDivinity(AbstractDungeon.miscRng);
  }

  private static DivineBeing getDivinity(Random rng) {
    if(override != null) {
      return override;
    }

    return DIVINITIES.size() > 0
        ? DIVINITIES.get(DIVINITY_IDS.get(rng.random(0, DIVINITIES.size() - 1)))
        : null;
  }

  public static void setOverrideDivinity(String id) {
    override = DIVINITIES.get(id);
  }

  public static List<String> getDivinityIds() {
    return DIVINITY_IDS;
  }
}
