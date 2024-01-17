package spireMapOverhaul.zones.divinitiesgaze.cards;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;

public class GuidingLight extends AbstractSMOCard {

  public static String ID = SpireAnniversary6Mod.makeID("GuidingLight");

  public GuidingLight() {
    super(ID, -2, CardType.SKILL, CardRarity.RARE, CardTarget.NONE, CardColor.COLORLESS);
    this.exhaust = true;
  }

  @Override
  public void upgrade() {}

  @Override
  public void upp() {}

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
  }

  @Override
  public void triggerWhenDrawn() {
    Wiz.atb(new ExpertiseAction(Wiz.p(), BaseMod.MAX_HAND_SIZE));
    Wiz.atb(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 2.0F, 3.0F));
  }

  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
    return false;
  }

}
