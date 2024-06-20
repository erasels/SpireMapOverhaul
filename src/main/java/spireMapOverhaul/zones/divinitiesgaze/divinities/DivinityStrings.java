package spireMapOverhaul.zones.divinitiesgaze.divinities;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DivinityStrings {
  private String NAME;
  private String TITLE;
  private String EVENT_TEXT;
  private String EVENT_BUTTON_TEXT;
  private String EVENT_BUTTON_UNAVAILABLE_TEXT = "";
  private String EVENT_ACCEPT_TEXT;
  private String EVENT_REJECT_TEXT;
  @TextDictDisallowed private String BOON_CARD_ID;
  @TextDictDisallowed private String STATUS_CARD_ID;
  @TextDictDisallowed private String IMAGE_PATH;
  @TextDictDisallowed private String[] PRE_COMBAT_QUOTES;
  @TextDictDisallowed private String[] EXTRA_TEXT;
  @TextDictDisallowed private Map<String, String> MISC_TEXT = new HashMap<>();

  public DivinityStrings(String uiStringId, String boonCardId, String baneCardId, String img) {
    final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(uiStringId);
    this.PRE_COMBAT_QUOTES = uiStrings.TEXT == null ? new String[]{} : uiStrings.TEXT;
    this.EXTRA_TEXT = uiStrings.EXTRA_TEXT;
    this.BOON_CARD_ID = boonCardId;
    this.STATUS_CARD_ID = baneCardId;
    this.IMAGE_PATH = img;

    final Map<String, String> TEXT_DICT = uiStrings.TEXT_DICT;
    final Set<String> FIELDS = Arrays.stream(this.getClass().getDeclaredFields())
        .map(Field::getName)
        .collect(Collectors.toSet());

    for(String key: TEXT_DICT.keySet()) {
      // a custom key, just put it into the misc map, but only if it came from the specific loc
      if(!FIELDS.contains(key)) {
        MISC_TEXT.put(key, TEXT_DICT.get(key));
        continue;
      }

      try {
        Field field = DivinityStrings.class.getDeclaredField(key);
        if(field.getAnnotation(TextDictDisallowed.class) != null) {
          // field is one of the special ones we set elsewhere, don't want to overwrite
          return;
        }

        field.setAccessible(true);

        // set value to text dict value if present, or default otherwise
        field.set(this, TEXT_DICT.get(key));
      } catch (NoSuchFieldException | IllegalAccessException ignored) {
        // this cannot actually happen (unless someone were to patch fields to contain different values I guess)
      }
    }
  }

  public String getName() {
    return NAME;
  }

  public String getTitle() {
    return TITLE;
  }

  public String getEventText() {
    return EVENT_TEXT;
  }

  public String getBoonCardId() {
    return BOON_CARD_ID;
  }

  public String getStatusCardId() {
    return STATUS_CARD_ID;
  }

  public String getImagePath() {
    return IMAGE_PATH;
  }

  public String[] getPreCombatQuotes() {
    return PRE_COMBAT_QUOTES;
  }

  public String[] getExtraText() {
    return EXTRA_TEXT;
  }

  public Map<String, String> getMiscText() {
    return MISC_TEXT;
  }

  public String getEventButtonText() {
    return EVENT_BUTTON_TEXT;
  }

  public String getEventAcceptText() {
    return EVENT_ACCEPT_TEXT;
  }

  public String getEventRejectText() {
    return EVENT_REJECT_TEXT;
  }

  public String getEventButtonUnavailableText() {
    return EVENT_BUTTON_UNAVAILABLE_TEXT;
  }

  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface TextDictDisallowed {}
}
