package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.powers.FlightPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class ByrdsReducedFlight {
    @SpirePatch2(clz = Byrd.class, method = "usePreBattleAction")
    public static class FlightMinusOnePatch {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (n.getClassName().equals(FlightPower.class.getName())) {
                        n.replace("{$2 = " + Act2HumidityCheck() + " $2 : $2 - 1; $_ = $proceed($$);}");
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = Byrd.class, method = "takeTurn")
    public static class FlightMinusOnePatch2 {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (n.getClassName().equals(FlightPower.class.getName())) {
                        n.replace("{$2 = " + Act2HumidityCheck() + " $2 : $2 - 1; $_ = $proceed($$);}");
                    }
                }
            };
        }
    }

    public static String Act2HumidityCheck() {
        //skip the patch if we're not in humidity or we ARE in act 1
        return "(" + HumidityZone.class.getName() + ".isNotInZone() || " + Exordium.class.getName() + ".ID.equals(" + AbstractDungeon.class.getName() + ".id)) ? ";
    }
}