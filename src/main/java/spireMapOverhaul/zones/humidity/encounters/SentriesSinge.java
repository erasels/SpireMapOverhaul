package spireMapOverhaul.zones.humidity.monsters;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.cards.Singe;

public class SentriesSinge {

    @SpirePatch2(clz = Sentry.class,method = "getMove")
    public static class IntentPatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> getMove(Sentry __instance) {
            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
            __instance.setMove((byte) 3, AbstractMonster.Intent.DEBUFF);
            return SpireReturn.Return();
        }
    }

    //change
    //AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), this.dazedAmt));
    //to
    //AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Singe(), this.dazedAmt));
    @SpirePatch2(
            clz = Sentry.class,
            method = "takeTurn"
    )
    public static class BurnsPatch {
        @SpireInstrumentPatch
        public static ExprEditor Foo() {
            return new ExprEditor() {
                public void edit(NewExpr n) throws CannotCompileException {
                    if(n.getClassName().equals(MakeTempCardInDiscardAction.class.getName())) {
                        n.replace("{ $_ = $proceed("+HumidityZone.instrumentTerniary()+"$1 : new "+ Singe.class.getName() +"(), $2); }");
                    }
                }
            };
        }
    }
}
