package spireMapOverhaul.zones.gremlinTown.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.att;

public class WarHorn extends AbstractSMORelic {
    public static final String ID = makeID(WarHorn.class.getSimpleName());
    private static final int STRENGTH_AMOUNT = 3;

    public WarHorn() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public void atBattleStart() {
        boolean boss = false;
        for (AbstractMonster m : Wiz.getEnemies()) {
            if (m.type == AbstractMonster.EnemyType.BOSS){
                boss = true;
                break;
            }
        }

        if (boss) {
            flash();
            Wiz.applyToSelfTop(new StrengthPower(adp(), STRENGTH_AMOUNT));
            att(new RelicAboveCreatureAction(adp(), this));
        }
    }
}
