package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.invasion.powers.HandOfTheAbyssPower;

public class HandOfTheAbyss extends AbstractSMOCard {
    public static final String ID = SpireAnniversary6Mod.makeID("HandOfTheAbyss");
    private static final int COST = 1;
    private static final int AMOUNT = 2;
    private static final int UPGRADE_AMOUNT = 1;

    public HandOfTheAbyss() {
        super(ID, COST, CardType.POWER, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.baseMagicNumber = AMOUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new HandOfTheAbyssPower(p, this.magicNumber), this.magicNumber));
    }

    public void upp() {
        this.upgradeMagicNumber(UPGRADE_AMOUNT);
    }
}
