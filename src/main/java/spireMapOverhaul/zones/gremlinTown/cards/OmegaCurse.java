package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.powers.OmegaCursePower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

@NoPools
public class OmegaCurse extends AbstractSMOCard {
    public final static String ID = makeID(OmegaCurse.class.getSimpleName());
    private final static int MAGIC = 99;
    private final static int COST = 2;
    private final static int UPGRADED_COST = 1;

    public OmegaCurse() {
        super(ID, COST, CardType.SKILL, CardRarity.RARE, CardTarget.SELF, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = MAGIC;
        exhaust = true;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new OmegaCursePower(adp(), 3, magicNumber));
    }

    public void upp() {
        upgradeBaseCost(UPGRADED_COST);
    }
}