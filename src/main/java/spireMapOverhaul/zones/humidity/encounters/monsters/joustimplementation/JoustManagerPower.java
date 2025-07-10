package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Centurion;
import com.megacrit.cardcrawl.monsters.city.Healer;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.InvisibleDrawReductionSingleTurnPower;

public class JoustManagerPower extends AbstractSMOPower implements InvisiblePower {
    public static String POWER_ID = SpireAnniversary6Mod.makeID("JoustManagerPower");
    public static String NAME = "Joust Manager";
    public static String ZONE_ID = HumidityZone.ID;
    public int turnCount = 0;

    public JoustManagerPower(AbstractCreature owner) {
        super(POWER_ID, NAME, ZONE_ID, NeutralPowertypePatch.NEUTRAL, false, owner, 0);
    }

    @Override
    public void atStartOfTurn() {
        turnCount += 1;
        if (turnCount == 2) {
            if (preJoustMonstersAreValid())
                Wiz.att(new JoustAction1());
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        Wiz.att(new ApplyPowerAction(Wiz.adp(), Wiz.adp(), new InvisibleDrawReductionSingleTurnPower(Wiz.adp(), 99999)));
    }

    public static boolean preJoustMonstersAreValid() {
        if (HumidityZone.isNotInZone()) return false;
        if (Wiz.getEnemies().size() != 2) return false;
        if (!(Wiz.getEnemies().get(0) instanceof Centurion
                && Wiz.getEnemies().get(1) instanceof Healer)) return false;
        //getEnemies does not filter out monsters who are "dying" but not "dead". Do that here.
        for (AbstractMonster m : Wiz.getEnemies()) {
            if (m.isDying) return false;
        }
        return true;
    }

    public static boolean joustMonstersAreValid() {
        if (HumidityZone.isNotInZone()) return false;
        if (Wiz.getEnemies().size() != 3) return false;
        if (!(Wiz.getEnemies().get(0) instanceof Centurion
                && Wiz.getEnemies().get(1) instanceof Centurion
                && Wiz.getEnemies().get(2) instanceof Healer)) return false;
        for (AbstractMonster m : Wiz.getEnemies()) {
            if (m.isDying) return false;
        }
        return true;
    }
}
