package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SomewhatGeneric;import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.green.Defend_Green;
import com.megacrit.cardcrawl.cards.green.Neutralize;
import com.megacrit.cardcrawl.cards.green.Unload;
import com.megacrit.cardcrawl.cards.purple.Brilliance;
import com.megacrit.cardcrawl.cards.purple.Vigilance;
import com.megacrit.cardcrawl.cards.purple.Wallop;
import com.megacrit.cardcrawl.cards.red.Bash;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicZoneGameActionHistory;public class
AddOldestInHoroscopeToDeckEffect extends CosmicZoneAbstractGameAction{public CosmicZoneAbstractGameAction makeCopy(){return new
				AddOldestInHoroscopeToDeckEffect();}public
AddOldestInHoroscopeToDeckEffect(){}
public void update(){att(new MakeTempCardInDrawPileAction(CosmicZoneGameActionHistory.horoscope.getTopCard().makeCopy(),1,true,false));
	CosmicZoneGameActionHistory.horoscope.clear();CosmicZoneGameActionHistory.horoscope.addToBottom(new Bash());CosmicZoneGameActionHistory.horoscope.addToBottom(new Brilliance());
	CosmicZoneGameActionHistory.horoscope.addToBottom(new Wallop());CosmicZoneGameActionHistory.horoscope.addToBottom(new Unload());this.isDone=true;}}
