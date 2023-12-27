package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import spireMapOverhaul.SpireAnniversary6Mod;

public class EarthblessedBlade extends AbstractInvasionZoneRewardCard {
    public static final String ID = SpireAnniversary6Mod.makeID("EarthblessedBlade");
    private static final int COST = 1;
    private static final int BASE_DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int PLATED_ARMOR = 4;
    private static final int UPGRADE_PLATED_ARMOR = 1;
    private static final int THORNS = 2;
    private static final int UPGRADE_THORNS = 1;

    public EarthblessedBlade() {
        super(ID, COST, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ALL_ENEMY, CardColor.COLORLESS);
        this.baseDamage = BASE_DAMAGE;
        this.baseBlock = PLATED_ARMOR;
        this.baseMagicNumber = THORNS;
        this.magicNumber = this.baseMagicNumber;
        this.isMultiDamage = true;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new SFXAction("ATTACK_HEAVY"));
        this.addToBot(new VFXAction(p, new CleaveEffect(), 0.1F));
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        this.addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, this.baseBlock), this.baseBlock));
        this.addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    protected void applyPowersToBlock() {
    }

    public void upp() {
        this.upgradeDamage(UPGRADE_DAMAGE);
        this.upgradeBlock(UPGRADE_PLATED_ARMOR);
        this.upgradeMagicNumber(UPGRADE_THORNS);
    }
}
