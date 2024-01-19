package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.powers.DelayedVigorPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

@NoPools
public class Charge extends AbstractSMOCard {
    public final static String ID = makeID(Charge.class.getSimpleName());
    private final static int MAGIC = 14;
    private final static int UPGRADE_MAGIC = 4;
    private final static int COST = 1;

    public Charge() {
        super(ID, GremlinTown.ID, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = MAGIC;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DelayedVigorPower(adp(), magicNumber));
    }

    public void upp() {
        upMagic(UPGRADE_MAGIC);
    }
}
