package spireMapOverhaul.zones.brokenSpace.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import com.megacrit.cardcrawl.relics.RedSkull;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenRedSkull extends BrokenRelic {
    public static final String ID = "BrokenRedSkull";
    private static final int DEX_AMT = 3;
    private boolean isActive = false;

    public BrokenRedSkull() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, RedSkull.ID);
    }

    public void atBattleStart() {
        this.isActive = false;// 28
        this.addToBot(new AbstractGameAction() {// 29
            public void update() {
                if (!isActive && !AbstractDungeon.player.isBloodied) {// DEX_AMT2
                    flash();// DEX_AMTDEX_AMT
                    pulse = true;// DEX_AMT4
                    AbstractDungeon.player.addPower(new DexterityPower(AbstractDungeon.player, DEX_AMT));// DEX_AMT5
                    this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, BrokenRedSkull.this));// DEX_AMT6
                    isActive = true;// DEX_AMT7
                    AbstractDungeon.onModifyPower();// DEX_AMT8
                }

                this.isDone = true;// 40
            }// 41
        });
    }// 44

    public void onBloodied() {
        if (this.isActive && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {// 61
            AbstractPlayer p = AbstractDungeon.player;// 62
            this.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, -DEX_AMT), -DEX_AMT));// 6DEX_AMT
        }

        this.stopPulse();// 65
        this.isActive = false;// 66
        AbstractDungeon.player.hand.applyPowers();// 67

    }// 57

    public void onNotBloodied() {
        this.flash();// 48
        this.pulse = true;// 49
        if (!this.isActive && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {// 50
            AbstractPlayer p = AbstractDungeon.player;// 51
            this.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, DEX_AMT), DEX_AMT));// 52
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));// 5DEX_AMT
            this.isActive = true;// 54
            AbstractDungeon.player.hand.applyPowers();// 55
        }
    }// 68


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + 50 + DESCRIPTIONS[1] + DEX_AMT + DESCRIPTIONS[2];
    }

    @Override
    public boolean canSpawn() {
        return adp().chosenClass == AbstractPlayer.PlayerClass.IRONCLAD;
    }
}
