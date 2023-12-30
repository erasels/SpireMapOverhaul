
package spireMapOverhaul.zones.gremlinTown.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;

import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

public class OmegaBlastPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("OmegaBlast");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static int blastIdOffset = 0;
    private final int damage;

    public OmegaBlastPower(AbstractCreature owner, int turns, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF,false, owner, amount);
        this.ID = "TheBomb" + blastIdOffset;
        ++blastIdOffset;
        this.amount = turns;
        damage = amount;
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            atb(new ReducePowerAction(owner, owner, this, 1));
            if (amount == 1) {
                atb(new DamageRandomEnemyAction(new DamageInfo(adp(), damage, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.FIRE));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0].replace("{0}", String.valueOf(amount));
        description = description.replace("{1}", String.valueOf(damage));
    }
}

