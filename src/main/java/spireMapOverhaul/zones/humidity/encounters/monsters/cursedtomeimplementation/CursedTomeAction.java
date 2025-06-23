package spireMapOverhaul.zones.humidity.encounters.monsters.cursedtomeimplementation;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation.EventTextRenderEffect;

public class CursedTomeAction extends AbstractGameAction {

    RoomEventDialog roomEventText;
    boolean alreadyCalledEvent = false;
    int turnCount=0;

    public CursedTomeAction(int turnCount){
        super();
        this.turnCount=turnCount;
    }


    @Override
    public void update() {
        AbstractEvent event=Wiz.curRoom().event;
        if(!alreadyCalledEvent){
            alreadyCalledEvent = true;
            if(turnCount==1) {
                AbstractEvent.type = AbstractEvent.EventType.ROOM;
                AbstractEvent ctme = new CursedTomeMidcombatEvent();
                Wiz.curRoom().event = ctme;
                EventTextRenderEffect etre = new EventTextRenderEffect(ctme);
                AbstractDungeon.topLevelEffectsQueue.add(etre);
            }else if(event==null || !(event instanceof CursedTomeMidcombatEvent)){
                isDone = true;
            }else{
                ((CursedTomeMidcombatEvent)event).setScreen(turnCount);
            }
        }
        else
        {
            if (event == null || !(event instanceof CursedTomeMidcombatEvent)) {
                isDone = true;
            }
            int screen=((CursedTomeMidcombatEvent)event).screen;
            if(0 < screen && screen < 5) {
                isDone = true;
            }
        }

    }
}

