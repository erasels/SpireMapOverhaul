package spireMapOverhaul.zones.gremlinTown.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

public class GremlinStaff extends AbstractSMORelic {
    public static final String ID = makeID(GremlinStaff.class.getSimpleName());
    private static final int VIGOR_AMOUNT = 15;

    public GremlinStaff() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replace("{0}", Integer.toString(VIGOR_AMOUNT));
    }

    @Override
    public void atBattleStart() {
        counter = 0;
    }

    public void atTurnStart() {
        if (!grayscale) {
            ++counter;
        }

        if (counter == 3) {
            flash();
            atb(new RelicAboveCreatureAction(adp(), this));
            applyToSelf(new VigorPower(adp(), VIGOR_AMOUNT));
            counter = -1;
            grayscale = true;
        }

    }

    public void onVictory() {
        counter = -1;
        grayscale = false;
    }
}
