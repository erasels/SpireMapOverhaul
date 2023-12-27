package spireMapOverhaul.zones.invasion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;

public class ReduceAllEnemyRegenAction extends AbstractGameAction {
    private int reduceAmount;

    public ReduceAllEnemyRegenAction(int reduceAmount) {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_XFAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_FAST;
        }

        this.duration = this.startDuration;
        this.reduceAmount = reduceAmount;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDying) {
                    AbstractPower regenPower = m.getPower(RegenerateMonsterPower.POWER_ID);
                    if (regenPower != null) {
                        if (reduceAmount >= regenPower.amount) {
                            this.addToTop(new RemoveSpecificPowerAction(m, null, RegenerateMonsterPower.POWER_ID));
                        }
                        else {
                            this.addToTop(new ApplyPowerAction(m, null, new RegenerateMonsterPower(m, -this.reduceAmount)));
                        }
                    }
                }
            }
        }

        this.tickDuration();
    }
}
