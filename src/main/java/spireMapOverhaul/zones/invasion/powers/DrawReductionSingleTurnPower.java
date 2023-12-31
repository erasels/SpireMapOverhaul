package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class DrawReductionSingleTurnPower extends AbstractPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("DrawReductionSingleTurn");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DrawReductionSingleTurnPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("lessdraw");
        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;
    }

    @Override
    public void onInitialApplication() {
        AbstractDungeon.player.gameHandSize -= this.amount;
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void onRemove() {
        AbstractDungeon.player.gameHandSize += this.amount;
    }

    @Override
    public void updateDescription() {
        this.description = (this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1]).replace("{0}", this.amount + "");
    }
}
