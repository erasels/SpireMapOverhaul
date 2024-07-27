package spireMapOverhaul.zones.candyland.consumables;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import spireMapOverhaul.abstracts.AbstractSMOCard;

abstract public class AbstractConsumable extends AbstractSMOCard {

    public AbstractConsumable(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(cardID, "CandyLand", cost, type, rarity, target);
        FleetingField.fleeting.set(this, true);
    }
}