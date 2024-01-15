package spireMapOverhaul.zones.ambushed.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import spireMapOverhaul.zones.ambushed.AmbushedUtil;

@SpirePatch(
        clz = MonsterGroup.class,
        method = "usePreBattleAction"
)
public class FlipLeftmostMonsterPatch {

        @SpirePrefixPatch
        public static void Prefix(MonsterGroup __instance) {
            if (AmbushedUtil.isInAmbushedZone()) {
                AbstractMonster leftmostMonster = null;
                float lowestDrawX = Float.MAX_VALUE;

                for (AbstractMonster m : __instance.monsters) {
                    if (m.drawX < lowestDrawX) {
                        lowestDrawX = m.drawX;
                        leftmostMonster = m;
                    }
                }

                if (leftmostMonster != null) {
                    leftmostMonster.flipHorizontal = true;
                }
            }
        }
    }