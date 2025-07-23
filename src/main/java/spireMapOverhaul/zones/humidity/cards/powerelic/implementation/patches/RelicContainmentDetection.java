package spireMapOverhaul.zones.humidity.cards.powerelic.implementation.patches;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.zones.humidity.cards.powerelic.implementation.PowerelicCard;

public class RelicContainmentDetection {
    public static boolean isContained(AbstractRelic relic) {
        return PowerelicCard.PowerelicRelicContainmentFields.isContained.get(relic);
    }
}
