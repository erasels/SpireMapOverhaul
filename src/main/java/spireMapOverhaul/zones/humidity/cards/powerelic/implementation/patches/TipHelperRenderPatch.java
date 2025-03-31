package spireMapOverhaul.zones.humidity.cards.powerelic.implementation.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.TipHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class TipHelperRenderPatch {
    //targeting
    //  sb.draw(tip.img, x + TEXT_OFFSET_X + gl.width + 5.0F * Settings.scale, y - 10.0F * Settings.scale, 32.0F * Settings.scale, 32.0F * Settings.scale);

    @SpirePatch2(clz = TipHelper.class, method = "renderPowerTips")
    public static class Foo {
        @SpireInstrumentPatch
        public static ExprEditor Bar() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    String settings = Settings.class.getName();
                    if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("draw")) {
                        if(m.getSignature().equals("(Lcom/badlogic/gdx/graphics/Texture;FFFF)V")){
                            m.replace("{ " +
                                    boolean.class.getName()+" isPowerelicTip="+TipHelperRelicDetection.class.getName()+".detectRelics($1);"+
                                    "if(!isPowerelicTip){" +
                                    "   $proceed($$);" +
                                    "}else{" +
                                    //sb.draw(tip.img, x + TEXT_OFFSET_X + gl.width + 5.0F * Settings.scale, y - 10.0F * Settings.scale, 32.0F * Settings.scale, 32.0F * Settings.scale);
                                    "   $proceed($1,$2-20F*"+settings+".scale,$3-16F*"+settings+".scale,$4*2F,$5*2F);" +
                                    "}"
                               + "}");
                        }
                    }
                }
            };
        }

    }

}
