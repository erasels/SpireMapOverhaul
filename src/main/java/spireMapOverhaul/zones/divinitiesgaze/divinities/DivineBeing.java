package spireMapOverhaul.zones.divinitiesgaze.divinities;

import com.megacrit.cardcrawl.rooms.AbstractRoom;

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

  default Consumer<AbstractRoom> getCombatRewards() { return room -> {};}

  default boolean doUpdate() {
    return true;
  }

  default void doEnterCombat() {}

  default boolean hasUpdateLogic() {
    return false;
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

  default String[] getKeywordsForCardChoice(){
    return new String[]{};
  }

  default String[] getKeywordsForCustomChoice(){
    return new String[]{};
  }
}
