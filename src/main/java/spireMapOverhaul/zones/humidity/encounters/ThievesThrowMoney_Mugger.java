package spireMapOverhaul.zones.humidity.encounters;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.GilTossPower;

public class ThievesThrowMoney_Mugger {

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class GilTossField {
        public static final SpireField<Integer> tossAmt = new SpireField<>(() -> 0);
    }
    @SpirePatch2(clz= Mugger.class,method= SpirePatch.CONSTRUCTOR)
    public static class MuggerConstructor{
        @SpirePostfixPatch
        public static void Foo(Mugger __instance){
            if(HumidityZone.isNotInZone())return;
            GilTossField.tossAmt.set(__instance, ReflectionHacks.getPrivate(__instance,Mugger.class,"goldAmt"));
            ReflectionHacks.setPrivate(__instance,Mugger.class,"goldAmt",0);
        }
    }
    @SpirePatch2(clz=Mugger.class,method="usePreBattleAction")
    public static class MuggerGilTossPower{
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(Mugger __instance){
            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new GilTossPower(__instance, GilTossField.tossAmt.get(__instance))));
            return SpireReturn.Return();
        }
    }

    @SpirePatch2(clz=Mugger.class,method="takeTurn")
    public static class TurnReplacement{
        @SpirePrefixPatch
        public static SpireReturn<Void> takeTurn(Mugger __instance) {
            if(HumidityZone.isNotInZone())return SpireReturn.Continue();
            final int tossAmt = GilTossField.tossAmt.get(__instance);
            int slashCount = ReflectionHacks.getPrivate(__instance,Mugger.class,"slashCount");
            int escapeDef = ReflectionHacks.getPrivate(__instance,Mugger.class,"escapeDef");
            switch (__instance.nextMove) {
                case 1:
                    if (slashCount == 0 && AbstractDungeon.aiRng.randomBoolean(0.6F)) {
                        AbstractDungeon.actionManager.addToBottom(new TalkAction(__instance, SLASH_MSG1, 0.3F, 2.0F));
                    }

                    ReflectionHacks.privateMethod(Mugger.class,"playSfx").invoke(__instance);
                    AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(__instance));
                    tossGold(__instance,tossAmt);
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)__instance.damage.get(0)));
                    ++slashCount;
                    ReflectionHacks.setPrivate(__instance,Mugger.class,"slashCount",slashCount);
                    if (slashCount == 2) {
                        if (AbstractDungeon.aiRng.randomBoolean(0.5F)) {
                            __instance.setMove((byte)2, AbstractMonster.Intent.DEFEND);
                        } else {
                            AbstractDungeon.actionManager.addToBottom(new SetMoveAction(__instance, MOVES[0], (byte)4, AbstractMonster.Intent.ATTACK, ((DamageInfo)__instance.damage.get(1)).base));
                        }
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new SetMoveAction(__instance, MOVES[1], (byte)1, AbstractMonster.Intent.ATTACK, ((DamageInfo)__instance.damage.get(0)).base));
                    }
                    break;
                case 2:
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(__instance, __instance, escapeDef));
                    AbstractDungeon.actionManager.addToBottom(new SetMoveAction(__instance, (byte)3, AbstractMonster.Intent.ESCAPE));
                    break;
                case 3:
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(__instance, RUN_MSG, 0.3F, 2.5F));
                    AbstractDungeon.getCurrRoom().mugged = true;
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmokeBombEffect(__instance.hb.cX, __instance.hb.cY)));
                    AbstractDungeon.actionManager.addToBottom(new EscapeAction(__instance));
                    AbstractDungeon.actionManager.addToBottom(new SetMoveAction(__instance, (byte)3, AbstractMonster.Intent.ESCAPE));
                    break;
                case 4:
                    ReflectionHacks.privateMethod(Mugger.class,"playSfx").invoke(__instance);
                    ++slashCount;
                    ReflectionHacks.setPrivate(__instance,Mugger.class,"slashCount",slashCount);
                    AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(__instance));
                    tossGold(__instance,tossAmt);
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)__instance.damage.get(1)));
                    __instance.setMove((byte)2, AbstractMonster.Intent.DEFEND);
            }
            return SpireReturn.Return();
        }

    }


    public static void tossGold(Mugger __instance,int tossAmt) {
        AbstractDungeon.player.gainGold(tossAmt);

        for(int i = 0; i < tossAmt; ++i) {
            AbstractDungeon.effectList.add(new GainPennyEffect(Wiz.adp(), __instance.hb.cX, __instance.hb.cY, Wiz.adp().hb.cX, Wiz.adp().hb.cY, true));
        }
    }

    private static MonsterStrings monsterStrings;
    public static String NAME;
    public static String[] MOVES;
    public static String[] DIALOG;
    private static final String SLASH_MSG1;
    private static final String RUN_MSG;
    static{
        String ID = SpireAnniversary6Mod.makeID("Mugger");
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
        SLASH_MSG1 = DIALOG[0];
        RUN_MSG = DIALOG[1];
    }


}
