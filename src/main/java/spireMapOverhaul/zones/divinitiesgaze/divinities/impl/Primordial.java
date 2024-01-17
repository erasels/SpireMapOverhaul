package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import com.megacrit.cardcrawl.cards.status.Slimed;
import spireMapOverhaul.zones.divinitiesgaze.cards.Propagation;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Primordial extends BaseDivineBeing {

  static {
    new Primordial();
  }

  public Primordial() {
    super(Propagation.ID, Slimed.ID);
  }

  @Override
  public Consumer<Integer> doEventButtonAction() {
    return (i) -> {};
  }

  @Override
  public Supplier<Boolean> isEventOptionEnabled() {
    return () -> true;
  }
}
