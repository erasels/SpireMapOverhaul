package spireMapOverhaul.zones.CosmicEukotranpha.events;
import com.megacrit.cardcrawl.cards.colorless.Bite;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.red.Feed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class TightropeOfDeserts extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				TightropeOfDeserts.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
				"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
/*
public CZMEOption Gaze0=new CZMEOption("Gaze 0 (-6 hp. +1 Max hp. Repeatable)","Chose Gaze 0",null,false,null,null,
				"Start1",0,0,0,"","",false){};
public CZMEOption GazeRecur0=new CZMEOption("Gaze 0 (-6 hp. +1 Max hp. Repeatable)","Chose Gaze 0",null,false,null,null,
				"Start1",0,0,0,"","",false){};
public CZMEOption Walk0=new CZMEOption("Lose 3 hp. Change Gaze effect. Choose this 3 times to end the event","Chose Walk 0",null,false,null,null,
				"UsedWalk0",0,0,0,"","",false){};
public CZMEOption DropDown=new CZMEOption("Add 4 Bites and 1 Feed to deck","Chose Drop Down",new CZMEOption[]{Leave},false,null,null,
				"End",0,0,0,"","",true){};
public CZMEOption Focus=new CZMEOption("(This option goes away if you choose any option). Lose 3 max hp. Heal 5 hp. Exit this event","Chose Focus",new CZMEOption[]{Leave},false,null,null,
				"End",0,0,0,"","",true){};
public CZMEOption Gaze1=new CZMEOption("Gaze 1 (Lose 9 hp. Remove a card from your deck. Repeatable)","Chose Gaze 1",new CZMEOption[]{GazeRecur1,Walk1,DropDown},false,null,null,
				"UsedWalk0",0,0,1,"","",false){};
public CZMEOption GazeRecur1=new CZMEOption("Gaze 0 (-6 hp. +1 Max hp. Repeatable)","Chose Gaze 1",null,false,null,null,
				"Start1",0,0,0,"","",false){};
public CZMEOption Gaze2=new CZMEOption("Gaze 2 (-6 hp. +1 Max hp. Repeatable)","Chose Gaze 2",new CZMEOption[]{GazeRecur2,Walk2,DropDown},false,null,null,
				"UsedWalk1",0,0,0,"","",false){};
public CZMEOption GazeRecur2=new CZMEOption("Gaze 0 (-6 hp. +1 Max hp. Repeatable)","Chose Gaze 2",null,false,null,null,
				"Start1",0,0,0,"","",false){};
public CZMEOption Walk1=new CZMEOption("Lose 3 hp. Change Gaze effect. Choose this 2 more times to end the event","Chose Walk 1",null,false,null,null,
				"UsedWalk1",0,0,0,"","",false){};
public CZMEOption Walk2=new CZMEOption("Lose 3 hp. Change Gaze effect. End the event","Chose Walk 2",new CZMEOption[]{Leave},false,null,null,
				"End",0,0,0,"","",true){};
public CZMEOption Leave=new CZMEOption("Leave Now","Hoh? You're back?",null,false,null,null,
				"End",0,0,0,"","",true){};
*/
//Issue: Gaze0 > Gaze0
//Solutions:
//
//

public TightropeOfDeserts(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";optionList.clear();goi.clear();
	optionList.add(new CZMEOption("Gaze0","[Gaze (0)] (-6 hp. +1 Max hp. Repeatable)","Chose Gaze 0",new String[]{"Gaze0","Walk0","DropDown"},false,null,null,
					"Start1",0,0,0,"","",false){});
	optionList.add(new CZMEOption("Walk0","[Walk (0)] Lose 3 hp. Change Gaze effect. Choose this 3 times to end the event","Chose Walk 0",new String[]{"Gaze1","Walk1","DropDown"},false,null,null,
					"UsedWalk0",0,0,0,"","",false){});
	optionList.add(new CZMEOption("DropDown","[Drop Down] Add 4 Bites and 1 Feed to deck","Chose Drop Down",new String[]{"Leave"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Focus","(This option goes away if you choose any option). Lose 3 max hp. Heal 5 hp. Exit this event","Chose Focus",new String[]{"Leave"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Gaze1","Gaze 1 (Lose 19 hp. Remove a card from your deck. Repeatable)","Chose Gaze 1",new String[]{"Gaze1","Walk1","DropDown"},false,null,null,
					"UsedWalk0",0,0,1,"","",false){});
	optionList.add(new CZMEOption("Gaze2","Gaze 2 (Add Madness to deck. Heal 8 hp. Repeatable)","Chose Gaze 2",new String[]{"Gaze2","Walk2","DropDown"},false,null,null,
					"UsedWalk1",0,0,0,"","",false){});
	optionList.add(new CZMEOption("Walk1","Lose 3 hp. Change Gaze effect. Choose this 2 more times to end the event","Chose Walk 1",new String[]{"Gaze2","Walk2","DropDown"},false,null,null,
					"UsedWalk1",0,0,0,"","",false){});
	optionList.add(new CZMEOption("Walk2","Lose 3 hp. Change Gaze effect. End the event","Chose Walk 2",new String[]{"Leave"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Leave","Leave Now","Hoh? You're back?",null,false,null,null,
					"End",0,0,0,"","",true){});
	sFOsL(new String[]{"Gaze0","Walk0","DropDown","Focus"});
}
protected void buttonEffect(int i){
	switch(part){
		case"Start":
			//Gaze: Lose 6 hp. Gain 1 Max hp (Repeatable)
			//Walk: Lose 3 hp. Change Gaze effect. Choose this 3 times to end the event
			//Drop Down: Add 4 Bites and 1 Feed to deck
			//Focus: (This option goes away if you choose any option). Lose 3 max hp. Heal 5 hp. Exit this event
			//Gaze (2): Lose 9 hp. Remove a card from your deck
			//Gaze (3): Add Madness to deck. Heal 5 hp
			switch(i){
				case 0:loseHp(6);incMaxHp(1);exerciseOption("Gaze0");break;
				case 1:loseHp(3);exerciseOption("Walk0");break;
				case 2:obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Feed());exerciseOption("DropDown");break;
				case 3:loseMaxHp(3);heal(5);exerciseOption("Focus");break;
			}break;
		case"Start1":
			switch(i){
				case 0:loseHp(6);incMaxHp(1);exerciseOption("Gaze0");break;
				case 1:loseHp(3);exerciseOption("Walk0");break;
				case 2:obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Feed());exerciseOption("DropDown");break;
			}break;
		case"UsedWalk0":
			switch(i){
				case 0:loseHp(19);exerciseOption("Gaze1");openGrid();break;
				case 1:loseHp(3);exerciseOption("Walk1");break;
				case 2:obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Feed());exerciseOption("DropDown");break;
			}break;
		case"UsedWalk1":
			switch(i){
				case 0:obtainCard(new Madness());heal(8);exerciseOption("Gaze2");break;
				case 1:loseHp(3);exerciseOption("Walk2");break;
				case 2:obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Bite());obtainCard(new Feed());exerciseOption("DropDown");break;
			}break;
		case"End":upText("Hello, You're back");switch(i){case 0:openMap();}
	}}
public void update(){super.update();
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
		removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));clearGrid();activeNum=0;}}

}
