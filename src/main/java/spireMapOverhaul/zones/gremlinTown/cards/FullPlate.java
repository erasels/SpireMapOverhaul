package spireMapOverhaul.zones.gremlinTown.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import spireMapOverhaul.abstracts.AbstractSMOCard;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

public class FullPlate extends AbstractSMOCard {
    public final static String ID = makeID(FullPlate.class.getSimpleName());
    private final static int COST = 2;
    private final static int MAGIC = 6;
    private final static int UPG_MAGIC = 2;

    public FullPlate() {
        super(ID, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF, CardColor.COLORLESS);
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new MetallicizePower(adp(), magicNumber));
    }

    public void upp() {
        upMagic(UPG_MAGIC);
    }
}
