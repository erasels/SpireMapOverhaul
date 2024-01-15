package spireMapOverhaul.zones.mirror.powers;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.mirror.MirrorZone;

// This power only exists so it can flash.
// All logics are in MirrorZone and CaptureEnemyMovePatch.
public class MirrorZonePower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("MirrorZone");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MirrorZonePower(AbstractCreature owner) {
        super(POWER_ID, NAME, MirrorZone.ID, NeutralPowertypePatch.NEUTRAL, false, owner, -1);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
