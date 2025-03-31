//package spireMapOverhaul.zones.humidity.monsters;
//
//import com.evacipated.cardcrawl.modthespire.lib.*;
//import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.monsters.exordium.Sentry;
//import spireMapOverhaul.util.Wiz;
//import spireMapOverhaul.zones.humidity.HumidityZone;
//import spireMapOverhaul.zones.humidity.powers.CustomExplosivePower;
//
//public class SentriesExplosiveDEPRECATED {
//
//    @SpirePatch(clz = Sentry.class, method = SpirePatch.CLASS)
//    public static class ExplosionFields {
//        public static final SpireField<Integer> turnCount = new SpireField<>(() -> 1);
//        public static final SpireField<Integer> timeLimit = new SpireField<>(() -> -1);
//    }
//
//    @SpirePatch2(clz = Sentry.class,method = "getMove")
//    public static class IntentPatch{
//        @SpirePrefixPatch
//        public static SpireReturn<Void> getMove(Sentry __instance) {
//            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
//            if(!ExplosionFields.turnCount.get(__instance).equals(ExplosionFields.timeLimit.get(__instance))) {
//                __instance.setMove((byte) 3, AbstractMonster.Intent.DEBUFF);
//            }else{
//                __instance.setMove((byte) 5, AbstractMonster.Intent.UNKNOWN);
//            }
//            return SpireReturn.Return();
//        }
//    }
//
//    @SpirePatch2(
//            clz = Sentry.class,
//            method = "usePreBattleAction"
//    )
//    public static class SetupTimerPatch {
//        @SpirePostfixPatch
//        public static void Foo(Sentry __instance) {
//            if (HumidityZone.isNotInZone()) return;
//            int index=Wiz.getEnemies().indexOf(__instance);
//            if(index<0)return;
//            int timeLimit=index*2+2;
//            if(index>=2)timeLimit=index+3;
//            ExplosionFields.timeLimit.set(__instance,timeLimit);
//            Wiz.atb(new ApplyPowerAction(__instance, __instance, new CustomExplosivePower(__instance, timeLimit)));
//        }
//    }
//
//
//
//    @SpirePatch2(
//            clz = Sentry.class,
//            method = "takeTurn"
//    )
//    public static class ExplodeTurnPatch {
//        @SpirePrefixPatch
//        public static void Foo(Sentry __instance) {
//            ExplosionFields.turnCount.set(__instance,ExplosionFields.turnCount.get(__instance)+1);
//            //the actual exploding process is in CustomExplosivePower.duringTurn
//        }
//    }
//}
