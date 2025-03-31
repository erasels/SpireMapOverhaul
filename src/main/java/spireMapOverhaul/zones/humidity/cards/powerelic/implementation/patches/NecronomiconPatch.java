package spireMapOverhaul.zones.humidity.cards.powerelic.implementation.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Necronomicon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class NecronomiconPatch {

    //replace
    //      AbstractDungeon.effectList.add(new ShowCardAndObtainEffect((AbstractCard)new Necronomicurse(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    //with
    //      AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect((AbstractCard)new Necronomicurse(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));

    @SpirePatch2(clz = Necronomicon.class, method = "onEquip")
    public static class Foo {
        @SpireInstrumentPatch
        public static ExprEditor Bar() {
            return new ExprEditor() {
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractDungeon.class.getName()) && m.getFieldName().equals("effectList")) {
                        m.replace("$_ = "+AbstractDungeon.class.getName()+".effectsQueue;");
                    }
                }
            };
        }
    }
}
