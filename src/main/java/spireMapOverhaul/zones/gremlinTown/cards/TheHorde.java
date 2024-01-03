package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.Enums.GREMLIN;
import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

@NoPools
public class TheHorde extends AbstractSMOCard {
    public final static String ID = makeID(TheHorde.class.getSimpleName());
    private final static int DAMAGE = 6;
    private final static int UPG_DAMAGE = 2;
    private final static int MAGIC = 2;
    private final static int COST = 2;

    public TheHorde() {
        super(ID, COST, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY, CardColor.COLORLESS);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        AbstractCard c = GremlinTown.getRandomCommonGremlinInCombat();
        c.setCostForTurn(-99);
        atb(new MakeTempCardInHandAction(c));
        AbstractCard c2 = GremlinTown.getRandomCommonGremlinInCombat();
        c2.setCostForTurn(-99);
        atb(new MakeTempCardInHandAction(c2));
    }

    @Override
    public void applyPowers() {
        int temp = baseDamage;
        int x = countCards();
        baseDamage = x * temp;
        super.applyPowers();
        isDamageModified = baseDamage == damage;
        baseDamage = temp;
    }

    private static int countCards() {
        int count = 0;

        for (AbstractCard c : adp().hand.group)
            if (c.hasTag(GREMLIN))
                count++;
        for (AbstractCard c : adp().discardPile.group)
            if (c.hasTag(GREMLIN))
                count++;
        for (AbstractCard c : adp().drawPile.group)
            if (c.hasTag(GREMLIN))
                count++;

        return count;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int temp = baseDamage;
        int x = countCards();
        baseDamage = x * temp;
        super.calculateCardDamage(mo);
        isDamageModified = baseDamage == damage;
        baseDamage = temp;
    }

    public void upp() {
        upgradeDamage(UPG_DAMAGE);
    }
}
