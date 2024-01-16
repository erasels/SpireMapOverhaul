package spireMapOverhaul.zones.gremlinTown.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

public class Duelity extends AbstractSMORelic {
    public static final String ID = makeID(Duelity.class.getSimpleName());

    public Duelity() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            flash();
            atb(new RelicAboveCreatureAction(adp(), this));
            atb(new ApplyPowerAction(adp(), adp(), new StrengthPower(adp(), 1), 1));
            atb(new ApplyPowerAction(adp(), adp(), new LoseStrengthPower(adp(), 1), 1));
        }
    }
}
