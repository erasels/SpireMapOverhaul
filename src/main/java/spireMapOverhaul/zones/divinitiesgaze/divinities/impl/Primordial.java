package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.zones.divinitiesgaze.cards.Mitosis;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class Primordial extends BaseDivineBeing {

  public static final String ID = "Primordial";

  public Primordial() {
    super(ID, Mitosis.ID, Slimed.ID);
  }

  @Override
  public Consumer<Integer> doEventButtonAction() {
    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    AbstractDungeon.player.masterDeck.group.stream()
        .filter(X -> X.type != AbstractCard.CardType.CURSE)
        .forEach(group::addToBottom);

    return (i) -> AbstractDungeon.gridSelectScreen.open(group, 1, getDivinityStrings().getMiscText().get(SELECT_TEXT), false);
  }

  @Override
  public boolean doUpdate() {
    if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
      AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

      List<AbstractCard> targets = new ArrayList<>(AbstractDungeon.player.masterDeck.group);
      targets.remove(card);
      targets.removeIf(x -> x.type == AbstractCard.CardType.CURSE);

      AbstractCard randomCard = targets.get(AbstractDungeon.eventRng.random(targets.size()));

      AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card.makeStatEquivalentCopy(),
          (float) Settings.WIDTH * 0.33f, (float)Settings.HEIGHT / 2.0F));
      AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(randomCard.makeStatEquivalentCopy(),
          (float) Settings.WIDTH * 0.66f, (float)Settings.HEIGHT / 2.0F));

      AbstractDungeon.gridSelectScreen.selectedCards.clear();
      return true;
    }
    return false;
  }

  @Override
  public boolean hasUpdateLogic() {
    return true;
  }

  @Override
  public Supplier<Boolean> isEventOptionEnabled() {
    return () -> AbstractDungeon.player.masterDeck.size() > 0;
  }
}
