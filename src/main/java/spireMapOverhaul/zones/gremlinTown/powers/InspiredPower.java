package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.util.Wiz.atb;

public class InspiredPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("Inspired");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public InspiredPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, GremlinTown.ID, PowerType.BUFF,false, owner, amount);
    }

    @Override
    public void updateDescription() {
        if (amount == 1)
            description = DESCRIPTIONS[0].replace("{0}", Integer.toString(amount));
        else
            description = DESCRIPTIONS[1].replace("{0}", Integer.toString(amount));
    }

    @Override
    public void atStartOfTurn() {
        flash();
        atb(new GainEnergyAction(amount));
    }

    @Override
    public void atStartOfTurnPostDraw() {
        atb(new DrawCardAction(amount));
    }
}

