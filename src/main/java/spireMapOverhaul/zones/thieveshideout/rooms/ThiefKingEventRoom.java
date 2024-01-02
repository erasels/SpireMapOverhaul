package spireMapOverhaul.zones.thieveshideout.rooms;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.EventRoom;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.thieveshideout.events.ThiefKingEvent;

public class ThiefKingEventRoom extends EventRoom {
    @Override
    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        this.event = new ThiefKingEvent();
        this.event.onEnterRoom();
    }
}
