package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.zones.divinitiesgaze.cards.Clairvoyance;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class Eclectic extends BaseDivineBeing {

  public static final String ID = "Eclectic";

  public Eclectic() {
    super(ID, Clairvoyance.ID, Dazed.ID);
  }

  @Override
  public Consumer<Integer> doEventButtonAction() {
    // [Learn] Choose 1 of 5 upgraded cards to add to your deck.
    return x -> {
      CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
      Set<UUID> cardUUIDs = new HashSet<>();
      while (group.size() < 5) {
        AbstractCard card = AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();
        if(!cardUUIDs.contains(card.uuid)) {
          cardUUIDs.add(card.uuid);
          card.upgrade();
          group.addToTop(card);
        }
      }

      AbstractDungeon.gridSelectScreen.open(group, 1, getDivinityStrings().getMiscText().get(SELECT_TEXT), false);
    };
  }

  @Override
  public boolean doUpdate() {
    if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
      AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeStatEquivalentCopy(),
          (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
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
