package spireMapOverhaul.zones.CosmicEukotranpha.events;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods.ShuffleFirstTurnEndsInDPMod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.special.TheSunSoFarAway;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class DancingDevil extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				DancingDevil.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
				"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
public DancingDevil(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";optionList.clear();goi.clear();
	optionList.add(new CZMEOption("Blue","[Blue Alley] Add a specific card to deck","Chose Blue Alley",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Green","[Green Meadows] Add 3 random cosmic cards to deck (Does nothing right now)","Chose Green Meadows",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Red","[Red Tundra] Lose 7 hp. Give 3 cards in deck \"First turn end in discard pile: Shuffle this into deck\"","Chose Red Tundra",new String[]{"Exit"},false,null,null,
					"End",0,0,1,"","",true){});
	optionList.add(new CZMEOption("No","[No, No, No] Heal 5 hp",":C",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Exit","[Exit] Open Map","Bye!",null,false,null,null,
					"End",0,0,0,"","",true){});
	sFOsL(new String[]{"Blue","Green","Red","No"});
}
protected void buttonEffect(int i){
	switch(part){
		case"Start":
			//Blue Alley: Add a specific card to deck ("The Sun So Far Away", Hidden description)
			//Green Meadows: Add 3 random cosmic cards to deck (You can't refuse)
			//Red Tundra: Lose 7 hp. Give 3 cards in master deck "First turn end in discard pile: Shuffle this into deck"
			//No, No, No: Heal 5 hp
			switch(i){
				case 0:obtainCard(new TheSunSoFarAway());exerciseOption("Blue");break;
				case 1:exerciseOption("Green");break;//TODO: Implement Functions
				case 2:loseHp(7);openGrid(3,"Timmi");exerciseOption("Red");break;
				case 3:heal(5);exerciseOption("No");break;
			}break;
		case"End":upText("Hello, You're back");switch(i){case 0:openMap();}
	}}
public void update(){super.update();
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
		if(activeNum==1){for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){evAddMod(c,new ShuffleFirstTurnEndsInDPMod(1));}}
		clearGrid();activeNum=0;}}
}
