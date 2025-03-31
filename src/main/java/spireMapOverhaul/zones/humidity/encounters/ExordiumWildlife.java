package spireMapOverhaul.zones.humidity.encounters;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.RitualDaggerAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.relics.PaperFrog;
import javassist.CtBehavior;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.encounters.monsters.EarlyByrd;

public class ExordiumWildlife {

    @SpirePatch(clz = MonsterHelper.class, method = "bottomWildlife")
    public static class MovePatch {
        @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"monsters"}
        )
        public static void Foo(@ByRef AbstractMonster[][] monsters) {
            if(HumidityZone.isNotInZone())return;
            if(monsters[0][0] instanceof JawWorm){
                AbstractMonster[] replacementMonsters = new AbstractMonster[3];
                replacementMonsters[0]=new JawWorm(-460.0F, 25.0F);
                replacementMonsters[1]=monsters[0][1];
                replacementMonsters[2]=new Byrd(0.0F, 70.0F);
                monsters[0]=replacementMonsters;
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

