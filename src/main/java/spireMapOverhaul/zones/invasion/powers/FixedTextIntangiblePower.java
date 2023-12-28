package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class FixedTextIntangiblePower extends IntangiblePower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("FixedTextIntangiblePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FixedTextIntangiblePower(AbstractCreature owner, int turns) {
        super(owner, turns);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
    }
}
