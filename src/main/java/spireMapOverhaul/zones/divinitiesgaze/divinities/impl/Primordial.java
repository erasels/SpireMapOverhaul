package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.zones.divinitiesgaze.cards.Mitosis;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Primordial extends BaseDivineBeing {

  public Primordial() {
    super(Mitosis.ID, Slimed.ID);
  }

  @Override
  public Consumer<Integer> doEventButtonAction() {
    return (i) -> AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, getDivinityStrings().getMiscText().get(SELECT_TEXT), false);
  }

  @Override
  public boolean doUpdate() {
    if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
      AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeStatEquivalentCopy(), (float) Settings.WIDTH * 0.33f, (float)Settings.HEIGHT / 2.0F));
      AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeStatEquivalentCopy(), (float) Settings.WIDTH * 0.66f, (float)Settings.HEIGHT / 2.0F));
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
