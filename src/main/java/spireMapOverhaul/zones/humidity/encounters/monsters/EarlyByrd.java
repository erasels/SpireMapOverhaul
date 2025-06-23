package spireMapOverhaul.zones.humidity.encounters.monsters;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.EarlyByrdMinionPower;

import java.util.Objects;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class EarlyByrd {
    public static final String ID = makeID("EarlyByrd");
    public static final String EARLYBYRD_NAME = CardCrawlGame.languagePack.getMonsterStrings(ID).NAME;

    public static boolean jawWormInRoom(){
        for(AbstractMonster m : Wiz.getEnemies()){
            if(m instanceof JawWorm)return true;
        }
        return false;
    }

    @SpirePatch2(clz=Byrd.class,method="usePreBattleAction")
    public static class PowerPatch{
        @SpirePostfixPatch
        public static void Foo(Byrd __instance){
            if(HumidityZone.isNotInZone())return;
            if(!Objects.equals(AbstractDungeon.id, Exordium.ID))return;
            if(!jawWormInRoom())return;
            //note that BackAttackPower is purely cosmetic as far as the game engine is concerned
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new BackAttackPower(__instance)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new EarlyByrdMinionPower(__instance, 0)));
            __instance.name=EARLYBYRD_NAME;
        }
    }

    public static AbstractPlayer realPlayerTempStorage;

    @SpirePatch2(clz=Byrd.class,method="takeTurn")
    public static class AttackTheJawWormPatch {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (n.getClassName().equals(DamageAction.class.getName())) {
                        n.replace("{$1 = " + doesJawWormNotExist() + " $1 : (" + AbstractMonster.class.getName() + ")" + Wiz.class.getName() + ".getEnemies().get(0); $_ = $proceed($$);}");
                    }
                }
            };
        }
        public static String doesJawWormNotExist() {
            return "(" + HumidityZone.class.getName() + ".isNotInZone() || " + Wiz.class.getName() + ".getEnemies().size()<1 || !(" + Wiz.class.getName() + ".getEnemies().get(0) instanceof " + JawWorm.class.getName() + ")) ?";
        }
    }


    @SpirePatch2(clz = AbstractMonster.class,method="updateEscapeAnimation")
    public static class EarlyByrdNewEscapeAnimation{
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(AbstractMonster __instance){
            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
            if(!AbstractDungeon.id.equals(Exordium.ID))return SpireReturn.Continue();
            if(!(__instance instanceof Byrd))return SpireReturn.Continue();
            JawWorm jawWorm=CapturedEnemy.jawWorm.get(__instance);
            if(jawWorm==null) return SpireReturn.Continue();

            if (__instance.escapeTimer != 0.0F) {
                __instance.escapeTimer -= Gdx.graphics.getDeltaTime();
                float angle = (float) ((3.0F-__instance.escapeTimer)/2.0*Math.PI*2);
                final float JAW_WORM_START_X=Settings.WIDTH * 0.75F + -460.0F * Settings.xScale;
                final float JAW_WORM_START_Y=AbstractDungeon.floorY + 25.0F * Settings.yScale;
                final float BYRD_START_X=Settings.WIDTH * 0.75F + 0.0F * Settings.xScale;
                final float BYRD_START_Y=AbstractDungeon.floorY + 70.0F * Settings.yScale;
                if(__instance.escapeTimer>=1.5F){
                    __instance.drawX = (float) (JAW_WORM_START_X+Math.cos(angle)*460*Settings.scale);
                    __instance.drawY = (float) (BYRD_START_Y+Math.sin(angle)*90*Settings.scale);
                }else{
                    __instance.drawX+=Gdx.graphics.getDeltaTime() * 460.0F*3 * Settings.scale;
                    if(__instance.escapeTimer>0.1F) {
                        __instance.drawY+=Gdx.graphics.getDeltaTime() * 460.0F*5*(1.5f-__instance.escapeTimer) * Settings.scale;
                    }
                }
                __instance.flipHorizontal = __instance.escapeTimer<2.0F;
                if(__instance.escapeTimer<1.5F){
                    jawWorm.drawX=__instance.drawX;
                    jawWorm.drawY=__instance.drawY-0*Settings.scale;
                }
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
            clz=Byrd.class,
            method=SpirePatch.CLASS
    )
    public static class CapturedEnemy {
        public static SpireField<JawWorm> jawWorm = new SpireField<>(() -> null);
    }


}
