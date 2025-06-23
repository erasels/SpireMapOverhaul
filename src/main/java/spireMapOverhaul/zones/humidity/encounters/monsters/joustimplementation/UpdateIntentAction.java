package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UpdateIntentAction extends AbstractGameAction {

    AbstractMonster monster;
    public UpdateIntentAction(AbstractMonster monster){
        this.monster=monster;
    }

    @Override
    public void update() {
        //monster.rollMove();
        monster.createIntent();
        isDone=true;
    }
}
