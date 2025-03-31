package spireMapOverhaul.zones.humidity.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ExplosivePower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class CustomExplosivePower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("CustomExplosivePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = HumidityZone.ID;
    public int damage=18;

    public CustomExplosivePower(AbstractCreature owner, int amount) {
        super(POWER_ID,NAME,ZONE_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = ExplosivePower.DESCRIPTIONS[3] + this.damage + ExplosivePower.DESCRIPTIONS[2];
        } else {
            this.description = ExplosivePower.DESCRIPTIONS[0] + this.amount + ExplosivePower.DESCRIPTIONS[1] + this.damage + ExplosivePower.DESCRIPTIONS[2];
        }
    }
    public void duringTurn() {
        if (this.amount == 1 && !this.owner.isDying) {
            this.addToBot(new VFXAction(new ExplosionSmallEffect(this.owner.hb.cX, this.owner.hb.cY), 0.1F));
            this.addToBot(new SuicideAction((AbstractMonster)this.owner));
            DamageInfo damageInfo = new DamageInfo(this.owner, this.damage, DamageInfo.DamageType.THORNS);
            this.addToBot(new DamageAction(AbstractDungeon.player, damageInfo, AbstractGameAction.AttackEffect.FIRE, true));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
            this.updateDescription();
        }
    }
}
