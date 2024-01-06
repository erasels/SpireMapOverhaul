package spireMapOverhaul.zones.invasion.cards;

import spireMapOverhaul.abstracts.AbstractSMOCard;

public abstract class AbstractInvasionZoneCard extends AbstractSMOCard {
    public AbstractInvasionZoneCard(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(cardID, "Invasion", cost, type, rarity, target);
    }

    public AbstractInvasionZoneCard(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target, CardColor color) {
        super(cardID, "Invasion", cost, type, rarity, target, color);
    }
}
