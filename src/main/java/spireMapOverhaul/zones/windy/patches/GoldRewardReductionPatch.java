package spireMapOverhaul.zones.windy.patches;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.zones.windy.WindyZone;

public class GoldRewardReductionPatch {
    public static int combatGoldReduction = 0;

    @SpirePatch2(clz = AbstractRoom.class, method = "addGoldToRewards")
    public static class GoldReductionPatch {
        @SpirePrefixPatch
        public static void patch(AbstractRoom __instance, @ByRef int[] gold) {
            gold[0] = Math.max(0, gold[0]-combatGoldReduction);
            combatGoldReduction = 0;
        }
    }

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
