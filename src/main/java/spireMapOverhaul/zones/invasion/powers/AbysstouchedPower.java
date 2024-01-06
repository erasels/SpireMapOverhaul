package spireMapOverhaul.zones.invasion.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AbysstouchedPower extends AbstractInvasionPower implements HealthBarRenderPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("Abysstouched");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AbysstouchedPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        if (!this.owner.isPlayer && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flashWithoutSound();
            this.playApplyPowerSfx();
            this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS)));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.owner.isPlayer) {
            this.flashWithoutSound();
            this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS)));
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * (1 + this.amount / 100.0F);
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        this.description = (this.owner != null && this.owner.isPlayer ? DESCRIPTIONS[0] : DESCRIPTIONS[1]).replace("{0}", this.amount + "");
    }

    @Override
    public int getHealthBarAmount() {
        return this.owner.isPlayer ? 0 : this.owner.hasPower(BarricadePower.POWER_ID) ? Math.max(0, this.amount - this.owner.currentBlock) : this.amount;
    }

    @Override
    public Color getColor() {
        return new Color(-1608453889);
    }

    public static final String AbyssModId = "Abyss";
    public static final String CorruptTheSpireModId = "CorruptTheSpire";
    private static Constructor<?> abysstouchedConstructor = null;

    public static AbstractPower create(AbstractCreature owner, int amount) {
        if (Loader.isModLoaded(AbyssModId)) {
            try {
                if (abysstouchedConstructor == null) {
                    abysstouchedConstructor = Class.forName("abyss.powers.AbysstouchedPower").getConstructor(AbstractCreature.class, int.class);
                }
                return (AbstractPower)abysstouchedConstructor.newInstance(owner, amount);
            } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not create AbysstouchedPower");
            }
        }
        else if (Loader.isModLoaded(CorruptTheSpireModId)) {
            try {
                if (abysstouchedConstructor == null) {
                    abysstouchedConstructor = Class.forName("corruptthespire.powers.AbysstouchedPower").getConstructor(AbstractCreature.class, int.class);
                }
                return (AbstractPower)abysstouchedConstructor.newInstance(owner, amount);
            } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not create AbysstouchedPower");
            }
        }
        return new AbysstouchedPower(owner, amount);
    }
}
