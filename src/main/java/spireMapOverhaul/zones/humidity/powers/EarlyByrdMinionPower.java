package spireMapOverhaul.zones.humidity.powers;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class EarlyByrdMinionPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("EarlyByrdMinionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = HumidityZone.ID;

    public EarlyByrdMinionPower(AbstractCreature owner, int amount) {
        super(POWER_ID,NAME,ZONE_ID, PowerType.BUFF, false, owner, amount);
    }
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
