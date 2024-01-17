package spireMapOverhaul.zones.divinitiesgaze;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zones.divinitiesgaze.divinities.impl.Jurors;
import spireMapOverhaul.zones.divinitiesgaze.events.DivineVisitor;
import spireMapOverhaul.zones.divinitiesgaze.powers.VerdictPower;

public class DivinitiesGazeZone extends AbstractZone implements ModifiedEventRateZone, CombatModifyingZone {

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
  public String forceEvent() {
    return ModifiedEventRateZone.returnIfUnseen(DivineVisitor.ID);
  }

  @Override
  public void atPreBattle() {
    if (!Jurors.doApplyVerdictOnCombatStart) {
      return;
    }

    Jurors.doApplyVerdictOnCombatStart = false;
    Wiz.atb(new AbstractGameAction() {
      @Override
      public void update() {
        AbstractDungeon.getMonsters().monsters.forEach(m -> Wiz.att(new ApplyPowerAction(m, AbstractDungeon.player, new VerdictPower(m,  10), 10)));
        this.isDone = true;
      }
    });
  }
}
