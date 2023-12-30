package spireMapOverhaul.zones.gremlinTown.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.abstracts.AbstractSMOCard;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class Assassinate extends AbstractSMOCard {
    public final static String ID = makeID(Assassinate.class.getSimpleName());
    private final static int COST = 1;
    private final static int DAMAGE = 18;
    private final static int UPG_DAMAGE = 6;

    public Assassinate() {
        super(ID, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        baseDamage = DAMAGE;
        exhaust = true;
        isInnate = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    public void upp() {
        upgradeDamage(UPG_DAMAGE);
    }
}
