package spireMapOverhaul.zones.CosmicEukotranpha.patches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RegenPower;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.RegenReductionEffect;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class CosmicZoneRegenPowerPatch{@SpirePatch(clz=RegenPower.class,method="atEndOfTurn")
public static class thing{@SpirePostfixPatch public static void Postfix(RegenPower __this){CosmicZoneMod.logger.info("Patch: CosmicZoneRegenPowerPatch triggered");
	AbstractDungeon.actionManager.addToBottom(new RegenReductionEffect(__this.owner));}}}
//Todo: Change



/*
public class CosmicZoneRegenPowerPatch{@SpirePatch(clz=RegenAction.class,method="update")
public static class thing{@SpireInsertPatch(locator=Locator.class)
public static void patch(RegenAction __this){CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterStartOfTurnPatch triggered");
	if(!__this.target.isPlayer&&__this.target instanceof CosmicZoneMonster){AbstractPower p=__this.target.getPower("Regenerationx");
		if(p!=null){--p.amount;if(p.amount==0){__this.target.powers.remove(p);}else{p.updateDescription();}}}}
	private static class Locator extends SpireInsertLocator{public int[]Locate(CtBehavior ctMethodToPatch)throws Exception{
		Matcher.MethodCallMatcher fieldAccessMatcher=new Matcher.MethodCallMatcher(AbstractGameAction.class,"tickDuration");return LineFinder.findInOrder(ctMethodToPatch,(Matcher)fieldAccessMatcher);}}}}
}*/