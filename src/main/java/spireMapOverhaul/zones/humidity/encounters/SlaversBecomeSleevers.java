package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.events.city.Colosseum;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.exordium.SlaverBlue;
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.EarlyByrdMinionPower;
import spireMapOverhaul.zones.humidity.powers.ScrapOozePower;
import spireMapOverhaul.zones.humidity.powers.SleeverPower;

import java.util.Objects;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class SlaversBecomeSleevers {

    public static final String ID = makeID("Sleevers");
    public static final String BLUESLEEVER_NAME = CardCrawlGame.languagePack.getMonsterStrings(ID).DIALOG[0];
    public static final String REDSLEEVER_NAME = CardCrawlGame.languagePack.getMonsterStrings(ID).DIALOG[1];
    public static final String SLEEVER_GREETING = CardCrawlGame.languagePack.getMonsterStrings(ID).DIALOG[2];


    @SpirePatch2(clz=SlaverBlue.class,method=SpirePatch.CONSTRUCTOR)
    public static class BluePatch{
        @SpirePostfixPatch
        public static void Foo(SlaverBlue __instance){
            if(HumidityZone.isNotInZone())return;
            if(AbstractDungeon.id!=null && !AbstractDungeon.id.equals(Exordium.ID) && !colosseumInProgress())return;
            __instance.name=BLUESLEEVER_NAME;
            __instance.powers.add(new SleeverPower(__instance, 0));
        }
    }

    @SpirePatch2(clz=SlaverRed.class,method=SpirePatch.CONSTRUCTOR)
    public static class RedPatch{
        @SpirePostfixPatch
        public static void Foo(SlaverRed __instance){
            if(HumidityZone.isNotInZone())return;
            if(AbstractDungeon.id!=null && !AbstractDungeon.id.equals(Exordium.ID) && !colosseumInProgress())return;
            __instance.name=REDSLEEVER_NAME;
            __instance.powers.add(new SleeverPower(__instance, 0));
        }
    }

    @SpirePatch2(clz= AbstractMonster.class,method="usePreBattleAction")
    public static class SleeversGreeting{
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance){
            if(!(__instance instanceof SlaverBlue || __instance instanceof SlaverRed))return;
            if(HumidityZone.isNotInZone())return;
            if(AbstractDungeon.id!=null && !AbstractDungeon.id.equals(Exordium.ID) && !colosseumInProgress())return;
            if (Wiz.getEnemies().get(0) == __instance || Objects.equals(AbstractDungeon.id, Exordium.ID)) {
                Wiz.atb(new TalkAction(__instance, SLEEVER_GREETING));
            }
        }
    }

    public static boolean colosseumInProgress(){
        return Wiz.curRoom()!=null && Wiz.curRoom().event!=null && Wiz.curRoom().event instanceof Colosseum;
    }

}
