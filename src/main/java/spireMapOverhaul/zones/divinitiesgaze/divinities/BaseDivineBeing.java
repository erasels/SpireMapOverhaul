package spireMapOverhaul.zones.divinitiesgaze.divinities;

import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.divinitiesgaze.DivinitiesGazeZone;

public abstract class BaseDivineBeing implements DivineBeing {
  public static final String SELECT_TEXT = "SELECT_TEXT";
  protected final DivinityStrings divinityStrings;
  private final String DIVINITY_ID;

  public BaseDivineBeing(String divinityId, String boonCardId, String baneCardId) {
    this.DIVINITY_ID = divinityId;
    this.divinityStrings = new DivinityStrings(SpireAnniversary6Mod.makeID(DivinitiesGazeZone.ID + "_Divinity_" + divinityId),
        boonCardId, baneCardId,
        SpireAnniversary6Mod.makeEventPath(DivinitiesGazeZone.ID + "/" + DIVINITY_ID + ".png"));
  }

  @Override
  public DivinityStrings getDivinityStrings() {
    return divinityStrings;
  }

  public String getDivinityId() {
    return DIVINITY_ID;
  }
}
