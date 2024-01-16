package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToEnemy;

public class NoxiousBrewPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("NoxiousBrew");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public NoxiousBrewPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, GremlinTown.ID, PowerType.BUFF,false, owner, amount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0].replace("{0}", Integer.toString(amount));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractMonster target = Wiz.getRandomEnemy();
        applyToEnemy(target, new PoisonPower(target, adp(), amount));
    }
}

