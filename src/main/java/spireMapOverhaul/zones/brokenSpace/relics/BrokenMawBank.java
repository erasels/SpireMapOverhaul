package spireMapOverhaul.zones.brokenSpace.relics;

import com.megacrit.cardcrawl.relics.MawBank;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenMawBank extends BrokenRelic {
    public static final String ID = "BrokenMawBank";
    public static final int AMOUNT = 400;


    private boolean used = false;
    private boolean active = false;

    public BrokenMawBank() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, MawBank.ID);

        if (counter == -2) {
            grayscale = true;
            used = true;
        }
    }


    @Override
    public void onEnterRoom(AbstractRoom room) {

        if (active) {
            active = false;
            this.flash();
            adp().loseGold(AMOUNT);
            used = true;
            grayscale = true;
            counter = -2;
        }

        if (room instanceof ShopRoom && !used) {
            this.flash();
            adp().gainGold(AMOUNT);
            active = true;
        }
        super.onEnterRoom(room);
    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1] + AMOUNT + DESCRIPTIONS[2];
    }
}
