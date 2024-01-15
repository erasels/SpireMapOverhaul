package spireMapOverhaul.zones.thieveshideout.rooms;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.rooms.EventRoom;

import java.util.function.Supplier;

public class ForcedEventRoom extends EventRoom {
    private Supplier<AbstractEvent> eventSupplier;

    public ForcedEventRoom(Supplier<AbstractEvent> eventSupplier) {
        this.eventSupplier = eventSupplier;
    }
    @Override
    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        this.event = eventSupplier.get();
        this.event.onEnterRoom();
    }
}
