package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class IceblessedBlade extends AbstractInvasionZoneRewardCard {
    public static final String ID = SpireAnniversary6Mod.makeID("IceblessedBlade");
    private static final int COST = 1;
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int ARTIFACT = 1;
    private static final int UPGRADE_ARTIFACT = 1;
    private static final int BUFFER = 1;

    public IceblessedBlade() {
        super(ID, COST, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = ARTIFACT;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, this.magicNumber)));
        this.addToBot(new ApplyPowerAction(p, p, new BufferPower(p, BUFFER)));
    }

    public void upp() {
        this.upgradeDamage(UPGRADE_DAMAGE);
        this.upgradeMagicNumber(UPGRADE_ARTIFACT);
    }
}
