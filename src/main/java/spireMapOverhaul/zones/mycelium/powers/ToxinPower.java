package spireMapOverhaul.zones.mycelium.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.mycelium.Mycelium;
import spireMapOverhaul.zones.volatileGrounds.VolatileGrounds;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class ToxinPower extends AbstractSMOPower {
    public static final String ID = makeID("mycToxinPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = Mycelium.ID;
    
    public ToxinPower(AbstractCreature owner, int amount) {
        super(ID, NAME, ZONE_ID, PowerType.DEBUFF, false, owner, amount);
        updateDescription();
    }
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
    }
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
            damageAmount += amount;
        }
        
        return damageAmount;
    }
}
