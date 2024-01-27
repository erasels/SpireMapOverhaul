package spireMapOverhaul.zones.divinitiesgaze.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.divinitiesgaze.DivinitiesGazeZone;

public class Inevitability extends AbstractSMOCard {
  public static String ID = SpireAnniversary6Mod.makeID("Inevitability");
  private static boolean canTalk = true;

  public Inevitability() {
    super(ID, DivinitiesGazeZone.ID, -2, CardType.SKILL, CardRarity.SPECIAL, CardTarget.ALL, CardColor.COLORLESS);
    this.magicNumber = this.baseMagicNumber = 5;
    this.block = this.baseBlock = 3;
    this.secondMagic = this.baseSecondMagic = 1;
    this.isMultiDamage = true;
    this.selfRetain = true;
  }

  @Override
  public void upp() {
    upgradeMagicNumber(2);
    upgradeBlock(1);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {}

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    return false;
  }

  @Override
  public void atTurnStart() {
    canTalk = true;
  }

  @Override
  public void onRetained() {
    this.flash();
    if(canTalk) {
      Wiz.atb(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[AbstractDungeon.cardRandomRng.random(0, cardStrings.EXTENDED_DESCRIPTION.length - 1)], 0.5F, 1.5F));
      canTalk = false;
    }
    Wiz.atb(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(this.magicNumber, true),
        DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
    Wiz.atb(new GainBlockAction(AbstractDungeon.player, this.block));
    Wiz.atb(new AbstractGameAction() {
      @Override
      public void update() {
        this.isDone = true;
        Inevitability.this.baseMagicNumber += Inevitability.this.secondMagic;
        Inevitability.this.baseBlock += Inevitability.this.secondMagic;
      }
    });
  }
}
