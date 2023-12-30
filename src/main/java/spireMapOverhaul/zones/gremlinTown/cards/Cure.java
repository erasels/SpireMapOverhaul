package spireMapOverhaul.zones.gremlinTown.cards;

import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.abstracts.AbstractSMOCard;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

public class Cure extends AbstractSMOCard {
    public final static String ID = makeID(Cure.class.getSimpleName());
    private final static int COST = 1;

    public Cure() {
        super(ID, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF, CardColor.COLORLESS);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new RemoveDebuffsAction(adp()));
    }

    public void upp() {
        selfRetain = true;
    }
}
