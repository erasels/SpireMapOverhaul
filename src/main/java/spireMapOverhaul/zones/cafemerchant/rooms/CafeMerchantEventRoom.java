package spireMapOverhaul.zones.cafemerchant.rooms;

import com.megacrit.cardcrawl.events.AbstractEvent;

import spireMapOverhaul.zones.thieveshideout.rooms.ForcedEventRoom;

import java.util.function.Supplier;

public class CafeMerchantEventRoom extends ForcedEventRoom {

    public CafeMerchantEventRoom(Supplier<AbstractEvent> eventSupplier) {
        super(eventSupplier);
    }
}
