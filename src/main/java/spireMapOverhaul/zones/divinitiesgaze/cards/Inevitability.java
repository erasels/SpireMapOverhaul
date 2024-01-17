package spireMapOverhaul.zones.divinitiesgaze.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;

public class Inevitability extends AbstractSMOCard {
  public static String ID = SpireAnniversary6Mod.makeID("Inevitability");

  public Inevitability() {
    super(ID, -2, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL, CardColor.COLORLESS);
    this.damage = this.baseDamage = 6;
    this.block = this.baseBlock = 4;
    this.magicNumber = this.baseMagicNumber = 1;
    this.isMultiDamage = true;
    this.selfRetain = true;
  }

  @Override
  public void upp() {}

  @Override
  public void upgrade() {}

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {}

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    return false;
  }

  @Override
  public void onRetained() {
    Wiz.atb(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
    Wiz.atb(new GainBlockAction(AbstractDungeon.player, this.block));
    Wiz.atb(new AbstractGameAction() {
      @Override
      public void update() {
        this.isDone = true;
        Inevitability.this.baseDamage += Inevitability.this.magicNumber;
        Inevitability.this.baseBlock += Inevitability.this.magicNumber;
      }
    });
  }
}
