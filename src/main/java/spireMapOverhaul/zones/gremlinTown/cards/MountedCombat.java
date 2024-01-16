package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.applyToEnemy;

@NoPools
public class MountedCombat extends AbstractSMOCard {
    public final static String ID = makeID(MountedCombat.class.getSimpleName());
    private final static int COST = 2;

    public MountedCombat() {
        super(ID, GremlinTown.ID, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        exhaust = true;
        isEthereal = true;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new SlowPower(m, 0));
    }

    public void upp() {
        isEthereal = false;
    }
}
