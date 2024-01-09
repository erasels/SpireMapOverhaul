package spireMapOverhaul.zones.voidseed.powers;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.voidseed.VoidSeedZone;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class VoidFogPower extends AbstractSMOPower {
    public static final String POWER_ID = makeID("VoidFogPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public VoidFogPower(AbstractCreature target, int amount) {
        super(POWER_ID, NAME, VoidSeedZone.ID, NeutralPowertypePatch.NEUTRAL, false, target, amount);
        canGoNegative = false;
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new LoseHPAction(owner, owner, amount));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
