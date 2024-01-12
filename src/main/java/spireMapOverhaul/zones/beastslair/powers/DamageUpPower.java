package spireMapOverhaul.zones.beastslair.powers;


import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.beastslair.BeastsLairZone;
import spireMapOverhaul.zones.manasurge.ManaSurgeZone;

public class DamageUpPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("DamageUpPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = BeastsLairZone.ID;

    public DamageUpPower(AbstractCreature owner, int amount) {
        super(POWER_ID,NAME,ZONE_ID, NeutralPowertypePatch.NEUTRAL, false, owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return damage +(damage * (this.amount / 100f));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}

