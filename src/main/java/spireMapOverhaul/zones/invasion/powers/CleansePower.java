package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class CleansePower extends AbstractPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("Cleanse");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean temporary;

    public CleansePower(AbstractCreature owner, int amount, boolean temporary) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.temporary = temporary;
        updateDescription();
        this.loadRegion("panache");
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        for (AbstractPower p : owner.powers) {
            if (p.type == PowerType.DEBUFF) {
                if (p.amount > 0 && !p.ID.equals(GainStrengthPower.POWER_ID)) {
                    if (this.amount >= p.amount) {
                        this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, p));
                    }
                    else {
                        p.reducePower(this.amount);
                    }
                }
                else if (p.amount < 0) {
                    if (this.amount >= Math.abs(p.amount)) {
                        this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, p));
                    }
                    else {
                        p.stackPower(this.amount);
                    }
                }
            }
        }

        if (this.temporary) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "") + (this.temporary ? " " + DESCRIPTIONS[1] : "");
    }
}