package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.city.Healer;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class HealerAttacksOtherMonsterPatch {

    public static AbstractPlayer realPlayerTempStorage;

    @SpirePatch2(clz=Healer.class,method="takeTurn")
    public static class AttackTheCenturionPatch {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (n.getClassName().equals(DamageAction.class.getName())) {
                        n.replace("{$1 = " + enemyCenturionJoustCheck() + " $1 : (" + AbstractMonster.class.getName() + ")" + Wiz.class.getName() + ".getEnemies().get(0); $_ = $proceed($$);}");
                    }
                }
            };
        }
        public static String enemyCenturionJoustCheck() {
            return "!("+ HumidityZone.class.getName()+".isInZone() && "+JoustManagerPower.class.getName()+".joustMonstersAreValid()) ?";
        }
    }


}
