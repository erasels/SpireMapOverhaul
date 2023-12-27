package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import spireMapOverhaul.SpireAnniversary6Mod;

public class LightningBolt extends AbstractInvasionZoneRewardCard {
    public static final String ID = SpireAnniversary6Mod.makeID("LightningBolt");
    private static final int COST = 1;
    private static final int DAMAGE = 11;
    private static final int UPGRADE_DAMAGE = 4;

    public LightningBolt() {
        super(ID, COST, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
        this.baseDamage = DAMAGE;
        this.shuffleBackIntoDrawPile = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new LightningEffect(m.drawX, m.drawY), Settings.FAST_MODE ? 0.0F : 0.1F));
        this.addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    public void upp() {
        this.upgradeDamage(UPGRADE_DAMAGE);
    }
}
