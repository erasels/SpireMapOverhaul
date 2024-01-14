package spireMapOverhaul.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SuppressWarnings("unused")
@SpirePatch(
        clz = AbstractDungeon.class,
        method = "getRewardCards"
)
public class AnyColorCardRewardPatch {
    public static boolean rewardsShouldBeAnyColor = false;

    @SpireInstrumentPatch
    public static ExprEditor addCheckForAnyColor() {
        return new ExprEditor() {
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getMethodName().equals("hasRelic"))
                    m.replace("{ $_ = $proceed($$) || " + AnyColorCardRewardPatch.class.getName() + ".rewardsShouldBeAnyColor; }");
            }
        };
    }
}
