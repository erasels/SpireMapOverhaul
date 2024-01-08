package spireMapOverhaul.zones.heavenlyClouds;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;

import static spireMapOverhaul.util.Wiz.atb;

public class InvisibleFlightTracker extends AbstractSMOPower implements InvisiblePower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID(InvisibleFlightTracker.class.getSimpleName());
    private boolean justApplied = true;

    public InvisibleFlightTracker(AbstractCreature owner, int amount) {
        super(POWER_ID, "", PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atEndOfRound() {
        if (justApplied) {
            justApplied = false;
        } else {
            atb(new ApplyPowerAction(owner, owner, new HeavenlyFlightPower(owner, amount), amount));
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }


}
