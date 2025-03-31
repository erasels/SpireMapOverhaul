package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.*;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.AcidicPower;
import spireMapOverhaul.zones.humidity.powers.ScrapOozePower;
import spireMapOverhaul.zones.humidity.powers.SpikyPower;
import spireMapOverhaul.zones.humidity.powers.SplatPower;

import java.util.Objects;

public class SlimesPatches {
    @SpirePatch2(clz=SpikeSlime_L.class,method=SpirePatch.CONSTRUCTOR,paramtypez={float.class,float.class,int.class,int.class})
    public static class PowerPatch{
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance){
            if(HumidityZone.isNotInZone())return;
            if(!AbstractDungeon.id.equals(Exordium.ID))return;
            __instance.powers.add(new ScrapOozePower(__instance, 25));
//            for(DamageInfo di : __instance.damage){
//                di.base=di.base/2;
//            }
        }
    }
    @SpirePatch2(clz=AcidSlime_L.class,method=SpirePatch.CONSTRUCTOR,paramtypez={float.class,float.class,int.class,int.class})
    public static class PowerPatch2{
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance){
            if(HumidityZone.isNotInZone())return;
            if(!AbstractDungeon.id.equals(Exordium.ID))return;
            __instance.powers.add(new ScrapOozePower(__instance, 25));
//            for(DamageInfo di : __instance.damage){
//                di.base=di.base/2;
//            }
        }
    }

    @SpirePatch2(clz=AcidSlime_M.class,method=SpirePatch.CONSTRUCTOR,paramtypez={float.class,float.class,int.class,int.class})
    public static class PowerPatch3{
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance){
            if(HumidityZone.isNotInZone())return;
            if(!Objects.equals(AbstractDungeon.id, Exordium.ID))return;
            __instance.powers.add(new AcidicPower(__instance, 1));
        }
    }

    @SpirePatch2(clz=SpikeSlime_M.class,method=SpirePatch.CONSTRUCTOR,paramtypez={float.class,float.class,int.class,int.class})
    public static class PowerPatch4{
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance){
            if(HumidityZone.isNotInZone())return;
            if(!Objects.equals(AbstractDungeon.id, Exordium.ID))return;
            __instance.powers.add(new SpikyPower(__instance, 1));
        }
    }

    @SpirePatch2(clz=AcidSlime_S.class,method=SpirePatch.CONSTRUCTOR)
    public static class PowerPatch5{
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance){
            if(HumidityZone.isNotInZone())return;
            if(!Objects.equals(AbstractDungeon.id, Exordium.ID))return;
            __instance.powers.add(new SplatPower(__instance, 0));
        }
    }

    @SpirePatch2(clz=SpikeSlime_S.class,method=SpirePatch.CONSTRUCTOR)
    public static class PowerPatch6{
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance){
            if(HumidityZone.isNotInZone())return;
            if(!Objects.equals(AbstractDungeon.id, Exordium.ID))return;
            __instance.powers.add(new SplatPower(__instance, 0));
        }
    }


}
