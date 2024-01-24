package spireMapOverhaul.zones.gremlinTown.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

public class LousePlushie extends AbstractSMORelic {
    public static final String ID = makeID(LousePlushie.class.getSimpleName());
    private static final int BLOCK_AMOUNT = 15;
    private boolean usedThisCombat = false;

    public LousePlushie() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replace("{0}", Integer.toString(BLOCK_AMOUNT));
    }

    @Override
    public void atPreBattle() {
        usedThisCombat = false;
        pulse = true;
        beginPulse();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !usedThisCombat
                && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            pulse = false;
            applyToSelf(new NextTurnBlockPower(adp(), BLOCK_AMOUNT));
            att(new RelicAboveCreatureAction(adp(), this));
            usedThisCombat = true;
            grayscale = true;
        }
        return damageAmount;
    }

    public void justEnteredRoom(AbstractRoom room) {
        grayscale = false;
    }

    public void onVictory() {
        pulse = false;
    }
}
