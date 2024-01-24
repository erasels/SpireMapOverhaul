package spireMapOverhaul.zones.divinitiesgaze.divinities;

import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.divinitiesgaze.DivinitiesGazeZone;

public abstract class BaseDivineBeing implements DivineBeing {
  public static final String SELECT_TEXT = "SELECT_TEXT";
  protected final DivinityStrings divinityStrings;

  public BaseDivineBeing(String boonCardId, String baneCardId) {
    this.divinityStrings = new DivinityStrings(SpireAnniversary6Mod.makeID(DivinitiesGazeZone.ID + "_Divinity_" + this.getClass().getSimpleName()),
        boonCardId, baneCardId,
        SpireAnniversary6Mod.makeEventPath(DivinitiesGazeZone.ID + "/" + this.getClass().getSimpleName()+".png"));
  }

  @Override
  public DivinityStrings getDivinityStrings() {
    return divinityStrings;
  }
}
