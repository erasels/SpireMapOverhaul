package spireMapOverhaul.zones.humidity.cards.powerelic.implementation.patches;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.cards.powerelic.implementation.PowerelicCard;

import java.util.ArrayList;
import java.util.Objects;


public class PowerelicSavePatches2 {
    public static class HumidityPowerelicTemporaryDuplicateRelics implements CustomSavable<ArrayList<CardedRelicSaveData>> {
        public final static String SaveKey = "HumidityPowerelicTemporaryDuplicateRelics";

        @Override
        public ArrayList<CardedRelicSaveData> onSave() {
            ArrayList<CardedRelicSaveData> relics = new ArrayList<>();
            for (AbstractRelic relic : Wiz.adp().relics) {
                //Relic is a "temporary duplicate" if it is flagged as contained within a card, but that card is not in the player's deck
                if (PowerelicCard.PowerelicRelicContainmentFields.isContained.get(relic)
                        && !Wiz.deck().contains(PowerelicCard.PowerelicRelicContainmentFields.withinCard.get(relic))) {
                    relics.add(new CardedRelicSaveData(relic.relicId, relic.counter, true));
                }
            }
            return relics;
        }

        @Override
        public void onLoad(ArrayList<CardedRelicSaveData> relics) {
            if (relics == null) return;
            PowerelicCard.logger.info("Loading list of temporary duplicate relics...");
            if (relics.isEmpty()) PowerelicCard.logger.info("(it's empty)");
            for (CardedRelicSaveData cardedRelicSaveData : relics) {
                boolean matchFound = false;
                AbstractRelic relic = null;
                //find the first relic in the player's relic list that
                //  1) matches the saved data and
                //  2) is not already captured, then
                //      flag it as temporary
                for (AbstractRelic playerRelic : Wiz.adp().relics) {
                    if (Objects.equals(playerRelic.relicId, cardedRelicSaveData.relicID)) {
                        if (playerRelic.counter == cardedRelicSaveData.counter) {
                            if (!PowerelicCard.PowerelicRelicContainmentFields.isContained.get(playerRelic)) {
                                PowerelicCard.logger.info(cardedRelicSaveData.relicID + " with counter " + cardedRelicSaveData.counter + " will be restored to temporary duplicate status");
                                relic = playerRelic;
                                matchFound = true;
                                break;
                            }
                        }
                    }
                }
                if (!matchFound) {
                    PowerelicCard.logger.info("WARNING: " + cardedRelicSaveData.relicID + " reports that it is a temporary duplicate, but we couldn't find a matching relic in player's list with counter " + cardedRelicSaveData.counter);
                }
                if (relic != null) {
                    PowerelicCard.PowerelicRelicContainmentFields.isContained.set(relic, true);
                    if (PowerelicCard.PowerelicRelicContainmentFields.withinCard.get(relic) != null) {
                        PowerelicCard.logger.info("WARNING: " + cardedRelicSaveData.relicID + " is somehow flagged as being captured within card " + PowerelicCard.PowerelicRelicContainmentFields.withinCard.get(relic) + " (expected null instead)");
                    }
                    PowerelicCard.PowerelicRelicContainmentFields.withinCard.set(relic, null);
                }
            }
        }
    }
}
