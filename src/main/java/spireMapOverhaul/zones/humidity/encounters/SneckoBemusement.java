package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.monsters.city.Snecko;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.BemusementPower;

public class SneckoBemusement {
    @SpirePatch2(clz= Snecko.class,method="takeTurn")
    public static class AttackTheCenturionPatch {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (n.getClassName().equals(ConfusionPower.class.getName())) {
                        n.replace("{ $_ = "+HumidityZone.instrumentTerniary()+" $proceed($1) : new "+ BemusementPower.class.getName() +"($1); }");
                    }
                }
            };
        }
    }

}
