package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class WallOfBlossoms extends AbstractInvasionZoneRewardCard {
    public static final String ID = SpireAnniversary6Mod.makeID("WallOfBlossoms");
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int DEXTERITY = 2;
    private static final int CARDS = 1;

    public WallOfBlossoms() {
        super(ID, COST, CardType.POWER, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.baseMagicNumber = DEXTERITY;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
        this.addToBot(new DrawCardAction(p, CARDS));
    }

    public void upp() {
        this.upgradeBaseCost(UPGRADE_COST);
    }
}
