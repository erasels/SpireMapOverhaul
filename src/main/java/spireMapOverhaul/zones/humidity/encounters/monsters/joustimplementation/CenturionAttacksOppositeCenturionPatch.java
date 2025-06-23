package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Centurion;
import com.megacrit.cardcrawl.monsters.city.Healer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class CenturionAttacksOppositeCenturionPatch {

    @SpirePatch(clz = Centurion.class,method = SpirePatch.CLASS)
    public static class Fields {
        public static SpireField<Boolean> isSolo = new SpireField<>(()->false);
    }

    public static AbstractPlayer realPlayerTempStorage;

    @SpirePatch2(clz= Centurion.class,method="takeTurn")
    public static class AttackTheCenturionPatch {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (n.getClassName().equals(DamageAction.class.getName())) {
                        n.replace("{$1 = " + enemyCenturionJoustCheck() + " $1 : " + CenturionAttacksOppositeCenturionPatch.class.getName()+".findEnemyCenturion(this) ; $_ = $proceed($$);}");
                    }
                }
            };
        }
        public static String enemyCenturionJoustCheck() {
            return "!("+ HumidityZone.class.getName()+".isInZone() && "+JoustManagerPower.class.getName()+".joustMonstersAreValid()) ?";
        }
    }
    public static AbstractMonster findEnemyCenturion(AbstractMonster __instance) {
        if(__instance==Wiz.getEnemies().get(0))
            return Wiz.getEnemies().get(1);
        else
            return Wiz.getEnemies().get(0);
//   (" + AbstractMonster.class.getName() + ")" + Wiz.class.getName() + ".getEnemies().get(0)
    }

    @SpirePatch2(clz=Centurion.class,method="getMove")
    public static class LeftCenturionIsSoloPatch {
        @SpireInsertPatch(locs={138,160},localvars={"aliveCount"})
        public static void Foo(Centurion __instance,@ByRef int[] aliveCount){
            if(Fields.isSolo.get(__instance))
                aliveCount[0]=1;
        }
    }
}
