package spireMapOverhaul.zones.brokenSpace.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnChannelRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.relics.DataDisk;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenDataDisk extends BrokenRelic implements OnChannelRelic {
    public static final String ID = "BrokenDataDisk";
    public static final int AMOUNT = 3;
    public boolean shouldTrigger = true;

    public BrokenDataDisk() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, DataDisk.ID);
    }

    @Override
    public void onChannel(AbstractOrb abstractOrb) {
        if (!shouldTrigger) {
            return;
        }

        addToTop(new AbstractGameAction() {// 29
            public void update() {
                shouldTrigger = true;
                this.isDone = true;// 40
            }// 41
        });
        addToTop(new ChannelAction(abstractOrb.makeCopy()));
        shouldTrigger = false;
    }

    @Override
    public void atBattleStart() {
        this.addToBot(new AbstractGameAction() {// 29
            public void update() {
                flash();
                AbstractDungeon.player.addPower(new FocusPower(AbstractDungeon.player, -AMOUNT));
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, BrokenDataDisk.this));
                AbstractDungeon.onModifyPower();
                this.isDone = true;// 40
            }// 41
        });
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
