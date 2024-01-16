package spireMapOverhaul.zones.divinitiesgaze.divinities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.List;

public class DivineBeingManager {
  // TODO add a denylist to config maybe?
  private static final List<DivineBeing> DIVINITIES = new ArrayList<>();

  public static void register(DivineBeing divinity) {
    if(!DIVINITIES.contains(divinity)) {
      DIVINITIES.add(divinity);
    }
  }

  public static DivineBeing getDivinityForEvent() {
    return getDivinity(AbstractDungeon.eventRng);
  }

  public static DivineBeing getDivinityForCombat() {
    return getDivinity(AbstractDungeon.miscRng);
  }

  private static DivineBeing getDivinity(Random rng) {
    return DIVINITIES.size() > 0 ? DIVINITIES.get(rng.random(0, DIVINITIES.size() - 1)) : null;
  }
}
