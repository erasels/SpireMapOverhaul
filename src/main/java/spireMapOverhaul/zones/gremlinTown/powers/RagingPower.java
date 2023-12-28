package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;

import static spireMapOverhaul.util.Wiz.atb;

public class RagingPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("Raging");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public RagingPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF,false, owner, amount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        atb(new RemoveSpecificPowerAction(owner, owner, this));
    }
}

