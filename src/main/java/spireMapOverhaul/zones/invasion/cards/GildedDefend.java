package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class GildedDefend extends AbstractInvasionZoneCard {
    public static final String ID = SpireAnniversary6Mod.makeID("GildedDefend");
    private static final int COST = 1;
    private static final int BASE_BLOCK = 7;
    private static final int UPGRADE_BLOCK = 3;
    private static final int DEXTERITY_AMOUNT = 1;

    public GildedDefend() {
        super(ID, COST, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.baseBlock = BASE_BLOCK;
        this.baseMagicNumber = DEXTERITY_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block));
        this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -this.magicNumber), -this.magicNumber));
    }

    public void upp() {
        this.upgradeBlock(UPGRADE_BLOCK);
    }
}
