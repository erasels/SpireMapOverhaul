package spireMapOverhaul.zones.wildfire;

import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import java.util.ArrayList;
import java.util.Collections;

public class FormatHelper {
    private static final String EXHAUST_TEXT = " NL " + capitalize(GameDictionary.EXHAUST.NAMES[0]) + LocalizedStrings.PERIOD;
    private static final String INNATE_TEXT = capitalize(GameDictionary.INNATE.NAMES[0]) + LocalizedStrings.PERIOD + " NL ";
    private static final String ETHEREAL_TEXT = capitalize(GameDictionary.ETHEREAL.NAMES[0]) + LocalizedStrings.PERIOD + " NL ";
    private static final String RETAIN_TEXT = capitalize(GameDictionary.RETAIN.NAMES[0]) + LocalizedStrings.PERIOD + " NL ";
    private static final String UNPLAYABLE_TEXT = capitalize(GameDictionary.UNPLAYABLE.NAMES[0]) + LocalizedStrings.PERIOD + " NL ";
    private static final StringBuilder newMsg = new StringBuilder();

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String capitalize(String str, String match) {
        return str.replace(match, capitalize(match));
    }

    public static String prefixWords(String input, String prefix) {
        newMsg.setLength(0);
        for (String word : input.split(" ")) {
            newMsg.append(prefix).append(word).append(' ');
        }

        return newMsg.toString().trim();
    }

    public static String prefixWords(String input, String prefix, String suffix) {
        newMsg.setLength(0);
        for (String word : input.split(" ")) {
            newMsg.append(prefix).append(word).append(suffix).append(' ');
        }

        return newMsg.toString().trim();
    }

    public static String insertBeforeText(String rawDescription, String text) {
        StringBuilder removed = new StringBuilder();
        ArrayList<String> matches = makeMatchers(INNATE_TEXT, ETHEREAL_TEXT, RETAIN_TEXT, UNPLAYABLE_TEXT);
        while (matches.stream().anyMatch(rawDescription::startsWith)) {
            for (String match : matches) {
                if (rawDescription.startsWith(match)) {
                    rawDescription = rawDescription.substring(match.length());
                    removed.append(match);
                }
            }
        }
        return removed + text + rawDescription;
    }

    public static String insertAfterText(String rawDescription, String text) {
        StringBuilder removed = new StringBuilder();
        for (String match : makeMatchers(EXHAUST_TEXT)) {
            if (rawDescription.endsWith(match)) {
                rawDescription = rawDescription.substring(0, rawDescription.length()-match.length());
                removed.append(match);
            }
        }
        return rawDescription + text + removed;
    }

    //Work with Minty Better Upgrade Text
    private static ArrayList<String> makeMatchers(String... inputs) {
        ArrayList<String> ret = new ArrayList<>();
        Collections.addAll(ret, inputs);
        for (String s : inputs) {
            ret.add((" [diffRmvS] "+s+" [diffRmvE] ").replace("  ", " "));
            ret.add((" [diffAddS] "+s+" [diffAddE] ").replace("  ", " "));
        }
        return ret;
    }
}
