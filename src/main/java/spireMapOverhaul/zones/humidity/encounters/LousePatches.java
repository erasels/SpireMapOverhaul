package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.CurlUpPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.SplatPower;

import java.util.Objects;

public class LousePatches {

    @SpirePatch2(clz=LouseNormal.class,method="usePreBattleAction")
    public static class RedLousePatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(){
            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
            return SpireReturn.Return();
        }
    }

//    //this is an old patch that merely halves Curl Up instead of removing it
//    // we're keeping this here, commented-out, in case we decide to revert to it
//    @SpirePatch2(clz=CurlUpPower.class,method=SpirePatch.CONSTRUCTOR)
//    public static class RedLousePatch_OLD{
//        @SpirePostfixPatch
//        public static void Foo(AbstractPower __instance){
//            if(HumidityZone.isNotInZone())return;
//            if(!(__instance.owner instanceof LouseNormal))return;
//            __instance.amount=__instance.amount/2;
//            __instance.description = CurlUpPower.DESCRIPTIONS[0] + __instance.amount + CurlUpPower.DESCRIPTIONS[1];
//        }
//    }

    @SpirePatch2(clz= LouseDefensive.class,method="takeTurn")
    public static class GreenLousePatch {
        @SpirePostfixPatch
        public static void Foo(LouseDefensive __instance) {
            if(HumidityZone.isNotInZone())return;
            if(__instance.nextMove==4) {
                for (AbstractMonster m : Wiz.getEnemies()) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, __instance, new WeakPower(m, 2, true), 2));
                }
            }
        }
    }
}
