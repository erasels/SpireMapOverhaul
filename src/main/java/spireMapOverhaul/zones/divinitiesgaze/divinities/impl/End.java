package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import spireMapOverhaul.zones.divinitiesgaze.cards.Inevitability;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class End extends BaseDivineBeing {

  public static final String ID = "End";

  public End() {
    super(ID, Inevitability.ID, VoidCard.ID);
  }

  @Override
  public Consumer<Integer> doEventButtonAction() {
    return (i) -> AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), 1,
        getDivinityStrings().getMiscText().get(SELECT_TEXT), false);
  }

  @Override
  public Supplier<Boolean> isEventOptionEnabled() {
    return () -> AbstractDungeon.player.masterDeck.getPurgeableCards().size() > 0;
  }

  public boolean doUpdate() {
    if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
      AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0), (Settings.WIDTH * 0.75f), (float)(Settings.HEIGHT / 2)));// 44 46
      AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));// 49
      AbstractDungeon.gridSelectScreen.selectedCards.clear();
      return true;
    }
    return false;
  }

  @Override
  public boolean hasUpdateLogic() {
    return true;
  }
}
