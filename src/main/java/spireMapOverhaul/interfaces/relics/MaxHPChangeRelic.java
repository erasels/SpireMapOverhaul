package spireMapOverhaul.interfaces.relics;

public interface MaxHPChangeRelic {
    default int onMaxHPChange(int amount) {
        return amount;
    }
}
