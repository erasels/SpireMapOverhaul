package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

public class SerpentsGazePower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("SerpentsGaze");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int ADDITIONAL_COST = 1;
    private int counter;

    public SerpentsGazePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, amount);
        this.counter = 0;
    }

    @Override
    public void updateDescription() {
        this.description = this.amount == 1
            ? DESCRIPTIONS[0].replace("{0}", ADDITIONAL_COST + "")
            : DESCRIPTIONS[1].replace("{0}", this.amount + "").replace("{1}", ADDITIONAL_COST + "");
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (this.counter > 0) {
            this.flash();
            if (card.cost >= 0) {
                card.setCostForTurn(Math.max(card.cost, card.costForTurn) + ADDITIONAL_COST);
            }
            this.counter--;
        }
    }

    @Override
    public void atStartOfTurn() {
        this.counter = this.amount;
    }
}

