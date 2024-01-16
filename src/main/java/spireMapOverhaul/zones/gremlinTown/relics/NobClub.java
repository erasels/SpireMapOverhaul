package spireMapOverhaul.zones.gremlinTown.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToEnemy;

public class NobClub extends AbstractSMORelic {
    public static final String ID = makeID(NobClub.class.getSimpleName());
    private static final int ATTACK_THRESHOLD = 5;

    public NobClub() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replace("{0}", Integer.toString(ATTACK_THRESHOLD));
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            ++counter;
            if (counter % ATTACK_THRESHOLD == 0) {
                counter = 0;
                flash();
                addToBot(new RelicAboveCreatureAction(adp(), this));
                AbstractMonster m = Wiz.getRandomEnemy();
                applyToEnemy(m, new VulnerablePower(m, 1, false));
            }
        }
    }
}
