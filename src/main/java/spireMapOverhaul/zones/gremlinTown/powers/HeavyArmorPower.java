package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;

public class HeavyArmorPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("HeavyArmor");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HeavyArmorPower(AbstractCreature owner) {
        super(POWER_ID, NAME, PowerType.BUFF,false, owner, -1);
        canGoNegative = false;
        priority = 98;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (owner.currentBlock > 0 && type == DamageInfo.DamageType.NORMAL)
            return damage * 2.0F;
        else
            return damage;
    }
}

