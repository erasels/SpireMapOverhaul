
package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;

import static spireMapOverhaul.util.Wiz.*;

public class OmegaCursePower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("OmegaCurse");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static int blastIdOffset = 0;
    private final int damage;

    public OmegaCursePower(AbstractCreature owner, int turns, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF,false, owner, amount);
        ID = POWER_ID + blastIdOffset;
        ++blastIdOffset;
        this.amount = turns;
        damage = amount;
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            atb(new ReducePowerAction(owner, owner, this, 1));
            if (amount == 1) {
                forAllMonstersLiving(m -> {
                    applyToEnemy(m, new WeakPower(m, amount, false));
                    applyToEnemy(m, new VulnerablePower(m, amount, false));
                });
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0].replace("{0}", String.valueOf(amount));
        description = description.replace("{1}", String.valueOf(damage));
    }
}

