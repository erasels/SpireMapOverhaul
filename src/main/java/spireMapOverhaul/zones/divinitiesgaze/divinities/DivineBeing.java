package spireMapOverhaul.zones.divinitiesgaze.divinities;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface DivineBeing {
  DivinityStrings getDivinityStrings();
  Consumer<Integer> doEventButtonAction();
  default String getCombatPhaseKey() {
    return "";
  }
  default Supplier<Boolean> isEventOptionEnabled() {
    return () -> true;
  }
  default boolean doUpdate() {
    return true;
  }

  default String getEventButtonText() {
    return getDivinityStrings().getEventButtonText();
  }

  default String getEventAcceptText() {
    return getDivinityStrings().getEventAcceptText();
  }

  default String getEventRejectText() {
    return getDivinityStrings().getEventRejectText();
  }

  default String getEventButtonUnavailableText() {
    return getDivinityStrings().getEventButtonUnavailableText();
  }
}
