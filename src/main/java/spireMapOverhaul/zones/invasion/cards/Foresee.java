package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;

public class Foresee extends AbstractInvasionZoneRewardCard {
    public static final String ID = SpireAnniversary6Mod.makeID("Foresee");
    private static final int COST = 1;
    private static final int SCRY = 4;
    private static final int UPGRADE_SCRY = 2;
    private static final int DRAW = 2;

    public Foresee() {
        super(ID, COST, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.baseMagicNumber = SCRY;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ScryAction(this.magicNumber));
        this.addToBot(new DrawCardAction(DRAW));
    }

    public void upp() {
        this.upgradeMagicNumber(UPGRADE_SCRY);
    }
}
