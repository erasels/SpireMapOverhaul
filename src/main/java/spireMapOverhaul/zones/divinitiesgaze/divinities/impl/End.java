package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import spireMapOverhaul.zones.divinitiesgaze.cards.Inevitability;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class End extends BaseDivineBeing {

  public End() {
    super(Inevitability.ID, VoidCard.ID);
  }

  private int counter = 0;

  @Override
  public Consumer<Integer> doEventButtonAction() {
    // [Forget] Choose 1 of 3 random cards to remove from your deck. Do this twice.
    // done in update because we need to spawn two screens
    return (x) -> {};
  }

  @Override
  public Supplier<Boolean> isEventOptionEnabled() {
    return () -> AbstractDungeon.player.masterDeck.getPurgeableCards().size() > 2;
  }

  public boolean doUpdate() {
    if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
      AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0), (Settings.WIDTH * 0.75f), (float)(Settings.HEIGHT / 2)));// 44 46
      AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));// 49
      AbstractDungeon.gridSelectScreen.selectedCards.clear();
      counter++;
    }
    if(counter < 2) {
      if(!AbstractDungeon.isScreenUp) {
        spawnChoice();
      }
      return false;
    }
    counter = 0;
    return true;
  }

  private void spawnChoice() {
    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    Set<UUID> cardUUIDs = new HashSet<>();
    CardGroup purgeableCards = AbstractDungeon.player.masterDeck.getPurgeableCards();
    if (purgeableCards.group.size() <= 3){
      group.group.addAll(purgeableCards.group);
    }
    else {
      while (group.size() < 3) {
        AbstractCard card = purgeableCards.getRandomCard(AbstractDungeon.eventRng);
        if (!cardUUIDs.contains(card.uuid)) {
          cardUUIDs.add(card.uuid);
          group.addToTop(card);
        }
      }
    }

    AbstractDungeon.gridSelectScreen.open(group, 1, getDivinityStrings().getMiscText().get(SELECT_TEXT), false);
  }

  @Override
  public boolean hasUpdateLogic() {
    return true;
  }
}
