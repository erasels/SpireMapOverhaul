package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.powers.CriticalPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

@NoPools
public class CriticalHit extends AbstractSMOCard {
    public final static String ID = makeID(CriticalHit.class.getSimpleName());
    private final static int COST = 1;
    private final static int MAGIC = 2;
    private final static int UPG_MAGIC = 1;

    public CriticalHit() {
        super(ID, GremlinTown.ID, COST, CardType.SKILL, CardRarity.RARE, CardTarget.SELF, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = MAGIC;
        exhaust = true;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new CriticalPower(adp(), magicNumber));
    }

    public void upp() {
        upMagic(UPG_MAGIC);
    }
}
