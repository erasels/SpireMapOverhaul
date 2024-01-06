package spireMapOverhaul.zones.candyland.consumables;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import spireMapOverhaul.abstracts.AbstractSMOCard;

@NoPools
abstract public class AbstractConsumable extends AbstractSMOCard {

    public AbstractConsumable(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(cardID, "CandyLand", cost, type, rarity, target);
        FleetingField.fleeting.set(this, true);
    }
}