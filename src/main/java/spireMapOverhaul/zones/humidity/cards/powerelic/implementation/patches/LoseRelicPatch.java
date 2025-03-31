package spireMapOverhaul.zones.humidity.cards.powerelic.implementation.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.Iterator;

public class LoseRelicPatch {
    //replace
    //      while(var3.hasNext())
    //with
    //      while(var3.hasNext() && toRemove == null)

    @SpirePatch2(clz = AbstractPlayer.class, method = "loseRelic")
    public static class Foo {
        @SpireInstrumentPatch
        public static ExprEditor Bar() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(Iterator.class.getName()) && m.getMethodName().equals("hasNext")) {
                        m.replace("$_ = $proceed($$) && toRemove == null;");
                    }
                }
            };
        }
    }
}
