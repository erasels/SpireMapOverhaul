package spireMapOverhaul.zones.divinitiesgaze.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;

public class VerdictPower extends AbstractSMOPower {
  public static final String POWER_ID = SpireAnniversary6Mod.makeID("VerdictPower");
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public VerdictPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, amount);
  }

  @Override
  public void atStartOfTurn() {
    Wiz.atb(owner.currentHealth < this.amount
        ? new InstantKillAction(owner)
        : new ApplyPowerAction(this.owner, this.owner, new VerdictPower(this.owner, 5), 5));
  }
}
