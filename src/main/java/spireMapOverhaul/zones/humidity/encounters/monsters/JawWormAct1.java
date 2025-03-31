package spireMapOverhaul.zones.humidity.encounters.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FlightPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class JawWormAct1 {

    @SpirePatch2(clz=JawWorm.class,method="usePreBattleAction")
    public static class PowerPatch{
        @SpirePostfixPatch
        public static void Foo(JawWorm __instance){
            if(HumidityZone.isNotInZone())return;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new SurroundedPower(__instance)));
        }
    }

    public static AbstractMonster findEarlyByrd(){
        if(Wiz.getEnemies().size()<2)return null;
        if(Wiz.getEnemies().get(1) instanceof Byrd)return Wiz.getEnemies().get(1);
        if(Wiz.getEnemies().size()<3)return null;
        if(Wiz.getEnemies().get(2) instanceof Byrd)return Wiz.getEnemies().get(2);
        return null;
    }

    @SpirePatch2(clz = JawWorm.class, method="die")
    public static class WhenJawWormDies {
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(AbstractMonster __instance){
            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
            if(!AbstractDungeon.id.equals(Exordium.ID))return SpireReturn.Continue();
            if(!(__instance instanceof JawWorm))return SpireReturn.Continue();
            AbstractMonster byrd = findEarlyByrd();
            if(byrd==null)return SpireReturn.Continue();

            if (__instance.currentHealth <= 0 && !__instance.isEscaping) {
                Wiz.att(new EscapeAction(__instance));
                if(!byrd.hasPower(FlightPower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(byrd, byrd, new FlightPower(byrd, 4)));
                    ReflectionHacks.privateMethod(AbstractCreature.class,"loadAnimation",String.class,String.class,float.class).invoke(byrd,"images/monsters/theCity/byrd/flying.atlas", "images/monsters/theCity/byrd/flying.json", 1.0F);
                    AnimationState.TrackEntry e = byrd.state.setAnimation(0, "idle_flap", true);
                }
                Wiz.att(new EscapeAction(byrd));
                EarlyByrd.CapturedEnemy.jawWorm.set(byrd,(JawWorm)__instance);
                CapturingEnemy.byrd.set(__instance,(Byrd)byrd);

                for (AbstractPower p : __instance.powers) {
                    p.onDeath();
                }

                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onMonsterDeath(__instance);
                }

                CardCrawlGame.sound.play("JAW_WORM_DEATH");
            }
            return SpireReturn.Return();
        }
    }

    @SpirePatch2(clz = AbstractMonster.class,method="updateEscapeAnimation")
    public static class JawWormCancelEscapeAnimation{
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(AbstractMonster __instance){
            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
            if(!AbstractDungeon.id.equals(Exordium.ID))return SpireReturn.Continue();
            if(!(__instance instanceof JawWorm))return SpireReturn.Continue();
            if(CapturingEnemy.byrd.get(__instance)==null) return SpireReturn.Continue();

            if (__instance.escapeTimer != 0.0F) {
                __instance.escapeTimer -= Gdx.graphics.getDeltaTime();
            }

            if (__instance.escapeTimer < 0.0F) {
                __instance.escaped = true;
                if (AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.getCurrRoom().isBattleOver && !AbstractDungeon.getCurrRoom().cannotLose) {
                    AbstractDungeon.getCurrRoom().endBattle();
                }
            }

            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz=JawWorm.class,
            method=SpirePatch.CLASS
    )
    public static class CapturingEnemy {
        public static SpireField<Byrd> byrd = new SpireField<>(() -> null);
    }

}
