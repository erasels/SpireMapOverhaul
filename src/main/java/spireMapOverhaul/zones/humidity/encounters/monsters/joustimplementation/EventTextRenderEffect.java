package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import spireMapOverhaul.util.Wiz;

public class EventTextRenderEffect extends AbstractGameEffect {
    AbstractEvent event;
    public EventTextRenderEffect(AbstractEvent event){
        this.event=event;
        color=new Color(1,1,1,1);
    }

    @Override
    public void update() {
        //several event processes are hardcoded to check for EVENT phase
        AbstractRoom.RoomPhase actualPhase=Wiz.curRoom().phase;
        Wiz.curRoom().phase = AbstractRoom.RoomPhase.EVENT;

        event.update();
        event.updateDialog();
        Wiz.curRoom().eventControllerInput();

        Wiz.curRoom().phase=actualPhase;
    }

    @Override
    public void render(SpriteBatch sb) {
        event.renderRoomEventPanel(sb);
        event.renderText(sb);
    }

    @Override
    public void dispose() {

    }
}
