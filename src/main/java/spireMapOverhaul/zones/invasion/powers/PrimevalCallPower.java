package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.monsters.PrimevalQueen;

public class PrimevalCallPower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("PrimevalCall");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PrimevalCallPower(AbstractCreature owner) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, 0);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        ((PrimevalQueen)this.owner).summonRoyalProtectors(1, false, true);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
    }
}
