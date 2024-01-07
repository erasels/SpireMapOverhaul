package spireMapOverhaul.zones.brokenSpace.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnChannelRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.relics.DataDisk;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenDataDisk extends BrokenRelic implements OnChannelRelic {
    public static final String ID = "BrokenDataDisk";
    public static final int AMOUNT = 1;
    private boolean usedThisTurn = false;

    public BrokenDataDisk() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, DataDisk.ID);
    }

    @Override
    public void onChannel(AbstractOrb abstractOrb) {
        if (usedThisTurn) {
            return;
        }
        addToBot(new ChannelAction(abstractOrb.makeCopy()));
        this.flash();
        usedThisTurn = true;

    }


    @Override
    public void atTurnStart() {
        super.atTurnStart();
        usedThisTurn = false;
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new ApplyPowerAction(adp(), adp(), new FocusPower(adp(), -AMOUNT), -AMOUNT));
        addToBot(new RelicAboveCreatureAction(adp(), BrokenDataDisk.this));
        usedThisTurn = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }

    @Override
    public boolean canSpawn() {
        return adp().chosenClass == AbstractPlayer.PlayerClass.DEFECT;
    }
}
