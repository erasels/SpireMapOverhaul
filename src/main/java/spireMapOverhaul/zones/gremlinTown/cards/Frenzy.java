package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.actions.WaitMoreAction;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

@NoPools
public class Frenzy extends AbstractSMOCard {
    public final static String ID = makeID(Frenzy.class.getSimpleName());
    private final static int COST = 2;
    private final static int DAMAGE = 7;
    private final static int UPG_DAMAGE = 2;
    private final static int MAGIC = 2;
    private final static int UPG_MAGIC = 1;

    public Frenzy() {
        super(ID, GremlinTown.ID, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        atb(new WaitMoreAction(0.13f));
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        applyToSelf(new StrengthPower(adp(), magicNumber));
    }

    public void upp() {
        upgradeDamage(UPG_DAMAGE);
        upMagic(UPG_MAGIC);
    }
}
