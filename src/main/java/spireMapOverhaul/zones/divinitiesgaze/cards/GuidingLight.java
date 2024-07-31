package spireMapOverhaul.zones.divinitiesgaze.cards;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.divinitiesgaze.DivinitiesGazeZone;

public class GuidingLight extends AbstractSMOCard {
  public static String ID = SpireAnniversary6Mod.makeID("GuidingLight");

  public GuidingLight() {
    super(ID, DivinitiesGazeZone.ID,  1, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
    this.baseMagicNumber = this.magicNumber = 2;
    this.isEthereal = true;
  }

  @Override
  public void upp() {
    this.upgradeMagicNumber(1);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.atb(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 2.0F, 3.0F));
    Wiz.atb(new DrawCardAction(this.magicNumber));
    Wiz.atb(new ArmamentsAction(true));
  }
}
