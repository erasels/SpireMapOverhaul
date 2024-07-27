package spireMapOverhaul.zones.frostlands.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.frostlands.FrostlandsZone;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class SpruceCharm extends AbstractSMORelic{
    public static final String ID = makeID(SpruceCharm.class.getSimpleName());
    public static final int amount = 1;
    public SpruceCharm() {
        super(ID, FrostlandsZone.ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        Wiz.atb(new ApplyPowerAction(Wiz.adp(), Wiz.adp(), new DoubleTapPower(Wiz.adp(), amount)));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SpruceCharm();
    }

}
