package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.powers.OmegaCursePower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

@NoPools
public class OmegaCurse extends AbstractSMOCard {
    public final static String ID = makeID(OmegaCurse.class.getSimpleName());
    private final static int MAGIC = 5;
    private final static int UPGRADE_MAGIC = 2;
    private final static int HP_LOSS = 10;
    private final static int COST = 1;

    public OmegaCurse() {
        super(ID, GremlinTown.ID, COST, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagic = secondMagic = HP_LOSS;
        exhaust = true;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new OmegaCursePower(m, magicNumber));
        atb(new LoseHPAction(adp(), adp(), secondMagic));
    }

    public void upp() {
        upMagic(UPGRADE_MAGIC);
    }
}
