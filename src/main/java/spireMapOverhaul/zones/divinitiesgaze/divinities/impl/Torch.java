package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.divinitiesgaze.cards.GuidingLight;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Torch extends BaseDivineBeing {

  public Torch() {
    super(GuidingLight.ID, Burn.ID);
  }

  @Override
  public Consumer<Integer> doEventButtonAction() {
    System.out.println("got consumer");
    return (x) -> {
      System.out.println("invoked consumer");
      Wiz.atb(new HealAction(AbstractDungeon.player, AbstractDungeon.player, (int)Math.floor(AbstractDungeon.player.maxHealth * 0.10f)));
      Wiz.atb(new AbstractGameAction() {
        @Override
        public void update() {
          this.isDone = true;
          List<AbstractCard> availableCards = AbstractDungeon.player.masterDeck.group.stream().filter(AbstractCard::canUpgrade).collect(Collectors.toList());

          if(availableCards.isEmpty()) {
            return;
          }

          AbstractCard card = availableCards.get(AbstractDungeon.eventRng.random(0, availableCards.size()));
          card.upgrade();
          AbstractDungeon.player.bottledCardUpgradeCheck(card);
          AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
          AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
        }
      });
    };
  }

  @Override
  public String getEventButtonText() {
    return String.format(super.getEventButtonText(), (int)Math.floor(AbstractDungeon.player.maxHealth * 0.10f));
  }
}
