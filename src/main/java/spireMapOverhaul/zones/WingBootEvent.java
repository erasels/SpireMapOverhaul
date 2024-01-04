package spireMapOverhaul.zones;

import basemod.AutoAdd;

import basemod.ReflectionHacks;

import basemod.eventUtil.EventUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.exordium.Mushrooms;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.brokenSpace.FakeEventRoom;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.megacrit.cardcrawl.core.CardCrawlGame.dungeon;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.currMapNode;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.scene;
import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

@AutoAdd.Ignore
public class WingBootEvent extends AbstractImageEvent {
    public static final String ID = "WingBootEvent";

    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(makeID(ID));

    private AbstractEvent event1;
    private AbstractEvent event2;
    private boolean abort = false;

    public WingBootEvent() {
        super(eventStrings.NAME, eventStrings.DESCRIPTIONS[0], "");


    }

    @Override
    public void onEnterRoom() {
        this.body = eventStrings.DESCRIPTIONS[0];
        event1 = AbstractDungeon.generateEvent(AbstractDungeon.eventRng);

        event2 = new Mushrooms();

        if (event1 != null) {
            event1.imageEventText.clear();
            imageEventText.setDialogOption("event1.NAME");
        }
        if (event2 != null) {
            event2.imageEventText.clear();
            imageEventText.setDialogOption("event2.NAME");
        }
        if (event1 == null && event2 == null) {
            imageEventText.setDialogOption("Something has gone horribly wrong, Leave.");
            abort = true;
        }

        super.onEnterRoom();
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                if (abort) {
                    abort();
                    return;
                }
                setEvent(event1);
                break;
            case 1:
                if (abort) {
                    abort();
                    return;
                }
                setEvent(event2);
                break;
            case 2:
                abort();

        }
    }

    public void setEvent(AbstractEvent event) {
        imageEventText.clear();
        MapRoomNode node = currMapNode;
        node.room = new EventRoom();
        AbstractDungeon.overlayMenu.proceedButton.hide();// 20


        try {
            event = event.getClass().getConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            e.printStackTrace();
            abort();
        }
        node.room.event = event;

        node.room.event.onEnterRoom();

        scene.nextRoom(currMapNode.room);// 2299

        if (currMapNode.room instanceof RestRoom) {// 2301
            AbstractDungeon.rs = AbstractDungeon.RenderScene.CAMPFIRE;// 2302
        } else if (currMapNode.room.event instanceof AbstractImageEvent) {// 2303
            AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;// 2304
        } else {
            AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;// 2306
        }

    }

    private void abort() {
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        imageEventText.clear();
        AbstractDungeon.overlayMenu.proceedButton.show();
    }
}