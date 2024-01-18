package spireMapOverhaul.zones.divinitiesgaze.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EntanglePower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.divinitiesgaze.DivinitiesGazeZone;

public class Inevitability extends AbstractSMOCard {
  public static String ID = SpireAnniversary6Mod.makeID("Inevitability");
  private static boolean canTalk = true;

  public Inevitability() {
    super(ID, DivinitiesGazeZone.ID, -2, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ALL, CardColor.COLORLESS);
    this.damage = this.baseDamage = 5;
    this.block = this.baseBlock = 3;
    this.magicNumber = this.baseMagicNumber = 1;
    this.isMultiDamage = true;
    this.selfRetain = true;
  }

  @Override
  public void upp() {
    upgradeDamage(2);
    upgradeBlock(1);
    this.upgraded = true;
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
    if(AbstractDungeon.player.hasPower(EntanglePower.POWER_ID)) {
      // do not trigger if cannot attack
      return;
    }

    this.flash();
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
    if(canTalk) {
      Wiz.atb(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[AbstractDungeon.cardRandomRng.random(0, cardStrings.EXTENDED_DESCRIPTION.length - 1)], 2.0F, 3.0F));
    }
    canTalk = false;
  }
}
