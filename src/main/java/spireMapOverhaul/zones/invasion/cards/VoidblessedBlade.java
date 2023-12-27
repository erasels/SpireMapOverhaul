package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;

public class VoidblessedBlade extends AbstractSMOCard {
    public static final String ID = SpireAnniversary6Mod.makeID("VoidblessedBlade");
    private static final int COST = 1;
    private static final int BASE_DAMAGE = 4;
    private static final int BASE_HITS_STRENGTH = 2;
    private static final int UPGRADE_HITS_STRENGTH = 1;
    private static final int BLUR = 1;

    public VoidblessedBlade() {
        super(ID, COST, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = BASE_HITS_STRENGTH;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
        this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -this.magicNumber), -this.magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new BlurPower(p, BLUR), BLUR));
    }

    public void upp() {
        this.upgradeMagicNumber(UPGRADE_HITS_STRENGTH);
    }
}
