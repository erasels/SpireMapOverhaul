package spireMapOverhaul.zones.gremlinTown.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.powers.OmegaBlastPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

public class OmegaBlast extends AbstractSMOCard {
    public final static String ID = makeID(OmegaBlast.class.getSimpleName());
    private final static int MAGIC = 60;
    private final static int UPG_MAGIC = 15;
    private final static int COST = 2;

    public OmegaBlast() {
        super(ID, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new OmegaBlastPower(adp(), 3, magicNumber));
    }

    public void upp() {
        upMagic(UPG_MAGIC);
    }
}
