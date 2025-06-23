package spireMapOverhaul.zones.humidity.encounters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.purple.Blasphemy;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Chosen;
import com.megacrit.cardcrawl.powers.HexPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.FakeDivinityStancePower;

public class ChosenDivinityStance {

    @SpirePatch(clz=Chosen.class,method=SpirePatch.CLASS)
    public static class HumidityBehavior {
        public static SpireField<Boolean> enabled = new SpireField<>(() -> false);
    }

    @SpirePatch2(clz=Chosen.class,method=SpirePatch.CONSTRUCTOR,paramtypez={float.class,float.class})
    public static class FlagAsHumidity{
        @SpirePostfixPatch
        public static void Foo(Chosen __instance){
            if(HumidityZone.isNotInZone()) return;
            HumidityBehavior.enabled.set(__instance,true);
        }
    }

    @SpirePatch2(clz=Chosen.class,method="getMove")
    public static class GetMovePatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(Chosen __instance) {
            if(HumidityZone.isNotInZone()) return SpireReturn.Continue();
            if(!HumidityBehavior.enabled.get(__instance)) return SpireReturn.Continue();
            //Chosen's moves aren't random during Humidity, so we only need to call getMove on turn 1
            if (AbstractDungeon.ascensionLevel >= 17) {
                __instance.setMove((byte)4, AbstractMonster.Intent.STRONG_DEBUFF);
            }else{
                __instance.setMove((byte)5, AbstractMonster.Intent.ATTACK, ((DamageInfo)__instance.damage.get(2)).base, 2, true);
            }
            return SpireReturn.Return();
        }
    }

    @SpirePatch2(clz=Chosen.class,method="takeTurn")
    public static class TakeTurnPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(Chosen __instance) {
            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
            if(!HumidityBehavior.enabled.get(__instance)) return SpireReturn.Continue();
            if(__instance.nextMove==5){
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(__instance));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)__instance.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)__instance.damage.get(2), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                __instance.setMove((byte)4, AbstractMonster.Intent.STRONG_DEBUFF);
            }else if(__instance.nextMove==4) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(__instance, Chosen.DIALOG[0]));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(__instance, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, __instance, new HexPower(AbstractDungeon.player, 1)));
                __instance.setMove((byte)3, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)__instance.damage.get(1)).base);
            }else if(__instance.nextMove==3) {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(__instance));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)__instance.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, __instance, new VulnerablePower(AbstractDungeon.player, 2, true), 2));
                __instance.setMove(new Blasphemy().name, (byte)6, AbstractMonster.Intent.BUFF);
            }else if(__instance.nextMove==6) {
                CardCrawlGame.sound.play("STANCE_ENTER_DIVINITY");
                AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.PINK, true));
                AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(__instance.hb.cX, __instance.hb.cY, "Divinity"));
                Wiz.atb(new ApplyPowerAction(__instance, __instance, new FakeDivinityStancePower(__instance)));
                Wiz.atb(new ApplyPowerAction(__instance, __instance, new EndTurnDeathPower(__instance)));
                __instance.setMove((byte)1, AbstractMonster.Intent.ATTACK, ((DamageInfo)__instance.damage.get(0)).base);
            }else if(__instance.nextMove==1) {
                //note that Chosen should be dead by now.
                //if something else is keeping Chosen alive, return to normal behavior
                //  (player still gets hit hard this turn)
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(__instance, 0.3F, 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, __instance.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                Wiz.atb(new RemoveSpecificPowerAction(__instance, __instance, FakeDivinityStancePower.POWER_ID));
                ReflectionHacks.setPrivate(__instance,Chosen.class,"firstTurn",false);
                ReflectionHacks.setPrivate(__instance,Chosen.class,"usedHex",true);
                HumidityBehavior.enabled.set(__instance,false);
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(__instance));
            }
            return SpireReturn.Return();
        }
    }
}
