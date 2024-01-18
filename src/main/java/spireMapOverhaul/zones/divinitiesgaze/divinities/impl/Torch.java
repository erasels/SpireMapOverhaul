package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.zones.divinitiesgaze.cards.GuidingLight;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Torch extends BaseDivineBeing {

  public Torch() {
    super(GuidingLight.ID, Burn.ID);
  }

  @Override
  public Consumer<Integer> doEventButtonAction() {
    return (x) -> {
      AbstractDungeon.player.heal((int)Math.floor(AbstractDungeon.player.maxHealth * 0.10f));
      List<AbstractCard> availableCards = AbstractDungeon.player.masterDeck.group.stream().filter(AbstractCard::canUpgrade).collect(Collectors.toList());

      if(availableCards.isEmpty()) {
        return;
      }

      AbstractCard card = availableCards.get(AbstractDungeon.eventRng.random(0, availableCards.size() - 1));
      card.upgrade();
      AbstractDungeon.player.bottledCardUpgradeCheck(card);
      AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
      AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
    };
  }

  @Override
  public String getEventButtonText() {
    return String.format(super.getEventButtonText(), (int)Math.floor(AbstractDungeon.player.maxHealth * (AbstractDungeon.ascensionLevel >= 15 ? 0.10f : 0.15f)));
  }

  @Override
  public Supplier<Boolean> isEventOptionEnabled() {
    return () -> AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth
        || AbstractDungeon.player.masterDeck.group.stream().anyMatch(AbstractCard::canUpgrade);
  }
}
