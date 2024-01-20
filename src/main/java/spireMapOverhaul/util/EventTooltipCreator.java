package spireMapOverhaul.util;

import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.Circlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class EventTooltipCreator {
  private static final Logger LOG = LogManager.getLogger(EventTooltipCreator.class);

  /**
   * Create a Circlet with custom tooltips. This can be passed into the AbstractRelic parameter of {@link basemod.abstracts.events.phases.TextPhase.OptionInfo TextPhase.OptionInfo}
   * to allow for rendering additional tooltips when the option is hovered in a {@link basemod.abstracts.events.PhasedEvent PhasedEvent}.
   *
   * @see basemod.abstracts.events.phases.TextPhase.OptionInfo
   * @see basemod.abstracts.events.PhasedEvent
   * @param keywords The list of keywords that should be applied to the relic.
   * @return A circlet instance with its keywords overridden to match those provided
   */
  public static Circlet createRelicForTootlips(String...keywords) {
    if(keywords == null || keywords.length == 0) {
      return null;
    }

    Circlet circlet = new Circlet();
    circlet.tips.clear();
    circlet.tips.addAll(Arrays.stream(keywords)
        .filter(Objects::nonNull)
        .map(key -> {
          Keyword keyword = SpireAnniversary6Mod.keywords.get(key);
          if(keyword == null) {
            LOG.info("Could not locate keyword with ID {} for event tooltip. Make sure keyword has ID key defined in your Keywordstrings.json.", key);
          }
          return keyword;
        })
        .filter(Objects::nonNull)
        .map(keyword -> new PowerTip(keyword.PROPER_NAME, keyword.DESCRIPTION))
        .collect(Collectors.toList()));
    return circlet.tips.size() == 0 ? null : circlet;
  }
}
