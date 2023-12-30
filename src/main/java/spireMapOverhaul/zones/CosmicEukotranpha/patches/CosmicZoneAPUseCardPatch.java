package spireMapOverhaul.zones.CosmicEukotranpha.patches;import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Defend_Blue;
import com.megacrit.cardcrawl.cards.blue.Strike_Blue;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicZoneGameActionHistory;
@SpirePatch(clz=AbstractPlayer.class,method="useCard")public class CosmicZoneAPUseCardPatch{
@SpirePostfixPatch public static void Postfix(AbstractPlayer __this,AbstractCard c,AbstractMonster monster,int energyOnUse){
	CosmicZoneMod.logger.info("Trigger Patch: CosmicZoneAPUseCardPatch triggered");
	if(AbstractDungeon.cardRandomRng.random(3)<2){
		CosmicZoneGameActionHistory.horoscope.addToBottom(new Strike_Red());
	}else{
		CosmicZoneGameActionHistory.horoscope.addToBottom(new Defend_Blue());
	}
	CosmicZoneGameActionHistory.horoscope.removeTopCard();
}}