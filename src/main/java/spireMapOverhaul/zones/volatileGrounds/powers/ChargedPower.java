package spireMapOverhaul.zones.volatileGrounds.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.volatileGrounds.VolatileGrounds;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class ChargedPower extends AbstractSMOPower {
    public static final String ID = makeID("volChargedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = VolatileGrounds.ID;
    
    public ChargedPower(AbstractCreature owner, int amount) {
        super(ID, NAME, ZONE_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
    }
    
    @Override
    public void onDeath() {
        this.addToBot(new VFXAction(new ExplosionSmallEffect(this.owner.hb.cX, this.owner.hb.cY), 0.1F));
        AbstractGameAction Action = new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.player.damage(new DamageInfo(owner, ChargedPower.this.amount, DamageInfo.DamageType.THORNS));
                for (AbstractMonster target : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    target.tint.color.set(Color.RED);
                    target.tint.changeColor(Color.WHITE.cpy());
                    target.damage(new DamageInfo(owner, ChargedPower.this.amount, DamageInfo.DamageType.THORNS));
                    if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                        AbstractDungeon.actionManager.clearPostCombatActions();
                    }
                }
                isDone = true;
            }
        };
        Action.actionType = AbstractGameAction.ActionType.DAMAGE;
        AbstractDungeon.actionManager.addToBottom(Action);
    }
}
