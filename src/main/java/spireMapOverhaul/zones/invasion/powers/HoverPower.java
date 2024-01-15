package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

public class HoverPower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("Hover");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HoverPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageType type) {
        return this.calculateDamageTakenAmount(damage, type);
    }

    private float calculateDamageTakenAmount(float damage, DamageType type) {
        return type != DamageType.HP_LOSS && type != DamageType.THORNS ? damage * (1 - (this.amount / 100.0F)) : damage;
    }
}

