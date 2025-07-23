package spireMapOverhaul.zones.humidity.encounters.monsters.cursedtomeimplementation;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class CursedTomeManagerPower extends AbstractSMOPower implements InvisiblePower {
    public static String POWER_ID = SpireAnniversary6Mod.makeID("CursedTomeManagerPower");
    public static String NAME = "Cursed Tome Manager";
    public static String ZONE_ID = HumidityZone.ID;
    public int turnCount = 0;

    public CursedTomeManagerPower(AbstractCreature owner) {
        super(POWER_ID, NAME, ZONE_ID, NeutralPowertypePatch.NEUTRAL, false, owner, 0);
    }

    @Override
    public void atStartOfTurn() {
        turnCount += 1;
        if (turnCount <= 5) {
            Wiz.att(new CursedTomeAction(turnCount));
        }
    }

}