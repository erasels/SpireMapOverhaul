package spireMapOverhaul.zones.humidity.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class AcidicPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("AcidicPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = HumidityZone.ID;

    public AcidicPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, ZONE_ID, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.rarity == AbstractCard.CardRarity.BASIC) {
            this.addToTop(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -amount), -amount));// 27
            if (!this.owner.hasPower(ArtifactPower.POWER_ID)) {// 29
                this.addToTop(new ApplyPowerAction(this.owner, this.owner, new GainStrengthPower(this.owner, amount), amount));// 30
            }
            this.flash();
        }
    }
}
