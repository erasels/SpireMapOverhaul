package spireMapOverhaul.zoneInterfaces;

import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.util.ArrayList;

public interface CampfireModifyingZone {

    /**
     * This executes after the relic method and after the CampfireOptions are already generated.
     * Should only be used for effects that do not affect the options.
     */
    default void onEnterRestRoom() {}

    /**
     * Happens after all possible buttons were added to the buttons list and before the game checks whether there are any
     * usable buttons. This list can be modified to make buttons (un)usable, removing options or adding CampfireOptions.
     * @param buttons The arraylist of the current AbstractCampfireOption
     */
    default void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {}

    /**
     * Method for deciding whether another campfire option may be selected after the first.
     * @param optionsSelectedAmt int value that represents the amount of options that have already been selected for this campfire
     * @return returning true here will allow for another selection, will trigger after each selection.
     */
    default boolean allowAdditionalOption(int optionsSelectedAmt) {
        return false;
    }

    /**
     * Happens after all messages are added to the list and before one is randomly selected.
     * @param messages The current list of messages which can be modified to your liking.
     */
    default void postAddCampfireMessages(ArrayList<String> messages) {}

    /**
     * This hook allows you to execute code after an option was used.
     * You can use ReflectionHacks in postAddButtons to change descriptions of options affected.
     * NOTE: Most options execute their actual code in effects, so if you want something to happen after those, use the effectslist
     * @param option The option that was selected
     */
    default void postUseCampfireOption(AbstractCampfireOption option) {}
}
