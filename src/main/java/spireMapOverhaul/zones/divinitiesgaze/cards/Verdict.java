package spireMapOverhaul.zones.divinitiesgaze.cards;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.divinitiesgaze.DivinitiesGazeZone;
import spireMapOverhaul.zones.divinitiesgaze.powers.VerdictPower;

public class Verdict extends AbstractSMOCard {
  public static String ID = SpireAnniversary6Mod.makeID("Verdict");
  private static boolean canTalk = true;

  public Verdict() {
    super(ID, DivinitiesGazeZone.ID, 1, CardType.SKILL, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
    this.magicNumber = this.baseMagicNumber = 12;
    this.exhaust = true;
  }

  @Override
  public void upp() {
    upgradeMagicNumber(5);
  }

  @Override
  public void atTurnStart() {
    canTalk = true;
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    if(canTalk) {
      Wiz.atb(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 2.0F, 3.0F));
    }
    Wiz.atb(new ApplyPowerAction(abstractMonster, abstractPlayer, new VerdictPower(abstractMonster, this.magicNumber), this.magicNumber));
    canTalk = false;
  }
}
