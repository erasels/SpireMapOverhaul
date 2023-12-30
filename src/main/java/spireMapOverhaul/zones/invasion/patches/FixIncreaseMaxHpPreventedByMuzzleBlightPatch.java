package spireMapOverhaul.zones.invasion.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.blights.Muzzle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = AbstractCreature.class, method = "increaseMaxHp", paramtypez = { int.class, boolean.class })
public class FixIncreaseMaxHpPreventedByMuzzleBlightPatch {
    public static class FixIncreaseMaxHpPreventedByMuzzleBlightExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(AbstractPlayer.class.getName()) && methodCall.getMethodName().equals("hasBlight")) {
                methodCall.replace(String.format("{ $_ = $proceed($$) && $1.equals(%1$s.ID) && this.isPlayer; }", Muzzle.class.getName()));
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor fixIncreaseMaxHpPreventedByMuzzleBlight() {
        return new FixIncreaseMaxHpPreventedByMuzzleBlightExprEditor();
    }
}
