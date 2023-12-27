package spireMapOverhaul.zones.invasion.actions;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.zones.invasion.InvasionZone;
import spireMapOverhaul.zones.invasion.powers.DurableMinionPower;

public class SummonElementalAction extends AbstractGameAction {
    private final AbstractMonster m;
    private final int strength;
    private final int block;

    public SummonElementalAction(String elementalID, float x, float y, int strength, int block) {
        this(elementalID, x, y, null, -1, strength, block, false);
    }

    public SummonElementalAction(String elementalID, float x, float y, AbstractMonster[] elementals, int slot, int strength, int block, boolean firstTurn) {
        this.actionType = ActionType.SPECIAL;
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }

        this.duration = this.startDuration;
        this.strength = strength;
        this.block = block;
        this.m = InvasionZone.getElemental(elementalID, x, y);
        if (!firstTurn) {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onSpawnMonster(this.m);
            }
        }
        if (elementals != null && slot != -1) {
            elementals[slot] = m;
        }
    }

    private int getSmartPosition() {
        int position = 0;

        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (this.m.drawX <= m.drawX) {
                break;
            }
        }

        return position;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            this.m.animX = 1200.0F * Settings.scale;
            this.m.init();
            this.m.applyPowers();
            AbstractDungeon.getCurrRoom().monsters.addMonster(this.getSmartPosition(), this.m);
            if (ModHelper.isModEnabled("Lethality")) {
                this.addToBot(new ApplyPowerAction(this.m, this.m, new StrengthPower(this.m, 3), 3));
            }

            if (ModHelper.isModEnabled("Time Dilation")) {
                this.addToBot(new ApplyPowerAction(this.m, this.m, new SlowPower(this.m, 0)));
            }
            if (strength != 0) {
                this.addToBot(new ApplyPowerAction(this.m, this.m, new StrengthPower(this.m, this.strength), this.strength));
            }
            if (block != 0) {
                this.addToBot(new GainBlockAction(this.m, this.block));
            }

            this.addToBot(new ApplyPowerAction(this.m, this.m, new MinionPower(this.m)));
            this.addToBot(new ApplyPowerAction(this.m, this.m, new DurableMinionPower(this.m)));
        }

        this.tickDuration();
        if (this.isDone) {
            this.m.animX = 0.0F;
            this.m.showHealthBar();
            this.m.usePreBattleAction();
        } else {
            this.m.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.scale, this.duration);
        }

    }
}
