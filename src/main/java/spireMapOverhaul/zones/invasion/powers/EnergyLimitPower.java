package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

public class EnergyLimitPower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("EnergyLimit");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int ENERGY_LIMIT = 3;

    public EnergyLimitPower(AbstractCreature owner) {
        super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, 0);
        this.priority = 10;
    }

    public void atStartOfTurnPostDraw() {
        int energyToLose = AbstractDungeon.player.energy.energy - ENERGY_LIMIT;
        if (energyToLose > 0) {
            this.flash();
            this.addToBot(new LoseEnergyAction(energyToLose));
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}

