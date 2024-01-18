package spireMapOverhaul.zones.divinitiesgaze.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.divinitiesgaze.powers.VerdictPower;

public class Verdict extends AbstractSMOCard {
  public static String ID = SpireAnniversary6Mod.makeID("Verdict");

  public Verdict() {
    super(ID, 2, CardType.SKILL, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
    this.magicNumber = this.baseMagicNumber = 10;
    this.exhaust = true;
  }

  @Override
  public void upp() {
    upgradeMagicNumber(5);
    this.upgraded = true;
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.atb(new ApplyPowerAction(abstractMonster, abstractPlayer, new VerdictPower(abstractMonster, this.magicNumber), this.magicNumber));
  }
}
