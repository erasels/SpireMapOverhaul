package spireMapOverhaul.zones.thefog.relics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.thefog.TheFogZone;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;

public class StoneFragment extends AbstractSMORelic {
    public static final String ID = makeID("StoneFragment");

    public StoneFragment() {
        super(ID, TheFogZone.ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        counter = 13;
    }

    @Override
    public void atTurnStart() {
        if (counter < 1) return;
        flash();
        atb(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        atb(new GainEnergyAction(1));
        counter--;
        if (counter < 1) {
            counter = -1;
            usedUp();
        }
    }

    @Override
    public void update() {
        super.update();
        description = (counter < 1) ? MSG[2] : DESCRIPTIONS[1]; // MSG[2] is the "used up" string
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1];
    }
}
