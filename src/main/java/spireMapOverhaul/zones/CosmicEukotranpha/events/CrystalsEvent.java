package spireMapOverhaul.zones.CosmicEukotranpha.events;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.special.CrystalsShotgun;import spireMapOverhaul.SpireAnniversary6Mod;
public class CrystalsEvent extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				CrystalsEvent.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
				"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
public CrystalsEvent(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";optionList.clear();goi.clear();
	optionList.add(new CZMEOption("Cheerfully",OPTIONS[0],DESCRIPTIONS[1],new String[]{"Exit"},false,new CrystalsShotgun(),null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Begrudgingly",OPTIONS[1],DESCRIPTIONS[2],new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Suspiciously",OPTIONS[2],DESCRIPTIONS[3],new String[]{"Exit"},false,null,null,
					"End",0,0,1,"","",true){});
	optionList.add(new CZMEOption("PassBy",OPTIONS[3],DESCRIPTIONS[4],new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Exit",OPTIONS[4],"Bye!",null,false,null,null,
					"End",0,0,0,"","",true){});
	sFOsL(new String[]{"Cheerfully","Begrudgingly","Suspiciously","PassBy"});
}
protected void buttonEffect(int i){
	switch(part){
		case"Start":
			//Cheerfully Accept: Heal 8 hp. Add Crystal's Shotgun to deck (16 D. Fasc: Gains Block instead. Then 32 D if you have 60+ B)
			//Begrudgingly Accept: Heal 8 hp. Upgrade random card
			//Suspiciously Accept: Heal 2 hp. Remove up to 1 of 4 randomly chosen cards in deck
			//Pass By: Lose 20 hp. Gain a random common relic
			switch(i){
				case 0:heal(8);obtainCard(new CrystalsShotgun());exerciseOption("Cheerfully");break;
				case 1:heal(8);upgradeRandomCards(1);exerciseOption("Begrudgingly");break;
				case 2:heal(2);openGridOp(xRanCardsFCGroup(4),"");exerciseOption("Suspiciously");break;
				case 3:loseHp(20);newRewardScrn(new RewardItem(randomRelic()));exerciseOption("PassBy");break;
			}break;
		case"End":upText("Hello, You're back");switch(i){case 0:openMap();}
	}}
public void update(){super.update();
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
		if(activeNum==1){removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));}
		clearGrid();activeNum=0;}}
}
