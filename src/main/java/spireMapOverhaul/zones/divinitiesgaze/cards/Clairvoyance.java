package spireMapOverhaul.zones.divinitiesgaze.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.cardmods.RetainMod;
import spireMapOverhaul.util.Wiz;

public class Clairvoyance extends AbstractSMOCard {

  public static String ID = SpireAnniversary6Mod.makeID("Clairvoyance");

  public Clairvoyance() {
    super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
    this.baseMagicNumber = this.magicNumber = 1;
    this.selfRetain = true;
    this.exhaust = true;
  }

  @Override
  public void upp() {
    upgradeMagicNumber(1);
    this.upgraded = true;
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.atb(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 2.0F, 3.0F));
  }

  @Override
  public void atTurnStart() {
    if(Wiz.hand().contains(this)) {
      Wiz.atb(new DrawCardAction(this.magicNumber, new AbstractGameAction() {
        @Override
        public void update() {
          for(AbstractCard c: DrawCardAction.drawnCards) {
            CardModifierManager.addModifier(c, new RetainMod(true));
          }
          this.isDone = true;
        }
      }));
    }
  }
}
