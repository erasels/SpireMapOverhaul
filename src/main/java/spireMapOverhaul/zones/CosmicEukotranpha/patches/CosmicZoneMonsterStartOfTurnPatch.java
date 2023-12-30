
/*

package spireMapOverhaul.zones.CosmicEukotranpha.patches;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.CosmicZoneMonster;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAndEnableControlsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import java.util.Collections;
import java.util.function.Consumer;
public class CosmicZoneMonsterStartOfTurnPatch{@SpirePatch(clz=GameActionManager.class,method="getNextAction")
    public static class thing{@SpireInsertPatch(locator=Locator.class)
        public static void patch(GameActionManager __this){CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterStartOfTurnPatch triggered");
            for(AbstractMonster mo:AbstractDungeon.getMonsters().monsters){if(mo instanceof CosmicZoneMonster){((CosmicZoneMonster)mo).startOfTurnIntentCheck();}}}
        private static class Locator extends SpireInsertLocator{public int[]Locate(CtBehavior ctMethodToPatch)throws Exception{
                Matcher.MethodCallMatcher fieldAccessMatcher=new Matcher.MethodCallMatcher(AbstractPlayer.class,"applyStartOfTurnRelics");return LineFinder.findInOrder(ctMethodToPatch,(Matcher)fieldAccessMatcher);}}}}


*/
