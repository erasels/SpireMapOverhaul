package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Healer;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import spireMapOverhaul.util.Wiz;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class JoustAction3 extends AbstractGameAction {
    public static final String ID = makeID("JoustHumidity");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    //Main joust loop. Loops enemy turns until somebody dies.

    RoomEventDialog roomEventText;
    public int enemyIndex;
    public JoustAction3(RoomEventDialog roomEventText){
        //Left Centurion gets summoned into slot 0
        this(roomEventText,0);
    }

    public JoustAction3(RoomEventDialog roomEventText,int enemyIndex){
        this.roomEventText=roomEventText;
        this.enemyIndex=enemyIndex;
    }


    @Override
    public void update() {
        boolean loopIsBroken=false;
        if(AbstractDungeon.getMonsters().monsters.size()<3)
            loopIsBroken=true;
        AbstractEvent event=Wiz.curRoom().event;
        if(event==null || !(event instanceof JoustMidcombatEvent)){
            loopIsBroken=true;
        }
        if(!JoustManagerPower.joustMonstersAreValid())
            loopIsBroken=true;
        if(!loopIsBroken) {
            AbstractMonster enemy = AbstractDungeon.getMonsters().monsters.get(enemyIndex);
            enemy.takeTurn();
            Wiz.atb(new RollMoveAction(enemy));
            Wiz.atb(new UpdateIntentAction(enemy));
            int nextEnemy = (enemyIndex + 1) % 3;
            if (enemy instanceof Healer) {
                Wiz.atb(new BetweenTurnsPowersAction());
            }
            Wiz.atb(new JoustAction3(roomEventText, nextEnemy));
            isDone = true;
        }else{
            Wiz.att(new RemoveSpecificPowerAction(Wiz.adp(), Wiz.adp(), JoustManagerPower.POWER_ID));
            isDone = true;
            //Right Centurion and Mystic escape.
            // This seems to work as expected even if they're dead (i.e. they stay dead).
            Wiz.att(new EscapeAction(Wiz.curRoom().monsters.monsters.get(1)));
            Wiz.att(new EscapeAction(Wiz.curRoom().monsters.monsters.get(2)));

            //If Left Centurion is dead, the battle ends automatically.
            //If Right Centurion is dead, Left is still alive.
            //We want to postpone the end of battle until the player clicks out of the event,
            //so create a new unseen enemy.
            AbstractMonster hidden=new ApologySlime();
            hidden.drawX=-Settings.WIDTH;hidden.drawY=-Settings.HEIGHT;
            Wiz.att(new SpawnMonsterAction(hidden,false));
        }
    }
}
