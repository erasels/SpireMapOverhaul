package spireMapOverhaul.zones.beastslair.powers;


import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.beastslair.BeastsLairZone;

public class FuryPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("FuryPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = BeastsLairZone.ID;

    public FuryPower(AbstractCreature owner, int amount) {
        super(POWER_ID,NAME,ZONE_ID, NeutralPowertypePatch.NEUTRAL, false, owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            this.flash();
            this.addToBot(new ApplyPowerAction(owner, owner, new DamageUpPower(owner, this.amount)));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}

