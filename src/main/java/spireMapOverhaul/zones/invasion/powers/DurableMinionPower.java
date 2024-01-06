package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

public class DurableMinionPower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("DurableMinion");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DurableMinionPower(AbstractCreature owner) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, 0);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}