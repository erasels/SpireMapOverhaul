package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

@NoPools
public class AngryStrike extends AbstractSMOCard {
    public final static String ID = makeID(AngryStrike.class.getSimpleName());
    private final static int DAMAGE = 6;
    private final static int UPG_DAMAGE = 2;
    private final static int COST = 0;

    public AngryStrike() {
        super(ID, GremlinTown.ID, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = DAMAGE;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        atb(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                ArrayList<AbstractCard> cards = new ArrayList<>();
                for (AbstractCard c : adp().hand.group)
                    if (c.type != CardType.ATTACK)
                        cards.add(c);

                if (cards.isEmpty())
                    return;

                int x = AbstractDungeon.cardRandomRng.random(cards.size() - 1);
                att(new ExhaustSpecificCardAction(cards.get(x), adp().hand));
            }
        });
    }

    public void upp() {
        upgradeDamage(UPG_DAMAGE);
    }
}
