package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.exordium.FungiBeast;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.HallucinationCloudPower;

public class FungiBeastHallucinations {

    @SpirePatch2(clz = FungiBeast.class, method = "usePreBattleAction")
    public static class PowerPatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> usePreBattleAction(FungiBeast __instance){
            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new HallucinationCloudPower(__instance, 2)));
            return SpireReturn.Return();
        }
    }
}
