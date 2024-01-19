package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.powers.RigPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

@NoPools
public class Rig extends AbstractSMOCard {
    public final static String ID = makeID(Rig.class.getSimpleName());
    private final static int COST = 1;
    private final static int UPGRADED_COST = 0;

    public Rig() {
        super(ID, GremlinTown.ID, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF, CardColor.COLORLESS);
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new RigPower(adp(), 1));
    }

    public void upp() {
        upgradeBaseCost(UPGRADED_COST);
    }
}
