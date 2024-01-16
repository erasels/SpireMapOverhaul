
package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

public class OmegaCursePower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("OmegaCurse");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public OmegaCursePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, GremlinTown.ID, PowerType.DEBUFF,false, owner, amount);
        priority = 4;
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL)
            return damage + amount;
        return damage;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0].replace("{0}", String.valueOf(amount));
    }
}

