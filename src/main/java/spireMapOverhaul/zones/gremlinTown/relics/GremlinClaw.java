package spireMapOverhaul.zones.gremlinTown.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

public class GremlinClaw extends AbstractSMORelic {
    public static final String ID = makeID(GremlinClaw.class.getSimpleName());

    public GremlinClaw() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0 && !m.hasPower(MinionPower.POWER_ID)) {
            flash();
            atb(new RelicAboveCreatureAction(adp(), this));
            atb(new HealAction(adp(), adp(), 1));
        }
    }
}
