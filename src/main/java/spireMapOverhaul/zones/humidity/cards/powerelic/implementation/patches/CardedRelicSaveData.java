package spireMapOverhaul.zones.humidity.cards.powerelic.implementation.patches;

import java.io.Serializable;

public class CardedRelicSaveData implements Serializable {
    public String relicID;
    public int counter;
    public boolean active;

    public CardedRelicSaveData(String relicID, int counter, boolean active) {
        this.relicID = relicID;
        this.counter = counter;
        this.active = active;
    }
}
