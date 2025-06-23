package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.SphericGuardian;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.BaseballPower;

public class SphericGuardianBaseball {
    @SpirePatch2(clz= SphericGuardian.class,method= "usePreBattleAction")
    public static class BaseballPatch {
        @SpirePostfixPatch
        public static void Foo(SphericGuardian __instance){
            if(HumidityZone.isNotInZone())return;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new BaseballPower(__instance,0)));
        }
    }
}
