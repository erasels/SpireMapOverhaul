package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.invasion.powers.MirarisWakePower;

public class MirarisWake extends AbstractSMOCard {
    public static final String ID = SpireAnniversary6Mod.makeID("MirarisWake");
    private static final int COST = 3;
    private static final int STATS = 1;
    private static final int UPGRADE_STATS = 1;

    public MirarisWake() {
        super(ID, COST, CardType.POWER, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.baseMagicNumber = STATS;
        this.magicNumber = this.baseMagicNumber;
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new MirarisWakePower(p, 1), 1));
    }

    public void upp() {
        this.isEthereal = false;
        this.upgradeMagicNumber(UPGRADE_STATS);
    }
}
