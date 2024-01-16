package spireMapOverhaul.zones.gremlinTown.relics;

import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;

public class Lockpicks extends AbstractSMORelic {
    public static final String ID = makeID(Lockpicks.class.getSimpleName());
    private static final int GOLD_AMOUNT = 30;

    public Lockpicks() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replace("{0}", Integer.toString(GOLD_AMOUNT));
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof RestRoom) {
            flash();
            adp().gainGold(GOLD_AMOUNT);
        }
    }
}
