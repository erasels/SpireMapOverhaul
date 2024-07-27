package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

public class GremsFirePower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("GremsFire");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public GremsFirePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, GremlinTown.ID, PowerType.BUFF,false, owner, amount);
        canGoNegative = false;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0].replace("{0}", Integer.toString(amount));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        atb(new DamageAllEnemiesAction(adp(), DamageInfo.createDamageMatrix(amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
    }
}

