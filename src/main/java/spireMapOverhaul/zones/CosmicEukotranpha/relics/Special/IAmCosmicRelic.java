package spireMapOverhaul.zones.CosmicEukotranpha.relics.Special;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.CuriosityPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.special.IAmCosmicCard;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.FascinationPower;
import spireMapOverhaul.zones.CosmicEukotranpha.relics.BaseRelic;

import static com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound.MAGICAL;
import static com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier.SPECIAL;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;
public class IAmCosmicRelic extends BaseRelic{public static String ID=SpireAnniversary6Mod.makeID(
				PinkRainRelic.class.getSimpleName());public
IAmCosmicRelic(){super(ID,"",SPECIAL,MAGICAL);}
//Combat start: Gain Regen to match 5 \[] Turn 6 start: Gain Fascination. Add 1 of 4 Cosmic cards to deck. Give foes 2 Curiosity. Add "I am Cosmic" to bottom of deck
public void atBattleStart(){if(getPowAmount(AbstractDungeon.player,"Regeneration")<5){poP(new RegenPower(AbstractDungeon.player,5-getPowAmount(AbstractDungeon.player,"Regeneration")));}}
public void atTurnStartPostDraw(){
	if(GameActionManager.turn==5){
		poP(new FascinationPower(AbstractDungeon.player,AbstractDungeon.player));
		//TODO: Card
		for(AbstractMonster mo:monsterList()){
			poT(mo,AbstractDungeon.player,new CuriosityPower(mo,2));
		}
		makeInDeck(new IAmCosmicCard(),1,2);
	}
}
}

