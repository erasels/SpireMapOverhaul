package spireMapOverhaul.zones.brokenSpace.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.SneckoSkull;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenSneckoSkull extends BrokenRelic implements OnReceivePowerRelic {
    public static final String ID = "BrokenSneckoSkull";
    public static final int AMOUNT = 1;

    public BrokenSneckoSkull() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, SneckoSkull.ID);
    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }


    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature) {
        flash();
        if (abstractPower.type == AbstractPower.PowerType.BUFF) {
            abstractPower.amount += AMOUNT;
        }
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature source, int stackAmount) {
        flash();
        if (power.type == AbstractPower.PowerType.BUFF) {
            return stackAmount + AMOUNT;
        }
        return stackAmount;
    }

    @Override
    public boolean canSpawn() {
        return adp().chosenClass == AbstractPlayer.PlayerClass.THE_SILENT;
    }
}
