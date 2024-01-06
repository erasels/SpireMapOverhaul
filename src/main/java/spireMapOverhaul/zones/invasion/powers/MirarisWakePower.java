package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

public class MirarisWakePower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("MirarisWake");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MirarisWakePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < this.amount; i++) {
            sb.append("[E] ");
        }
        String energy = sb.toString();

        this.description = DESCRIPTIONS[0].replace("{0}", energy);
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new GainEnergyAction(this.amount));
        this.flash();
    }
}

