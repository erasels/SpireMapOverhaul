package spireMapOverhaul.zones.gremlinTown.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

public class HeartImpairmentPower extends AbstractSMOPower implements BetterOnApplyPowerPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("HeartImpairment");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HeartImpairmentPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, GremlinTown.ID, PowerType.BUFF,false, owner, -1);
        canGoNegative = false;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public boolean betterOnApplyPower(AbstractPower pow, AbstractCreature target, AbstractCreature source) {
        return !pow.ID.equals(BeatOfDeathPower.POWER_ID) || !target.name.equals(CorruptHeart.NAME);
    }
}

