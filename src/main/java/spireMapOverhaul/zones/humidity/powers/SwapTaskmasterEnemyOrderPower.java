package spireMapOverhaul.zones.humidity.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Taskmaster;
import com.megacrit.cardcrawl.monsters.exordium.SlaverBlue;
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class SwapTaskmasterEnemyOrderPower extends AbstractSMOPower implements InvisiblePower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("BaseballPower");
    public static final String NAME = "(DNT)";
    public static String ZONE_ID = HumidityZone.ID;

    public SwapTaskmasterEnemyOrderPower(AbstractCreature owner) {
        super(POWER_ID,NAME,ZONE_ID, AbstractPower.PowerType.BUFF, false, owner, 1);
    }

    @Override
    public void atEndOfRound(){
        if(Wiz.curRoom().monsters.monsters.size()>=3) {
            AbstractMonster mon0 = Wiz.curRoom().monsters.monsters.get(0);
            AbstractMonster mon1 = Wiz.curRoom().monsters.monsters.get(1);
            AbstractMonster mon2 = Wiz.curRoom().monsters.monsters.get(2);
            if(mon0 instanceof SlaverRed && mon1 instanceof SlaverBlue && mon2 instanceof Taskmaster){
                //imagine three slavers on the edge of a cliff
                Wiz.curRoom().monsters.monsters.set(0,mon1);
                Wiz.curRoom().monsters.monsters.set(1,mon2);
                Wiz.curRoom().monsters.monsters.set(2,mon0);
            }
        }
        Wiz.atb(new RemoveSpecificPowerAction(this.owner,this.owner,this));
    }
}
