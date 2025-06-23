package spireMapOverhaul.zones.humidity.encounters.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.city.Centurion;
import com.megacrit.cardcrawl.monsters.city.Taskmaster;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation.JoustManagerPower;

public class MonsterVsMonsterDamageCalculation {

    public static AbstractMonster HumidityMonsterTarget(AbstractCreature attacker){
        //Returns null if monster is not attacking another monster,
        //or a reference to the target monster if it is.
        if(HumidityZone.isNotInZone())return null;
        if(attacker instanceof Byrd){
            if(!AbstractDungeon.id.equals(Exordium.ID))return null;
            if(Wiz.getEnemies().size()<2)return null;
            AbstractMonster jawWorm=Wiz.getEnemies().get(0);
            if(!(jawWorm instanceof JawWorm))return null;
            return jawWorm;
        }
        if(JoustManagerPower.joustMonstersAreValid()){
            if(attacker instanceof Centurion){
                if(attacker==Wiz.getEnemies().get(0))
                    return Wiz.getEnemies().get(1);
                else
                    return Wiz.getEnemies().get(0);
            }
        }
        if(attacker instanceof GremlinNob){
            if(Wiz.getEnemies().size()<2)return null;
            AbstractMonster taskmaster=Wiz.getEnemies().get(0);
            if(!(taskmaster instanceof Taskmaster))return null;
            return taskmaster;
        }

        return null;
    }

    @SpirePatch2(clz= DamageInfo.class, method="applyPowers")
    public static class ApplyDamageInfoPowersVsMonster{
        static AbstractStance actualStance;
        @SpirePrefixPatch
        public static void Foo(DamageInfo __instance, AbstractCreature owner, @ByRef AbstractCreature[] target){
            AbstractCreature newTarget = HumidityMonsterTarget(owner);
            if(newTarget!=null) {
                target[0] = newTarget;
                actualStance = Wiz.adp().stance;
                Wiz.adp().stance = new NeutralStance();
            }
        }
        @SpirePostfixPatch
        public static void Bar(DamageInfo __instance, AbstractCreature owner){
            if(HumidityMonsterTarget(owner)==null)return;
            Wiz.adp().stance=actualStance;
        }
    }
    @SpirePatch2(clz=AbstractMonster.class, method="applyPowers")
    public static class ApplyMonsterPowersVsMonster{
        @SpirePrefixPatch()
        public static SpireReturn<Void> Foo(AbstractMonster __instance){
            AbstractMonster newTarget = HumidityMonsterTarget(__instance);
            if(newTarget==null)return SpireReturn.Continue();

            for(DamageInfo dmg : __instance.damage) {
                dmg.applyPowers(__instance, newTarget);
                if (newTarget.hasPower(SurroundedPower.POWER_ID)) {
                    dmg.output = (int)((float)dmg.output * 1.5F);
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
    public static class CalculateDamageVsMonster{
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(AbstractMonster __instance, int dmg){
            AbstractCreature newTarget = HumidityMonsterTarget(__instance);
            if(newTarget==null)return SpireReturn.Continue();
            float tmp = (float)dmg;
            if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
                float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
                tmp *= mod;
            }
            for(AbstractPower p : __instance.powers) {
                tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL);
            }
            for(AbstractPower p : newTarget.powers) {
                tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);
            }
            //never apply player stance
            //tmp = AbstractDungeon.player.stance.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);
            //round down before applying BackAttack
            tmp = MathUtils.floor(tmp);
            if (newTarget.hasPower(SurroundedPower.POWER_ID)) {
                tmp = (float)((int)(tmp * 1.5F));
            }
            for(AbstractPower p : __instance.powers) {
                tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL);
            }
            for(AbstractPower p : newTarget.powers) {// 1329
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
}
