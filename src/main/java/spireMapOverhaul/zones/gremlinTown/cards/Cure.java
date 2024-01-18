package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

@NoPools
public class Cure extends AbstractSMOCard {
    public final static String ID = makeID(Cure.class.getSimpleName());
    private final static int COST = 1;

    public Cure() {
        super(ID, GremlinTown.ID, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF, CardColor.COLORLESS);
        exhaust = true;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new RemoveDebuffsAction(adp()));
    }

    public void upp() {
        selfRetain = true;
    }
}
