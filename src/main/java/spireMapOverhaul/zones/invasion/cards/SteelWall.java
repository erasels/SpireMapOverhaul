package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;

public class SteelWall extends AbstractSMOCard {
    public static final String ID = SpireAnniversary6Mod.makeID("SteelWall");
    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int UPGRADE_BLOCK = 1;
    private static final int METALLICIZE = 1;
    private static final int UPGRADE_METALLICIZE = 1;

    public SteelWall() {
        super(ID, COST, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = METALLICIZE;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
        this.addToBot(new ApplyPowerAction(p, p, new MetallicizePower(p, this.magicNumber), this.magicNumber));
    }

    public void upp() {
        this.upgradeBlock(UPGRADE_BLOCK);
        this.upgradeMagicNumber(UPGRADE_METALLICIZE);
    }
}
