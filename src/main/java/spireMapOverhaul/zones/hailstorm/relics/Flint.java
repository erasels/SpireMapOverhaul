package spireMapOverhaul.zones.hailstorm.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.frostlands.relics.SpruceCharm;
import spireMapOverhaul.zones.hailstorm.HailstormZone;
import spireMapOverhaul.zones.hailstorm.campfire.FlintOption;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.*;

public class Flint extends AbstractSMORelic {
    public static final String ID =  makeID("Flint");

    public Flint() {
        super(ID, HailstormZone.ID, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        if (!this.usedUp)
            options.add(new FlintOption());
    }

    @Override
    public void onTrigger() {
        this.usedUp();
        this.grayscale = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Flint();
    }
}