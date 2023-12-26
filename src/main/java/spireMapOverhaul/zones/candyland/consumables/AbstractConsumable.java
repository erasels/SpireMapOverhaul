package spireMapOverhaul.zones.candyland.consumables;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import spireMapOverhaul.abstracts.AbstractSMOCard;


abstract public class AbstractConsumable extends AbstractSMOCard {
    private final CardRarity realRarity;

    public AbstractConsumable(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(cardID, cost, type, CardRarity.SPECIAL, target);
        realRarity = rarity;
        FleetingField.fleeting.set(this, true);
    }

    public void setRarity(){
        rarity = realRarity;
    }
}