package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;

import java.util.function.Consumer;

public class Torch extends BaseDivineBeing {

  static {
    new Torch();
  }

  public Torch() {
    super(Strike_Red.ID, Burn.ID);
  }

  @Override
  public Consumer<Integer> doEventButtonAction() {
    // [Bask] Heal 10%. Upgrade a random card in your deck.
    return (x) -> {};
  }

  @Override
  public String getEventButtonText() {
    return String.format(super.getEventButtonText(), (int)Math.floor(AbstractDungeon.player.maxHealth * 0.10f));
  }
}
