package spireMapOverhaul.zones.humidity.encounters;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.Taskmaster;
import com.megacrit.cardcrawl.monsters.exordium.SlaverBlue;
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.vfx.combat.EntangleEffect;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation.UpdateIntentAction;
import spireMapOverhaul.zones.humidity.powers.FakeEntangledPower;
import spireMapOverhaul.zones.humidity.powers.SwapTaskmasterEnemyOrderPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class TaskmasterGetsEntangled {

    public static final String ID = makeID("Sleevers");
    public static final String SNEEZE = CardCrawlGame.languagePack.getMonsterStrings(ID).DIALOG[4];

    @SpirePatch(clz = SlaverRed.class, method = SpirePatch.CLASS)
    public static class HumidityTaskmasterField {
        public static final SpireField<Boolean> isHumidity = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz = MonsterHelper.class,
            method = "getEncounter"
    )
    public static class RedSlaverActsFirst {
        @SpirePostfixPatch
        public static MonsterGroup Foo(MonsterGroup __result, String key) {
            if(HumidityZone.isNotInZone()) return __result;
            if(!key.equals(MonsterHelper.SLAVERS_ENC)) return __result;
            SlaverRed red = new SlaverRed(125.0F, -30.0F);
            HumidityTaskmasterField.isHumidity.set(red,true);
            //Red is shuffled to first in the group
            return new MonsterGroup(new AbstractMonster[]{red, new SlaverBlue(-385.0F, -15.0F), new Taskmaster(-133.0F, 0.0F)});
        }
    }

    @SpirePatch(clz=SlaverRed.class,method="getMove")
    public static class RedSlaverEntangleIntent{
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(SlaverRed __instance) {
            if(!HumidityTaskmasterField.isHumidity.get(__instance))return SpireReturn.Continue();
            boolean firstTurn = ReflectionHacks.getPrivate(__instance, SlaverRed.class, "firstTurn");
            if (firstTurn) {
                ReflectionHacks.setPrivate(__instance, SlaverRed.class, "firstTurn", false);
                __instance.setMove(ReflectionHacks.getPrivateStatic(SlaverRed.class, "ENTANGLE_NAME"), (byte) 2, AbstractMonster.Intent.STRONG_DEBUFF);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz=SlaverRed.class,method="takeTurn")
    public static class RedSlaverEntangleTurn{
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(SlaverRed __instance) {
            if(!HumidityTaskmasterField.isHumidity.get(__instance))return SpireReturn.Continue();
            if(__instance.nextMove!=2)return SpireReturn.Continue();
            ReflectionHacks.privateMethod(SlaverRed.class,"playSfx").invoke(__instance);
            Wiz.atb(new TalkAction(__instance,SNEEZE));
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(__instance, "Use Net"));
            for(AbstractMonster m : Wiz.getEnemies()) {
                if(m!=__instance) {
                    //if (__instance.hb != null && m.hb != null && !Settings.FAST_MODE) {
                    if (__instance.hb != null && m.hb != null) {
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EntangleEffect(__instance.hb.cX - 70.0F * Settings.scale, __instance.hb.cY + 10.0F * Settings.scale, m.hb.cX, m.hb.cY), 0.1F));
                    }
                    //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, __instance, new EntanglePower(m)));
                }
            }
            for(AbstractMonster m : Wiz.getEnemies()) {
                if(m!=__instance) {
                    Wiz.atb(new SetMoveAction(m,(byte)99,AbstractMonster.Intent.STUN));
                    Wiz.atb(new UpdateIntentAction(m));
                    Wiz.atb(new ApplyPowerAction(m,__instance,new FakeEntangledPower(m)));
                }
            }
            Wiz.atb(new ApplyPowerAction(Wiz.adp(),__instance,new SwapTaskmasterEnemyOrderPower(Wiz.adp())));
            ReflectionHacks.setPrivate(__instance,SlaverRed.class,"usedEntangle",true);

            AbstractDungeon.actionManager.addToBottom(new RollMoveAction(__instance));

            return SpireReturn.Return();
        }
    }

}
