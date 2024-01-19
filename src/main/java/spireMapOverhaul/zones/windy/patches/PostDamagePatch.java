package spireMapOverhaul.zones.windy.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.zones.windy.WindyZone;

public class PostDamagePatch {
    @SpirePatch2(clz = AbstractMonster.class, method = "damage")
    public static class OnAttackedMonsterHook {
        @SpirePostfixPatch
        public static void patch(AbstractMonster __instance, DamageInfo info) {
            if(ZonePatches.currentZone() instanceof WindyZone){
                ((WindyZone)ZonePatches.currentZone()).postDamageCheck(__instance);
            }
        }
    }
}
