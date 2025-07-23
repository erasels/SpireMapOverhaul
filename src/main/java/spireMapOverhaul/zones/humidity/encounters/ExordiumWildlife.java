package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import javassist.CtBehavior;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class ExordiumWildlife {

    @SpirePatch(clz = MonsterHelper.class, method = "bottomWildlife")
    public static class MovePatch {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"monsters"}
        )
        public static void Foo(@ByRef AbstractMonster[][] monsters) {
            if (HumidityZone.isNotInZone()) return;
            if (monsters[0][0] instanceof JawWorm) {
                AbstractMonster[] replacementMonsters = new AbstractMonster[3];
                replacementMonsters[0] = new JawWorm(-460.0F, 25.0F);
                replacementMonsters[1] = monsters[0][1];
                replacementMonsters[2] = new Byrd(0.0F, 70.0F);
                monsters[0] = replacementMonsters;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.NewExprMatcher(MonsterGroup.class);
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }


}

