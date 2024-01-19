package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

@NoPools
public class Assassinate extends AbstractSMOCard {
    public final static String ID = makeID(Assassinate.class.getSimpleName());
    private final static int COST = 1;
    private final static int DAMAGE = 18;
    private final static int UPG_DAMAGE = 6;

    public Assassinate() {
        super(ID, GremlinTown.ID, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        baseDamage = DAMAGE;
        exhaust = true;
        isInnate = true;
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
