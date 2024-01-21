package spireMapOverhaul.zones.hailstorm.relics;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.Damaru;
import spireMapOverhaul.zones.brokenSpace.relics.BrokenRelic;

import static spireMapOverhaul.util.Wiz.adp;

@AutoAdd.Ignore
public class Flint extends BrokenRelic {
    public static final String ID = Flint.class.getSimpleName();


    public Flint() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Flint.ID);
    }

}