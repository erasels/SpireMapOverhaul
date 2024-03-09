package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;

// This class only exists to add a Math.abs around the number in the description for negative draw amounts
// This also moves the logic for changing the hand size from the constructor to onInitialApplication/stackPower,
// since even though changing hand size in the constructor works fine for DrawPower, we've found that it can
// cause issues when done by a modded power like this
public class FixedTextDrawPower extends AbstractPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("FixedTextDraw");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FixedTextDrawPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("draw");
        if (amount < 0) {
            this.type = PowerType.DEBUFF;
            this.loadRegion("draw2");
        } else {
            this.type = PowerType.BUFF;
            this.loadRegion("draw");
        }

        this.isTurnBased = false;
        this.canGoNegative = true;
    }

    @Override
    public void onInitialApplication() {
        AbstractDungeon.player.gameHandSize += amount;
    }

    @Override
    public void onRemove() {
        AbstractDungeon.player.gameHandSize -= this.amount;
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        AbstractDungeon.player.gameHandSize += stackAmount;
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        AbstractDungeon.player.gameHandSize -= reduceAmount;
        if (this.amount == 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    public void updateDescription() {
        if (this.amount > 0) {
            if (this.amount == 1) {
                this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
            } else {
                this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[3];
            }

            this.type = PowerType.BUFF;
        } else {
            if (this.amount == -1) {
                this.description = DESCRIPTIONS[0] + Math.abs(this.amount) + DESCRIPTIONS[2];
            } else {
                this.description = DESCRIPTIONS[0] + Math.abs(this.amount) + DESCRIPTIONS[4];
            }

            this.type = PowerType.DEBUFF;
        }
    }
}

