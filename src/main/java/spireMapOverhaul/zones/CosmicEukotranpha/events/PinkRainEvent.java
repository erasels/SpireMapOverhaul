package spireMapOverhaul.zones.CosmicEukotranpha.events;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods.ShuffleFirstTurnEndsInDPMod;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.addModT;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class PinkRainEvent extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				PinkRainEvent.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
				"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
public PinkRainEvent(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";optionList.clear();goi.clear();
	optionList.add(new CZMEOption("Cosmic","This event does nothing right now. [I am Cosmic] Add I am Cosmic to deck","Chose Cosmic",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Pink","[I am Pink Rain] Gain Pink Rain. Lose your starting relic (If you have it)","Chose Pink",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Dormant","[I am Dormant] Gain Mark of the Dormant","Chose Dormant",new String[]{"Exit"},false,null,null,
					"End",0,0,1,"","",true){});
	optionList.add(new CZMEOption("Exit","[Exit] Open Map","Bye!",null,false,null,null,
					"End",0,0,0,"","",true){});
	sFOsL(new String[]{"Cosmic","Pink","Dormant"});
}
protected void buttonEffect(int i){
	switch(part){
		case"Start":
			//I am Cosmic: Add I am Cosmic to deck
			//I am Pink Rain: Gain a Relic. Lose your starting relic if you had it (Not boss swaps)
			//I am Dormant: Gain a relic that fully heals you at combat end. Lose half your max hp. Resting increases your max hp by 5
			switch(i){
				case 0:exerciseOption("Cosmic");break;
				case 1:exerciseOption("Pink");break;
				case 2:exerciseOption("Dormant");break;
			}break;
		case"End":upText("Hello, You're back");switch(i){case 0:openMap();}
	}}
public void update(){super.update();
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
		if(activeNum==1){for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){addModT(c,new ShuffleFirstTurnEndsInDPMod(1));}}
		clearGrid();activeNum=0;}}
}
