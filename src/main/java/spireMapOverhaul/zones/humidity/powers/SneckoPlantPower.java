package spireMapOverhaul.zones.humidity.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.humidity.HumidityZone;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.monsterRng;

public class SneckoPlantPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("SneckoPlantPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = HumidityZone.ID;

    public int nextTurnRoll = 0;
    public int thisTurnRoll = 0;

    public SneckoPlantPower(AbstractCreature owner) {
        super(POWER_ID, NAME, ZONE_ID, AbstractPower.PowerType.DEBUFF, false, owner, 0);
        nextTurnRoll = monsterRng.random(0, 3);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_CONFUSION", 0.05F);
    }

    @Override
    public void atStartOfTurn() {
        thisTurnRoll = nextTurnRoll;
        nextTurnRoll = monsterRng.random(0, 3);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
