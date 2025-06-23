package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.MinionPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import spireMapOverhaul.patches.FontCreationPatches;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.GremlinLavosCorePower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class GremlinLeaderIsMinion {

    public static final String ID = makeID("GremlinLavos");
    public static final String NAME = CardCrawlGame.languagePack.getMonsterStrings(ID).NAME;

    public static boolean HumidityGremlinLeaderCheck(){
        if(HumidityZone.isNotInZone())return false;
        //can't check for a GremlinLeader at this point because room's monstergroup has not been initialized yet.
        //just check current act instead.
        return (AbstractDungeon.id.equals(TheCity.ID));
    }

    @SpirePatch2(clz=GremlinLeader.class,method=SpirePatch.CONSTRUCTOR)
    public static class RenameLeader{
        @SpirePostfixPatch
        public static void Foo(GremlinLeader __instance){
            if(!HumidityGremlinLeaderCheck())return;
            __instance.name=NAME;
        }
    }
    @SpirePatch2(clz= GremlinFat.class,method=SpirePatch.CONSTRUCTOR)
    public static class RenameFat{
        @SpirePostfixPatch public static void Foo(GremlinFat __instance){if(!HumidityGremlinLeaderCheck())return;__instance.name=NAME;}
    }
    @SpirePatch2(clz= GremlinThief.class,method=SpirePatch.CONSTRUCTOR)
    public static class RenameThief{
        @SpirePostfixPatch public static void Foo(GremlinThief __instance){if(!HumidityGremlinLeaderCheck())return;__instance.name=NAME;}
    }
    @SpirePatch2(clz= GremlinTsundere.class,method=SpirePatch.CONSTRUCTOR)
    public static class RenameTsundere{
        @SpirePostfixPatch public static void Foo(GremlinTsundere __instance){if(!HumidityGremlinLeaderCheck())return;__instance.name=NAME;}
    }
    @SpirePatch2(clz= GremlinWarrior.class,method=SpirePatch.CONSTRUCTOR)
    public static class RenameWarrior{
        @SpirePostfixPatch public static void Foo(GremlinWarrior __instance){if(!HumidityGremlinLeaderCheck())return;__instance.name=NAME;}
    }
    @SpirePatch2(clz= GremlinWizard.class,method=SpirePatch.CONSTRUCTOR)
    public static class RenameWizard{
        @SpirePostfixPatch public static void Foo(GremlinWizard __instance){if(!HumidityGremlinLeaderCheck())return;__instance.name=NAME;}
    }

    @SpirePatch(
            clz = GremlinLeader.class,
            method = "usePreBattleAction"
    )
    public static class ReassignMinionStatus1 {
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(GremlinLeader __instance) {
            if(HumidityZone.isNotInZone()) return SpireReturn.Continue();

            __instance.gremlins[0] = (AbstractMonster)AbstractDungeon.getMonsters().monsters.get(0);
            __instance.gremlins[1] = (AbstractMonster)AbstractDungeon.getMonsters().monsters.get(1);
            __instance.gremlins[2] = null;

            for(AbstractMonster m : __instance.gremlins) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new GremlinLavosCorePower(__instance)));
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new MinionPower(__instance)));

            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz = SummonGremlinAction.class,
            method = "update"
    )
    public static class ReassignMinionStatus2 {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (n.getClassName().equals(MinionPower.class.getName())) {
                        n.replace("{$_ = " + HumidityZone.instrumentTerniary() + " $proceed($$) : new "+GremlinLavosCorePower.class.getName()+"($$);}");
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = GremlinLeader.class,method="die")
    public static class GremlinLeaderDiesNormally {
        //Break out after calling super.die() so the summoned gremlins don't flee
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> Foo() {
            if(HumidityZone.isNotInZone()) return SpireReturn.Continue();
            return SpireReturn.Return();
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

}
