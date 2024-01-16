package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.util.Wiz.atb;

public class SapPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("Sap");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SapPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, GremlinTown.ID, PowerType.BUFF,false, owner, amount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        atb(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void atStartOfTurn() {
        atb(new LoseEnergyAction(amount));
        flash();
    }

    @Override
    public void updateDescription() {
        if (amount == 1)
            description = DESCRIPTIONS[0];
        else
            description = DESCRIPTIONS[1];
    }
}
