package spireMapOverhaul.zones.hailstorm.relics;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Damaru;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.TokeOption;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.brokenSpace.relics.BrokenRelic;
import spireMapOverhaul.zones.hailstorm.HailstormZone;
import spireMapOverhaul.zones.hailstorm.campfire.FlintOption;

import java.util.ArrayList;

import static spireMapOverhaul.util.Wiz.adp;

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