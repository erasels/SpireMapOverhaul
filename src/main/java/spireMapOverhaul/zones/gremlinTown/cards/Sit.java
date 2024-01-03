package spireMapOverhaul.zones.gremlinTown.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.applyToEnemy;

public class Sit extends AbstractSMOCard {
    public final static String ID = makeID(Sit.class.getSimpleName());
    private final static int COST = 1;
    private final static int MAGIC = 3;
    private final static int UPG_MAGIC = 1;

    public Sit() {
        super(ID, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = MAGIC;
        exhaust = true;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new StrengthPower(m, -magicNumber));
    }

    public void upp() {
        upMagic(UPG_MAGIC);
    }
}
