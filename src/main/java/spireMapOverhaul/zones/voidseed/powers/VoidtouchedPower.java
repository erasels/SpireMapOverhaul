package spireMapOverhaul.zones.voidseed.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.BufferPower;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.voidseed.VoidSeedZone;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class VoidtouchedPower extends AbstractSMOPower {
    public static final String POWER_ID = makeID("VoidtouchedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public VoidtouchedPower(AbstractCreature target, int amount) {
        super(POWER_ID, NAME, VoidSeedZone.ID, PowerType.BUFF, false, target, amount);
        canGoNegative = false;
    }

    @Override
    public void atStartOfTurn() {
        if (!owner.hasPower(BufferPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(owner, owner, new BufferPower(owner, amount), amount));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
