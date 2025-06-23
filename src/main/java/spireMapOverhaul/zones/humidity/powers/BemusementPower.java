package spireMapOverhaul.zones.humidity.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class BemusementPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("BemusementPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = HumidityZone.ID;


    public BemusementPower(AbstractCreature owner) {
        super(POWER_ID,NAME,ZONE_ID, AbstractPower.PowerType.DEBUFF, false, owner, 0);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_CONFUSION", 0.05F);
    }

    public void onCardDraw(AbstractCard card) {
        if (card.cost >= 0) {
            int newCostRoll = AbstractDungeon.cardRandomRng.random(2);
            //leaving some space here in case we want to do something more interesting than a simple 33% chance
            int newCost = newCostRoll;
            if (card.cost != newCost) {
                card.cost = newCost;
                card.costForTurn = card.cost;
                card.isCostModified = true;
            }

            card.freeToPlayOnce = false;
        }

    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }


}