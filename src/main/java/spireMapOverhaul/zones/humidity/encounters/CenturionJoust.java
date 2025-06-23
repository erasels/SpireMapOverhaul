package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Centurion;
import com.megacrit.cardcrawl.monsters.city.Healer;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation.JoustManagerPower;

public class CenturionJoust {

    @SpirePatch2(clz = AbstractMonster.class, method = "usePreBattleAction")
    public static class JoustManagerPowerPatch{
        @SpirePostfixPatch
        public static void usePreBattleAction(AbstractMonster __instance){
            if(HumidityZone.isNotInZone())return;
            if(!(__instance instanceof Centurion))return;
            if(Wiz.getEnemies().size()==2
            && Wiz.getEnemies().get(0) instanceof Centurion
            && Wiz.getEnemies().get(1) instanceof Healer) {
                Wiz.att(new ApplyPowerAction(Wiz.adp(), Wiz.adp(), new JoustManagerPower(Wiz.adp())));
            }
        }
    }

}
