package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.util.Wiz.applyToSelf;
import static spireMapOverhaul.util.Wiz.atb;

public class DelayedVigorPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("DelayedVigor");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DelayedVigorPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, GremlinTown.ID, PowerType.BUFF,false, owner, amount);
        ID = POWER_ID + GameActionManager.turn;
        isTwoAmount = true;
        amount2 = 2;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount2 == 2)
            description = DESCRIPTIONS[0].replace("{0}", String.valueOf(amount));
        else
            description = DESCRIPTIONS[1].replace("{0}", String.valueOf(amount));
    }

    @Override
    public void atStartOfTurn() {
        amount2--;
        if (amount2 == 0) {
            atb(new RemoveSpecificPowerAction(owner, owner, this));
            applyToSelf(new VigorPower(owner, amount));
        }
        updateDescription();
    }
}

