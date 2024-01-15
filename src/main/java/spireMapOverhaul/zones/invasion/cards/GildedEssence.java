package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class GildedEssence extends AbstractInvasionZoneCard {
    public static final String ID = SpireAnniversary6Mod.makeID("GildedEssence");
    private static final int COST = 1;
    private static final int BASE_MAGIC_NUMBER = 2;
    private static final int UPGRADE_MAGIC_NUMBER = 1;

    public GildedEssence() {
        super(ID, COST, CardType.POWER, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.baseMagicNumber = BASE_MAGIC_NUMBER;
		this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, this.magicNumber), this.magicNumber));
    }

    public void upp() {
        this.upgradeMagicNumber(UPGRADE_MAGIC_NUMBER);
    }
}
