package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.actions.EasyXCostAction;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;

@NoPools
public class Flurry extends AbstractSMOCard {
    public final static String ID = makeID(Flurry.class.getSimpleName());
    private final static int COST = -1;
    private final static int DAMAGE = 8;
    private final static int UPG_DAMAGE =3;

    public Flurry() {
        super(ID, GremlinTown.ID, COST, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY, CardColor.COLORLESS);
        baseDamage = DAMAGE;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new EasyXCostAction(this, (effect, params) -> {
            effect++;
            for (int i = 0; i < effect; i++)
                atb(new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.SLASH_HEAVY));
            return true;
        }));
    }

    public void upp() {
        upgradeDamage(UPG_DAMAGE);
    }
}
