package spireMapOverhaul.zones.hailstorm.relics;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.hailstorm.HailstormZone;
import spireMapOverhaul.zones.hailstorm.campfire.FlintOption;

import java.util.ArrayList;

@AutoAdd.Ignore
public class Flint extends AbstractSMORelic {
    public static final String ID = Flint.class.getSimpleName();


    public Flint() {
        super(ID, HailstormZone.ID, RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
    }

    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        if (!this.usedUp)
            options.add(new FlintOption());
    }
}