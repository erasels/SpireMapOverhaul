package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.powers.HordePower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;

@NoPools
public class TheHorde extends AbstractSMOCard {
    public final static String ID = makeID(TheHorde.class.getSimpleName());
    private final static int COST = 1;

    public TheHorde() {
        super(ID, GremlinTown.ID, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF, CardColor.COLORLESS);
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new HordePower(adp(), 1));
    }

    public void upp() {
        isInnate = true;
    }
}
