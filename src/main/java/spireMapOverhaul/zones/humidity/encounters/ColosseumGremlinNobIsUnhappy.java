package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Taskmaster;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.GremlinNobMinionPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class ColosseumGremlinNobIsUnhappy {

    @SpirePatch(
            clz = GremlinNob.class,
            method = SpirePatch.CLASS
    )
    public static class Fields {
        public static SpireField<Boolean> extraDialog = new SpireField<>(() -> false);
    }

    public static final String ID = makeID("MaltreatedGremlinNob");
    public static final String ELITE_NAME = CardCrawlGame.languagePack.getMonsterStrings(ID).NAME;
    public static final String ELITE_DIALOG = CardCrawlGame.languagePack.getMonsterStrings(ID).DIALOG[0];

    public static boolean taskmasterInRoom() {
        for (AbstractMonster m : Wiz.getEnemies()) {
            if (m instanceof Taskmaster) return true;
        }
        return false;
    }

    @SpirePatch2(clz = AbstractMonster.class, method = "usePreBattleAction")
    public static class PowerPatch {
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance) {
            if (!(__instance instanceof GremlinNob)) return;
            if (HumidityZone.isNotInZone()) return;
            if (!SlaversBecomeSleevers.colosseumInProgress()) return;
            //note that BackAttackPower is purely cosmetic as far as the game engine is concerned
            __instance.name = ELITE_NAME;
        }
    }

    @SpirePatch2(clz = GremlinNob.class, method = "takeTurn")
    public static class AttackTheTaskmasterPatch {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (n.getClassName().equals(DamageAction.class.getName())) {
                        n.replace("{$1 = " + doesTaskmasterNotExist() + " $1 : (" + AbstractMonster.class.getName() + ")" + Wiz.class.getName() + ".getEnemies().get(0); $_ = $proceed($$);}");
                    }
                }
            };
        }

        public static String doesTaskmasterNotExist() {
            return "(" + HumidityZone.class.getName() + ".isNotInZone() || " + Wiz.class.getName() + ".getEnemies().size()<1 || !(" + Wiz.class.getName() + ".getEnemies().get(0) instanceof " + Taskmaster.class.getName() + ")) ?";
        }
    }

    @SpirePatch2(clz = GremlinNob.class, method = "takeTurn")
    public static class TaskmasterExtraDialog {
        @SpirePostfixPatch
        public static void Foo(GremlinNob __instance) {
            if (HumidityZone.isNotInZone()) return;
            if (!Fields.extraDialog.get(__instance)) {
                Fields.extraDialog.set(__instance, true);
                AbstractMonster taskmaster = findTaskmaster();
                if (taskmaster != null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new BackAttackPower(__instance)));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new GremlinNobMinionPower(__instance, 0)));
                    if (taskmaster instanceof Taskmaster) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(taskmaster, taskmaster, new SurroundedPower(taskmaster)));
                    }
                    Wiz.atb(new TalkAction(taskmaster, ELITE_DIALOG, 1.0F, 3.0F));
                }
            }
        }
    }


    @SpirePatch2(clz = Taskmaster.class, method = "die")
    public static class WhenTaskmasterDies {
        @SpirePrefixPatch
        public static void Foo(AbstractMonster __instance) {
            if (HumidityZone.isNotInZone()) return;
            if (!(__instance instanceof Taskmaster)) return;
            AbstractMonster gremlinNob = findGremlinNob();
            if (gremlinNob == null) return;

            Wiz.att(new EscapeAction(gremlinNob));
        }
    }

    public static AbstractMonster findGremlinNob() {
        if (Wiz.getEnemies().size() < 2) return null;
        if (Wiz.getEnemies().get(1) instanceof GremlinNob) return Wiz.getEnemies().get(1);
        if (Wiz.getEnemies().size() < 3) return null;
        if (Wiz.getEnemies().get(2) instanceof GremlinNob) return Wiz.getEnemies().get(2);
        return null;
    }

    public static AbstractMonster findTaskmaster() {
        if (Wiz.getEnemies().size() < 1) return null;
        if (Wiz.getEnemies().get(0) instanceof Taskmaster) return Wiz.getEnemies().get(0);
        return null;
    }
}