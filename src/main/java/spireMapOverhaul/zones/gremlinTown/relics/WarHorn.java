package spireMapOverhaul.zones.gremlinTown.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;

public class WarHorn extends AbstractSMORelic {
    public static final String ID = makeID(WarHorn.class.getSimpleName());

    public WarHorn() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            flash();
            AbstractCard c = GremlinTown.getRandomGremlinInCombat();
            c.setCostForTurn(-99);
            atb(new MakeTempCardInHandAction(c));
        }
    }
}
