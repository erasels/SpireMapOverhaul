package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.powers.InspiredPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

@NoPools
public class Inspire extends AbstractSMOCard {
    public final static String ID = makeID(Inspire.class.getSimpleName());
    private final static int COST = 2;

    public Inspire() {
        super(ID, GremlinTown.ID, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF, CardColor.COLORLESS);
        isEthereal = true;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new InspiredPower(adp(), 1));
    }

    @Override
    public void upp() {
        isEthereal = false;
    }
}
