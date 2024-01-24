package spireMapOverhaul.zones.divinitiesgaze.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.divinitiesgaze.DivinitiesGazeZone;

public class VerdictPower extends AbstractSMOPower implements HealthBarRenderPower {
  public static final String POWER_ID = SpireAnniversary6Mod.makeID("VerdictPower");
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  public static final String KEYWORD = SpireAnniversary6Mod.makeID("Verdict");

  public VerdictPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, DivinitiesGazeZone.ID, PowerType.DEBUFF, false, owner, amount);
  }

  @Override
  public void atStartOfTurn() {
    if (owner.currentHealth < this.amount) {
      Wiz.atb(new VFXAction(new WeightyImpactEffect(this.owner.hb.cX, this.owner.hb.cY, Color.WHITE.cpy())));
      Wiz.atb(new WaitAction(0.8F));
      Wiz.atb(new InstantKillAction(owner));
    } else {
      Wiz.atb(new ApplyPowerAction(this.owner, this.owner, new VerdictPower(this.owner, 5), 5));
    }
  }

  @Override
  public int getHealthBarAmount() {
    return this.amount;
  }

  @Override
  public Color getColor() {
    return Color.DARK_GRAY;
  }

  @Override
  public void updateDescription() {
    super.updateDescription();
    this.description = DESCRIPTIONS[0];
  }
}
