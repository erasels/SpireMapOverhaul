package spireMapOverhaul.zones.divinitiesgaze.cards;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.divinitiesgaze.DivinitiesGazeZone;

public class GuidingLight extends AbstractSMOCard {
  public static String ID = SpireAnniversary6Mod.makeID("GuidingLight");
  public static boolean canTalk = true;

  public GuidingLight() {
    super(ID, DivinitiesGazeZone.ID,  -2, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
    this.exhaust = true;
  }

  @Override
  public void upp() {}

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
  }

  @Override
  public void atTurnStart() {
    canTalk = true;
  }

  @Override
  public void triggerWhenDrawn() {
    Wiz.atb(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
    Wiz.atb(new ExpertiseAction(Wiz.p(), BaseMod.MAX_HAND_SIZE));
    if(this.upgraded) {
      Wiz.atb(new ArmamentsAction(true));
    }
    if(canTalk) {
      Wiz.atb(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 2.0F, 3.0F));
    }
    canTalk = false;
  }

  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
    return false;
  }

}
