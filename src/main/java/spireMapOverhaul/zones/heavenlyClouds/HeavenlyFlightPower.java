package spireMapOverhaul.zones.heavenlyClouds;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;

import static spireMapOverhaul.util.Wiz.atb;

public class HeavenlyFlightPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID(HeavenlyFlightPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final int DAMAGE_REDUCTION = 50;
    private static final float HEIGHT_ADJUSTMENT = 100.0f;
    private final int initialAmount;

    public HeavenlyFlightPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, amount);
        this.loadRegion("flight");
        this.priority = 50;
        this.initialAmount = amount;
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        owner.drawY += HEIGHT_ADJUSTMENT * Settings.scale;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.owner != this.owner && info.type == DamageInfo.DamageType.NORMAL && info.output > 0) {
            amount--;
            this.flashWithoutSound();
            if (amount <= 0) {
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            }
        }
        return damageAmount;
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return calculateDamageTakenAmount(damage);
        } else {
            return damage;
        }
    }

    @Override
    public void atEndOfRound() {
        this.amount = initialAmount;
    }

    @Override
    public void onRemove() {
        owner.drawY -= HEIGHT_ADJUSTMENT * Settings.scale;
        if (owner instanceof AbstractMonster) {
            StunMonsterPower power = new StunMonsterPower((AbstractMonster) owner, this.amount);
            power.type = NeutralPowertypePatch.NEUTRAL;
            atb(new ApplyPowerAction(owner, owner, power, 1));
        }
        atb(new ApplyPowerAction(owner, owner, new InvisibleFlightTracker(owner, initialAmount), initialAmount));
    }

    private float calculateDamageTakenAmount(float damage) {
        return damage * (1 - ((float)DAMAGE_REDUCTION / 100));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + DAMAGE_REDUCTION + DESCRIPTIONS[1] + initialAmount + DESCRIPTIONS[2];
    }
}
