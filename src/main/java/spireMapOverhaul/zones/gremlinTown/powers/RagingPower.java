package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.util.Wiz.atb;

public class RagingPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("Raging");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public RagingPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, GremlinTown.ID, PowerType.BUFF,false, owner, amount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0].replace("{0}", String.valueOf(amount));
    }

    @Override
    public void atEndOfRound() {
        atb(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount)));
    }
}

