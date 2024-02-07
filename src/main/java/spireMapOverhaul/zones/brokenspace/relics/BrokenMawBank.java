package spireMapOverhaul.zones.brokenspace.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.MawBank;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import spireMapOverhaul.SpireAnniversary6Mod;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenMawBank extends BrokenRelic {
    public static final String ID = "BrokenMawBank";
    public static final int AMOUNT = 400;

    private boolean active = false;

    public BrokenMawBank() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, MawBank.ID);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof ShopRoom && !usedUp) {
            this.flash();
            adp().gainGold(AMOUNT);
            active = true;
        }
        super.onEnterRoom(room);
    }

    @Override
    public void setCounter(int setCounter) {
        this.counter = setCounter;
        if (setCounter == -2) {
            this.usedUp();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1] + AMOUNT + DESCRIPTIONS[2];
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = { SaveFile.class })
    public static class BrokenMawBankPatch {
        @SpirePrefixPatch
        public static void MarkRelicAsUsed(AbstractDungeon __instance, SaveFile saveFile) {
            BrokenMawBank brokenMawBank = (BrokenMawBank)adp().getRelic(SpireAnniversary6Mod.makeID(BrokenMawBank.ID));
            if (brokenMawBank != null && brokenMawBank.active) {
                brokenMawBank.active = false;
                brokenMawBank.flash();
                adp().loseGold(AMOUNT);
                brokenMawBank.usedUp();
                brokenMawBank.counter = -2;
            }
        }
    }
}
