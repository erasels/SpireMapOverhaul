package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Healer;
import com.megacrit.cardcrawl.powers.FrailPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.NewExpr;
import spireMapOverhaul.util.Wiz;

public class BuffForAlliesOnlyPatch {

    //replace
    // if (!m.isDying && !m.isEscaping) {
    //with
    // if (!m.isDying && !m.isEscaping && (...)) {
    //which translates to
    // if (!m.isDying && !(m.isEscaping || !(...))){


    @SpirePatch(clz = Healer.class,method="takeTurn")
    public static class BuffAlliesOnlyPatch {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().equals(AbstractMonster.class.getName()) && f.getFieldName().equals("isEscaping")) {
                        f.replace("{$_ = $proceed($$) || !"+BuffForAlliesOnlyPatch.class.getName()+".validMonsterCheck(m);}");
                    }
                }
            };
        }
    }
    @SpirePatch(clz = Healer.class,method="takeTurn")
    public static class HealReductionPatch {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (n.getClassName().equals(HealAction.class.getName())) {
                        final float HealReductionMultiplier=1.0f;
                        n.replace("{$3 = !"+BuffForAlliesOnlyPatch.class.getName()+".validMonsterCheck(m) ? $3 : (int)($3 / "+HealReductionMultiplier+"f); $_ = $proceed($$);}");
                    }
                }
            };
        }
    }
    @SpirePatch(clz = Healer.class,method="takeTurn")
    public static class FrailRedirectionPatch {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (n.getClassName().equals(ApplyPowerAction.class.getName())) {
                        final float HealReductionMultiplier=1.0f;
                        n.replace("{if("+JoustManagerPower.class.getName()+".joustMonstersAreValid() && $3 instanceof "+ FrailPower.class.getName()+")"
                                +"{$1 = "+CenturionAttacksOppositeCenturionPatch.class.getName()+".findEnemyCenturion(this); $3 = new "
                                +FrailPower.class.getName()+"("+CenturionAttacksOppositeCenturionPatch.class.getName()+".findEnemyCenturion(this),2,true);} $_ = $proceed($$);}");
                    }
                }
            };
        }
    }
    public static boolean validMonsterCheck(AbstractMonster m){
        //if a joust is not active, all monsters are valid
        if(!JoustManagerPower.joustMonstersAreValid())return true;
        //if a joust is active, ignore left centurion
        if(Wiz.getEnemies().get(0) == m)return false;
        return true;
    }
}
