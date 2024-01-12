package spireMapOverhaul.zones.beastslair.powers;


import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.beastslair.BeastsLairZone;
import spireMapOverhaul.zones.manasurge.ManaSurgeZone;

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
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        addToBot(new ApplyPowerAction(this.owner, this.owner, new DamageUpPower(owner,amount), this.amount));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}

