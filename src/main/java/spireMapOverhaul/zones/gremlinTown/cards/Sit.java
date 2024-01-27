package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.applyToEnemy;

@NoPools
public class Sit extends AbstractSMOCard {
    public final static String ID = makeID(Sit.class.getSimpleName());
    private final static int COST = 1;
    private final static int MAGIC = 6;
    private final static int UPG_MAGIC = 2;

    public Sit() {
        super(ID, GremlinTown.ID, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = MAGIC;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new StrengthPower(m, -magicNumber));
        if (m != null && !(m.hasPower(ArtifactPower.POWER_ID)))
            applyToEnemy(m, new GainStrengthPower(m, magicNumber));
    }

    public void upp() {
        upMagic(UPG_MAGIC);
    }
}
