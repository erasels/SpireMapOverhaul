package spireMapOverhaul.zones.thieveshideout.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.util.Wiz;

public class WeakLooter extends Looter {
    public WeakLooter(float x, float y) {
        super(x, y);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        Wiz.atb(new ApplyPowerAction(this, this, new StrengthPower(this, -2)));
    }
}
