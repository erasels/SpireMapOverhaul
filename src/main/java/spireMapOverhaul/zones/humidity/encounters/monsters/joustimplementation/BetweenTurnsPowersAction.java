package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.util.Wiz;

public class BetweenTurnsPowersAction extends AbstractGameAction {
    @Override
    public void update() {
        for (AbstractMonster m : Wiz.getEnemies()) {
            m.applyStartOfTurnPowers();
            m.applyTurnPowers();
            m.applyStartOfTurnPostDrawPowers();
            m.applyEndOfTurnTriggers();
        }
        Wiz.curRoom().monsters.applyEndOfTurnPowers();
        for (AbstractMonster m : Wiz.getEnemies()) {
            m.loseBlock();
        }
        //for later: check barricade/calipers.  it doesn't matter mechanically though.
        Wiz.adp().loseBlock();
        isDone = true;
    }
}
