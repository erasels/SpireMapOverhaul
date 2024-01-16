package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class HydraHeadsPower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("HydraHeads");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int threshold;

    public HydraHeadsPower(AbstractCreature owner, int threshold) {
        super(POWER_ID, NAME, PowerType.BUFF, true, owner, 0);
        this.threshold = threshold;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.threshold + "");
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount >= this.threshold && this.owner.hasPower(StrengthPower.POWER_ID) && this.owner.getPower(StrengthPower.POWER_ID).amount > 0) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -1), -1));
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new HydraHeadRegrowthPower(this.owner, 1), 1));
        }
        return damageAmount;
    }
}

