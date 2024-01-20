package spireMapOverhaul.zones.wildfire.powers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameParticleEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.wildfire.Wildfire;
import spireMapOverhaul.zones.wildfire.actions.BurningLoseHPAction;

public class BurningPower extends AbstractSMOPower implements HealthBarRenderPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID(BurningPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Color color = Color.ORANGE.cpy();
    private float particleTimer;

    public BurningPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, Wildfire.ID, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.05F);
    }

    @Override
    public void updateDescription() {
        if (owner instanceof AbstractPlayer) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flashWithoutSound();
            addToBot(new DamageAction(owner, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void atStartOfTurn() {
        if (!(owner instanceof AbstractPlayer) && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flashWithoutSound();
            addToBot(new BurningLoseHPAction(owner, owner, amount));
        }
    }

    @Override
    public int getHealthBarAmount() {
        if (owner instanceof AbstractPlayer) {
            return Math.max(0, amount - owner.currentBlock);
        }
        return amount;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void updateParticles() {
        particleTimer -= Gdx.graphics.getDeltaTime();
        if (particleTimer <= 0) {
            particleTimer = 1f/Math.min(10, Math.max(1, amount));
            for(int i = 0; i < 4; ++i) {// 21
                AbstractDungeon.effectsQueue.add(new FlameParticleEffect(owner.hb.cX, owner.hb.cY));
            }

            for(int i = 0; i < 1; ++i) {// 24
                AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(owner.hb.cX, owner.hb.cY));
            }
        }
    }
}
