package spireMapOverhaul.zones.divinitiesgaze;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.events.AbstractEvent;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;

public class DivinitiesGazeZone extends AbstractZone implements ModifiedEventRateZone {

  public static final String ID = "DivinitiesGaze";

  public DivinitiesGazeZone() {
    super(ID);
    this.width = 3;
    this.height = 4;

  }

  @Override
  public AbstractZone copy() {
    return new DivinitiesGazeZone();
  }

  @Override
  public Color getColor() {
    return new Color(0.804f, 1f, 0.529f, 0.43f);
  }

  @Override
  public AbstractEvent forceEvent() {
    return ModifiedEventRateZone.super.forceEvent();
  }
}
