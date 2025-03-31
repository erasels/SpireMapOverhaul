package spireMapOverhaul.zones.humidity.encounters.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PaperFrog;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import spireMapOverhaul.patches.BestiaryIntegrationPatch;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.EarlyByrdMinionPower;
import spireMapOverhaul.zones.storm.patches.ElectricCardPatch;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class EarlyByrd {
    public static final String ID = makeID("EarlyByrd");
    public static final String EARLYBYRD_NAME = CardCrawlGame.languagePack.getMonsterStrings(ID).NAME;

    @SpirePatch2(clz=Byrd.class,method="usePreBattleAction")
    public static class PowerPatch{
        @SpirePostfixPatch
        public static void Foo(Byrd __instance){
            if(HumidityZone.isNotInZone())return;
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
                        n.replace("{$1 = " + doesJawWormExist() + " $1 : (" + AbstractMonster.class.getName() + ")" + Wiz.class.getName() + ".getEnemies().get(0); $_ = $proceed($$);}");
                    }
                }
            };
        }
        public static String doesJawWormExist() {
            return "(" + HumidityZone.class.getName() + ".isNotInZone() || " + Wiz.class.getName() + ".getEnemies().size()<1 || !(" + Wiz.class.getName() + ".getEnemies().get(0) instanceof " + JawWorm.class.getName() + ")) ?";
        }
    }

    @SpirePatch2(clz=DamageInfo.class, method="applyPowers")
    public static class ApplyDamageInfoPowersVsJawWorm{
        static AbstractStance actualStance;
        @SpirePrefixPatch
        public static void Foo(DamageInfo __instance, AbstractCreature owner, @ByRef AbstractCreature[] target){
            if(HumidityZone.isNotInZone())return;
            if(!AbstractDungeon.id.equals(Exordium.ID))return;
            if(!(owner instanceof Byrd))return;
            if(Wiz.getEnemies().size()<2)return;
            AbstractMonster jawWorm=Wiz.getEnemies().get(0);
            if(!(jawWorm instanceof JawWorm))return;
            target[0]=jawWorm;
            actualStance=Wiz.adp().stance;
            Wiz.adp().stance=new NeutralStance();
        }
        @SpirePostfixPatch
        public static void Bar(DamageInfo __instance, AbstractCreature owner){
            if(HumidityZone.isNotInZone())return;
            if(!AbstractDungeon.id.equals(Exordium.ID))return;
            if(!(owner instanceof Byrd))return;
            if(Wiz.getEnemies().size()<2)return;
            AbstractMonster jawWorm=Wiz.getEnemies().get(0);
            if(!(jawWorm instanceof JawWorm))return;
            Wiz.adp().stance=actualStance;
        }
    }
    @SpirePatch2(clz=AbstractMonster.class, method="applyPowers")
    public static class ApplyMonsterPowersVsJawWorm{
        @SpirePrefixPatch()
        public static SpireReturn<Void> Foo(AbstractMonster __instance){
            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
            if(!AbstractDungeon.id.equals(Exordium.ID))return SpireReturn.Continue();
            if(!(__instance instanceof Byrd))return SpireReturn.Continue();
            if(Wiz.getEnemies().size()<2)return SpireReturn.Continue();
            AbstractMonster jawWorm=Wiz.getEnemies().get(0);
            if(!(jawWorm instanceof JawWorm))return SpireReturn.Continue();
            for(DamageInfo dmg : __instance.damage) {
                dmg.applyPowers(__instance, jawWorm);
                int x=0;x+=1;
                if (true) {//always apply BackAttack
                    dmg.output = (int)((float)dmg.output * 1.5F);
                    int y=0;y+=1;
                }
            }
            EnemyMoveInfo move = ReflectionHacks.getPrivate(__instance,AbstractMonster.class,"move");
            if (move.baseDamage > -1) {
                ReflectionHacks.privateMethod(AbstractMonster.class,"calculateDamage",int.class).invoke(__instance,move.baseDamage);
            }
            ReflectionHacks.setPrivate(__instance,AbstractMonster.class,"intentImg",
                    ReflectionHacks.privateMethod(AbstractMonster.class, "getIntentImg").invoke(__instance));
            ReflectionHacks.privateMethod(AbstractMonster.class, "updateIntentTip").invoke(__instance);
            return SpireReturn.Return();
        }
    }


    @SpirePatch2(clz=AbstractMonster.class, method="calculateDamage")
    public static class CalculateDamageVsJawWorm{
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(AbstractMonster __instance, int dmg){
            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
            if(!AbstractDungeon.id.equals(Exordium.ID))return SpireReturn.Continue();
            if(!(__instance instanceof Byrd))return SpireReturn.Continue();
            if(Wiz.getEnemies().size()<2)return SpireReturn.Continue();
            AbstractMonster jawWorm=Wiz.getEnemies().get(0);
            if(!(jawWorm instanceof JawWorm))return SpireReturn.Continue();
            AbstractMonster target = jawWorm;
            float tmp = (float)dmg;
            if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
                float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
                tmp *= mod;
            }
            for(AbstractPower p : __instance.powers) {
                tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL);
            }
            for(AbstractPower p : target.powers) {
                tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);
            }
            //never apply player stance
            //tmp = AbstractDungeon.player.stance.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);
            //round down before applying BackAttack
            tmp = MathUtils.floor(tmp);
            if (true) { //always apply BackAttack
                tmp = (float)((int)(tmp * 1.5F));
            }
            for(AbstractPower p : __instance.powers) {
                tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL);
            }
            for(AbstractPower p : target.powers) {// 1329
                tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL);// 1330
            }
            dmg = MathUtils.floor(tmp);
            if (dmg < 0) {
                dmg = 0;
            }
            ReflectionHacks.setPrivate(__instance,AbstractMonster.class,"intentDmg",dmg);
            return SpireReturn.Return();
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
