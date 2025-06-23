package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Centurion;
import spireMapOverhaul.util.Wiz;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class JoustAction2 extends AbstractGameAction {
    public static final String ID = makeID("JoustHumidity");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    //Intro joust loop. Prevents actions until player makes a bet.

    RoomEventDialog roomEventText;
    public JoustAction2(RoomEventDialog roomEventText){
        this.roomEventText=roomEventText;
    }

    @Override
    public void update() {
        if(AbstractDungeon.getMonsters().monsters.size()>=3){
            AbstractMonster leftCenturion=AbstractDungeon.getMonsters().monsters.get(2);
            if(leftCenturion.isDeadOrEscaped()){
                Wiz.att(new RemoveSpecificPowerAction(Wiz.adp(),Wiz.adp(),JoustManagerPower.POWER_ID));
                isDone=true;
            }else {
                AbstractEvent event=Wiz.curRoom().event;
                if(event==null || !(event instanceof JoustMidcombatEvent) || ((JoustMidcombatEvent)event).screen>2){
                    isDone = true;
                    Wiz.att(new JoustAction3(roomEventText));
                }
            }
        }else{
            Wiz.att(new RemoveSpecificPowerAction(Wiz.adp(),Wiz.adp(),JoustManagerPower.POWER_ID));
            isDone=true;
        }
    }
}
