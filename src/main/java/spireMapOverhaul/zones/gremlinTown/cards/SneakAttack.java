package spireMapOverhaul.zones.gremlinTown.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class SneakAttack extends AbstractSMOCard {
    public final static String ID = makeID(SneakAttack.class.getSimpleName());
    private final static int DAMAGE = 12;
    private final static int UPG_DAMAGE = 3;
    private final static int COST = 1;

    public SneakAttack() {
        super(ID, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        baseDamage = DAMAGE;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    public void upp() {
        upgradeDamage(UPG_DAMAGE);
    }
}
