package spireMapOverhaul.zones.CosmicEukotranpha.patches;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnDrawPileShufflePower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
public class CosmicZoneOnShuffleDeckPatch{public CosmicZoneOnShuffleDeckPatch(){}
    @SpirePatch(clz=ShuffleAllAction.class,method="<ctor>")
    public static class ShufflePatchThree{public ShufflePatchThree(){}
        public static void Postfix(ShuffleAllAction __instance){
            for(AbstractMonster mo:AbstractDungeon.getMonsters().monsters){for(AbstractPower po:mo.powers){if(po instanceof OnDrawPileShufflePower&&po instanceof BasePower){((OnDrawPileShufflePower)po).onShuffle();}}}
        }}
    @SpirePatch(clz=ShuffleAction.class,method="update")
    public static class ShufflePatchTwo{public ShufflePatchTwo(){}
        public static void Postfix(ShuffleAction __instance){boolean b=(Boolean)ReflectionHacks.getPrivate(__instance,ShuffleAction.class,"triggerRelics");
            if(b){for(AbstractMonster mo:AbstractDungeon.getMonsters().monsters){for(AbstractPower po:mo.powers){if(po instanceof OnDrawPileShufflePower&&po instanceof BasePower){((OnDrawPileShufflePower)po).onShuffle();}}}}
        }}
    @SpirePatch(clz=EmptyDeckShuffleAction.class,method="<ctor>")
    public static class ShufflePatchOne{public ShufflePatchOne(){}
        public static void Postfix(EmptyDeckShuffleAction __instance){
            for(AbstractMonster mo:AbstractDungeon.getMonsters().monsters){for(AbstractPower po:mo.powers){if(po instanceof OnDrawPileShufflePower&&po instanceof BasePower){((OnDrawPileShufflePower)po).onShuffle();}}}
        }}}








/*
public class CosmicZoneOnShuffleDeckPatch{@SpirePatch(clz=ShuffleAllAction.class,method="<ctor>")
public static class thing{@SpireInsertPatch(locator=Locator.class)
public static void patch(GameActionManager __this){CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterStartOfTurnPatch triggered");
    for(AbstractMonster mo:AbstractDungeon.getMonsters().monsters){if(mo instanceof CosmicZoneMonster){((CosmicZoneMonster)mo).startOfTurnIntentCheck();}}}
    private static class Locator extends SpireInsertLocator{public int[]Locate(CtBehavior ctMethodToPatch)throws Exception{
        Matcher.MethodCallMatcher fieldAccessMatcher=new Matcher.MethodCallMatcher(AbstractPlayer.class,"applyStartOfTurnRelics");return LineFinder.findInOrder(ctMethodToPatch,(Matcher)fieldAccessMatcher);}}}}
*/