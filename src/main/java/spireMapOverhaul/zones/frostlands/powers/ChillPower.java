package spireMapOverhaul.zones.frostlands.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.frostlands.FrostlandsZone;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class ChillPower  extends AbstractSMOPower {
    public static String POWER_ID = makeID(ChillPower.class.getSimpleName());
    public static PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static String[] DESCRIPTIONS = strings.DESCRIPTIONS;

    public ChillPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, FrostlandsZone.ID, AbstractPower.PowerType.DEBUFF, false, owner, amount);
        isTurnBased = true;
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? Math.max(0, damage - this.amount) : damage;
    }

    @Override
    public void atEndOfRound() {
        Wiz.atb(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
