package spireMapOverhaul.zones.frostlands.powers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import spireMapOverhaul.abstracts.AbstractSMOPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class LeavePower extends AbstractSMOPower{
    public static String POWER_ID = makeID(LeavePower.class.getSimpleName());
    public static PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static String[] DESCRIPTIONS = strings.DESCRIPTIONS;

    public LeavePower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, "Frostlands", AbstractPower.PowerType.BUFF, false, owner, 0);
    }

    @Override
    public void onDeath() {
        owner.currentHealth = 1;
        escape();
    }
/*
    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount){
        boolean is
        for (AbstractMonster m: AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractDungeon.getCurrRoom().is
        }
        if(damageAmount >= owner.currentHealth)
        {
            escape();
            damageAmount = owner.currentHealth - 1;
        }
        return damageAmount;
    }
*/
    public void escape()
    {
        addToBot(new VFXAction(new SmokeBombEffect(owner.hb.cX, owner.hb.cY)));
        if(owner instanceof AbstractMonster)
            AbstractDungeon.actionManager.addToBottom(new EscapeAction((AbstractMonster) owner));
        owner.isEscaping = true;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
