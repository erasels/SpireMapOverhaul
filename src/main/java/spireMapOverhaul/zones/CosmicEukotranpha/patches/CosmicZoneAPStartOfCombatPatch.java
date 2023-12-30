package spireMapOverhaul.zones.CosmicEukotranpha.patches;import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.blue.Strike_Blue;
import com.megacrit.cardcrawl.cards.green.Defend_Green;
import com.megacrit.cardcrawl.cards.green.Neutralize;
import com.megacrit.cardcrawl.cards.green.Strike_Green;
import com.megacrit.cardcrawl.cards.purple.Strike_Purple;
import com.megacrit.cardcrawl.cards.purple.Vigilance;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicZoneGameActionHistory;
@SpirePatch(clz=AbstractPlayer.class,method="applyStartOfCombatLogic")public class CosmicZoneAPStartOfCombatPatch{
@SpirePostfixPatch public static void Postfix(AbstractPlayer __this){
	CosmicZoneMod.logger.info("Trigger Patch: CosmicZoneAPStartOfCombatPatch triggered");
	//In Cosmic Zone or have Cosmic card
	CosmicZoneGameActionHistory.horoscope.clear();
	CosmicZoneGameActionHistory.horoscope.addToBottom(new Strike_Red());
	CosmicZoneGameActionHistory.horoscope.addToBottom(new Defend_Green());
	CosmicZoneGameActionHistory.horoscope.addToBottom(new Neutralize());
	CosmicZoneGameActionHistory.horoscope.addToBottom(new Vigilance());
	CosmicZoneGameActionHistory.horoscopeDoThings=1;
}}