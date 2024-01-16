package spireMapOverhaul.zones.gremlinTown.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.actions.ChooseOneGremlin;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

public class GremlinHood extends AbstractSMORelic {
    public static final String ID = makeID(GremlinHood.class.getSimpleName());

    public GremlinHood() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atBattleStartPreDraw() {
        atb(new RelicAboveCreatureAction(adp(), this));
        atb(new ChooseOneGremlin());
    }
}
