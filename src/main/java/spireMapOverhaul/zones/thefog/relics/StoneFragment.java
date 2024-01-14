package spireMapOverhaul.zones.thefog.relics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.thefog.TheFogZone;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;

public class StoneFragment extends AbstractSMORelic {
    public static final String ID = makeID("StoneFragment");

    public StoneFragment() {
        super(ID, TheFogZone.ID, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atTurnStart() {
        pulse = true;
        beginPulse();
        counter = 0;
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        counter += 1;
        if (counter == 6) {
            flash();
            pulse = false;
            stopPulse();
            atb(new GainEnergyAction(1));
        }
    }

    @Override
    public void onVictory() {
        pulse = false;
        stopPulse();
        counter = -1;
    }
}
