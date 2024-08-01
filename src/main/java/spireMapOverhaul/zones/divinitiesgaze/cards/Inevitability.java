package spireMapOverhaul.zones.divinitiesgaze.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.divinitiesgaze.DivinitiesGazeZone;

public class Inevitability extends AbstractSMOCard {
  public static String ID = SpireAnniversary6Mod.makeID("Inevitability");

  public Inevitability() {
    super(ID, DivinitiesGazeZone.ID, 0, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
    this.damage = this.baseDamage = 6;
    this.block = this.baseBlock = 5;
    this.magicNumber = this.baseMagicNumber = 3;
  }

  @Override
  public void upp() {
    upgradeDamage(3);
    upgradeBlock(3);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    blck();
    Wiz.atb(new TalkAction(true,
        cardStrings.EXTENDED_DESCRIPTION[AbstractDungeon.cardRandomRng.random(0, cardStrings.EXTENDED_DESCRIPTION.length - 1)],
        0.5F, 1.5F));
    dmg(abstractMonster, AbstractGameAction.AttackEffect.SMASH);
    Wiz.atb(new AbstractGameAction() {
      @Override
      public void update() {
        this.isDone = true;
        Inevitability.this.baseDamage += magicNumber;
        Inevitability.this.baseBlock += magicNumber;
      }
    });
    Wiz.att(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, false, false,
        (float)Settings.WIDTH * (float)0.66, (float)Settings.HEIGHT * (float)0.5));
  }
}
