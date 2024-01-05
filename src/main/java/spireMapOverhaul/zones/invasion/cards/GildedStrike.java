package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class GildedStrike extends AbstractInvasionZoneCard {
    public static final String ID = SpireAnniversary6Mod.makeID("GildedStrike");
    private static final int COST = 1;
    private static final int BASE_DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int VULNERABLE_AMOUNT = 1;
    private static final int UPGRADE_VULNERABLE = 1;

    public GildedStrike() {
        super(ID, COST, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = VULNERABLE_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
        this.tags.add(CardTags.STRIKE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
    }

    public void upp() {
        this.upgradeDamage(UPGRADE_DAMAGE);
        this.upgradeMagicNumber(UPGRADE_VULNERABLE);
    }
}
