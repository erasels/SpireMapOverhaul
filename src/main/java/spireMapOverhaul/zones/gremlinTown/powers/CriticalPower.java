
package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.util.Wiz.atb;

public class CriticalPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("Critical");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CriticalPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, GremlinTown.ID, PowerType.BUFF,false, owner, amount);
        priority = 7;
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackAmount > 1) {
            fontScale = 8.0F;
            amount += stackAmount - 1;
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0].replace("{0}", String.valueOf(amount));
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK)
            atb(new RemoveSpecificPowerAction(owner, owner, this));

    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * amount : damage;
    }
}

