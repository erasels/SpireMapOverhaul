package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;

public class LightningHelix extends AbstractSMOCard {
    public static final String ID = SpireAnniversary6Mod.makeID("LightningHelix");
    private static final int COST = 0;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int HEALING = 3;
    private static final int UPGRADE_HEALING = 2;

    public LightningHelix() {
        super(ID, COST, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = HEALING;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new LightningEffect(m.drawX, m.drawY), Settings.FAST_MODE ? 0.0F : 0.1F));
        this.addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        this.addToBot(new HealAction(p, p, this.magicNumber));
    }

    public void upp() {
        this.upgradeDamage(UPGRADE_DAMAGE);
        this.upgradeMagicNumber(UPGRADE_HEALING);
    }
}
