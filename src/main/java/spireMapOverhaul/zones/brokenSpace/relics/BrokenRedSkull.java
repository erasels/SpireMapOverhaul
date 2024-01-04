package spireMapOverhaul.zones.brokenSpace.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
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
        this.isActive = false;
        this.addToBot(new AbstractGameAction() {
            public void update() {
                if (!isActive && !AbstractDungeon.player.isBloodied) {
                    flash();
                    pulse = true;
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, DEX_AMT), DEX_AMT));
                    addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, BrokenRedSkull.this));
                    isActive = true;

                }

                this.isDone = true;
            }
        });
    }

    public void onBloodied() {
        if (this.isActive && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -DEX_AMT), -DEX_AMT));
        }

        this.stopPulse();
        this.isActive = false;


    }

    public void onNotBloodied() {
        this.flash();
        this.pulse = true;
        if (!this.isActive && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer p = AbstractDungeon.player;
            this.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, DEX_AMT), DEX_AMT));
            this.addToTop(new RelicAboveCreatureAction(p, this));
            this.isActive = true;
            AbstractDungeon.player.hand.applyPowers();
        }
    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + 50 + DESCRIPTIONS[1] + DEX_AMT + DESCRIPTIONS[2];
    }

    @Override
    public boolean canSpawn() {
        return adp().chosenClass == AbstractPlayer.PlayerClass.IRONCLAD;
    }
}
