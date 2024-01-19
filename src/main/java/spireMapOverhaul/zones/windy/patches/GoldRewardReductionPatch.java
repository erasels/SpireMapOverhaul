package spireMapOverhaul.zones.windy.patches;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class GoldRewardReductionPatch {
    public static int combatGoldReduction = 0;

    //on the first call to addGoldToRewards, reduce gold reward param
    @SpirePatch2(clz = AbstractRoom.class, method = "addGoldToRewards")
    public static class GoldReductionPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(AbstractRoom __instance, @ByRef int[] gold) {
            gold[0] = gold[0]-combatGoldReduction;
            combatGoldReduction = 0;

            if(gold[0] <= 0){
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    //to have save and load retain gold reduction:
    public static class SavableCombatGoldReduction implements CustomSavable<Integer> {
        public final static String SaveKey = "WindyCombatGoldReduction";

        @Override
        public Integer onSave() {
            return combatGoldReduction;
        }

        @Override
        public void onLoad(Integer b) {
            combatGoldReduction = b == null ? 0 : b;
        }
    }
}