package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.powers.TenderizerPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

@NoPools
public class MeatMallet extends AbstractSMOCard {
    public final static String ID = makeID(MeatMallet.class.getSimpleName());
    private final static int MAGIC = 1;
    private final static int COST = 1;
    private final static int UPGRADED_COST = 0;

    public MeatMallet() {
        super(ID, GremlinTown.ID, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = MAGIC;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new TenderizerPower(adp(), magicNumber));
    }

    public void upp() {
        upgradeBaseCost(UPGRADED_COST);
    }
}
