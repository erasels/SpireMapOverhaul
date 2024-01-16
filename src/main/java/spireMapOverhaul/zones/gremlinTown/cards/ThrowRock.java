package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

@NoPools
public class ThrowRock extends AbstractSMOCard {
    public final static String ID = makeID(ThrowRock.class.getSimpleName());
    private final static int DAMAGE = 6;
    private final static int UPG_DAMAGE = 2;
    private final static int MAGIC = 2;
    private final static int UPG_MAGIC = 1;
    private final static int COST = 1;

    public ThrowRock() {
        super(ID, GremlinTown.ID, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        applyToSelf(new StrengthPower(adp(), magicNumber));
        applyToSelf(new LoseStrengthPower(adp(), magicNumber));
    }

    public void upp() {
        upgradeDamage(UPG_DAMAGE);
        upMagic(UPG_MAGIC);
    }
}
