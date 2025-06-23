package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.unique.GainBlockRandomMonsterAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.util.Wiz;

public class BlockForAlliesOnlyPatch {
    @SpirePatch (clz = GainBlockRandomMonsterAction.class,method="update")
    public static class RandomBlockUpdatePatch{
        @SpireInsertPatch(locator = Locator.class)
        public static void Foo(GainBlockRandomMonsterAction __instance){
            if(!JoustManagerPower.joustMonstersAreValid())return;

            AbstractCreature blocker = __instance.source;

            if(blocker==Wiz.getEnemies().get(0)){
                //Left Centurion blocks for himself or the player
                if(AbstractDungeon.aiRng.random.nextBoolean()){
                    __instance.target=blocker;
                }else{
                    __instance.target=Wiz.adp();
                }
            }else if(blocker==Wiz.getEnemies().get(1)){
                //Right Centurion blocks for himself or the Mystic
                if(AbstractDungeon.aiRng.random.nextBoolean()){
                    __instance.target=blocker;
                }else{
                    __instance.target=Wiz.getEnemies().get(2);
                }
            }
        }
    }
    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "effectList");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}