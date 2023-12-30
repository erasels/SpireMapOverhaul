package spireMapOverhaul.zones.CosmicEukotranpha.events;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.cards.purple.Omniscience;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.*;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.ATTACK;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.POWER;import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class AstralightRoads extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				AstralightRoads.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
				"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
public CardGroup al;public CardGroup bp;public CardGroup cu;public CardGroup dr;public int distance=0;
public AstralightRoads(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";optionList.clear();goi.clear();AbstractDungeon.getCurrRoom().phase=AbstractRoom.RoomPhase.COMPLETE;
	optionList.add(new CZMEOption("Travel","[Travel] Lose 2 hp. Walk down Astralight Roads","Travelling",new String[]{"Browse","Place","JumpOff"},false,null,null,
					"Roads",0,0,0,"","",false){});
	optionList.add(new CZMEOption("PassBy","[Pass By] Heal 11 hp. Add Regret to deck","Passed By",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Browse","[Browse] Lose 2 hp. Change Place","Browsing",new String[]{"Browse","Place","JumpOff"},false,null,null,
					"Roads",0,0,0,"","",false){});
	optionList.add(new CZMEOption("Place","[Place] Place X card","Placed ?",new String[]{"Browse","Place","JumpOff"},false,null,null,
					"Roads",0,0,1,"","",false){});
	optionList.add(new CZMEOption("Place0","[Place] Place Basic card","Placed 0",new String[]{"Browse","Place","JumpOff"},false,null,null,
					"Roads",0,0,1,"","",false){});
	optionList.add(new CZMEOption("Place1","[Place] Place Common card","Placed 1",new String[]{"Browse","Place","JumpOff"},false,null,null,
					"Roads",0,0,2,"","",false){});
	optionList.add(new CZMEOption("Place2","[Place] Place Uncommon card","Placed 2",new String[]{"Browse","Place","JumpOff"},false,null,null,
					"Roads",0,0,3,"","",false){});
	optionList.add(new CZMEOption("Place3","[Place] Place Rare card","Placed 3",new String[]{"Browse","Place","JumpOff"},false,null,null,
					"Roads",0,0,4,"","",false){});
	optionList.add(new CZMEOption("Place4","[Place] Place Special card","Placed 4",new String[]{"Browse","Place","JumpOff"},false,null,null,
					"Roads",0,0,5,"","",false){});
	optionList.add(new CZMEOption("Place5","[Place] Place Curse card","Placed 5",new String[]{"Browse","Place","JumpOff"},false,null,null,
					"Roads",0,0,6,"","",false){});
	optionList.add(new CZMEOption("Place6","[Place] Place Power card","Placed 6",new String[]{"Browse","Place","JumpOff"},false,null,null,
					"Roads",0,0,7,"","",false){});
	optionList.add(new CZMEOption("Place7","[Place] Place Any card","Placed 7",new String[]{"Browse","Place","JumpOff"},false,null,null,
					"Roads",0,0,8,"","",false){});
	optionList.add(new CZMEOption("JumpOff","[Jump Off] Add Doubt to deck if you used Browse or Place less than 5 times total","Jumped off",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",false){});
	optionList.add(new CZMEOption("Exit","[Exit] Open Map","",null,false,null,null,
					"End",0,0,0,"","",true){});
	sFOsL(new String[]{"Travel","PassBy"});
}
protected void buttonEffect(int i){
	switch(part){case"Start":switch(i){
		//Travel: Lose 2 hp. Go to Roads
		//Pass By: Heal 11 hp. Add Regret to deck
			case 0:loseHp(2);int w=AbstractDungeon.miscRng.random(6);getOption("Travel").optionsStrings[1]="Place"+w;getOption("Travel").nextScreenNum=w;
				goi.clear();switch(w){
				case 0:for(AbstractCard c:purgablMD().group){if(c.rarity==BASIC){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 1:for(AbstractCard c:purgablMD().group){if(c.rarity==COMMON){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 2:for(AbstractCard c:purgablMD().group){if(c.rarity==UNCOMMON){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 3:for(AbstractCard c:purgablMD().group){if(c.rarity==RARE){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 4:for(AbstractCard c:purgablMD().group){if(c.rarity==SPECIAL){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 5:for(AbstractCard c:purgablMD().group){if(c.rarity==CURSE){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 6:for(AbstractCard c:purgablMD().group){if(c.type==POWER){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;}exerciseOption("Travel");break;
			case 1:heal(11);obtainCard(new Regret());exerciseOption("PassBy");break;
		}break;
		case"Roads":switch(i){
			//Browse: Lose 2 hp. Change Place to something else
			//Place: Remove specific type of card in deck. Lose 10 hp. Then do an effect
			//Jump Off: Add Doubt to deck if you used Browse or Place less than 5 times total
			case 0:loseHp(2);int w=AbstractDungeon.miscRng.random(6);getOption("Browse").optionsStrings[1]="Place"+w;getOption("Browse").nextScreenNum=w;
			goi.clear();switch(w){
				case 0:for(AbstractCard c:purgablMD().group){if(c.rarity==BASIC){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 1:for(AbstractCard c:purgablMD().group){if(c.rarity==COMMON){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 2:for(AbstractCard c:purgablMD().group){if(c.rarity==UNCOMMON){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 3:for(AbstractCard c:purgablMD().group){if(c.rarity==RARE){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 4:for(AbstractCard c:purgablMD().group){if(c.rarity==SPECIAL){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 5:for(AbstractCard c:purgablMD().group){if(c.rarity==CURSE){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
				case 6:for(AbstractCard c:purgablMD().group){if(c.type==POWER){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;}distance++;exerciseOption("Browse");break;
			case 1:openGrid();distance++;activeNum=getOption("Place"+screenNum).nextActiveNum;break;
			case 2:if(distance<5){obtainCard(new Doubt());}exerciseOption("JumpOff");break;
			}break;
		case"End":upText("Hello, You're back");switch(i){case 0:openMap();}
	}}
public void update(){super.update();
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){switch(activeNum){
		//[Basic] Remove 1 of 4 random cards in deck
		//[Common] Choose up to 3 of 9 (RedGreBlePurCos) Common Atks to add to deck
		//[Uncommon] Next frame card input any card
		//[Rare] Card reward of Cosmic card or Omniscience
		//[Special] Add "" to deck
		//[Curse] Gain 2 Max hp
		//[Power] Choose from 2 of 10 cards to add to deck
		case 1:removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));clearGrid();openGrid(xRanCardsFCGroup(4,purgablMD()),"");break;
		case 2:removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));clearGrid();for(int i=0;i<9;i++){goi.addToTop(AbstractDungeon.getCardFromPool(COMMON,ATTACK,true));}openGrid(3,"");break;
		case 3:removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));clearGrid();break;
		case 4:removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));goi.addToTop(new Omniscience());openGrid();break;
		case 5:removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));obtainCard(new Strike_Red());break;
		case 6:removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));incMaxHp(2);break;
		case 7:removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));addRandomCardsToGoi(10);openGrid(2,"");break;
		case 8:break;
		case 9:removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));clearGrid();break;
		case 10:for(AbstractCard co:AbstractDungeon.gridSelectScreen.selectedCards){obtainCard(co.makeCopy());}clearGrid();break;
		case 11:obtainCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));clearGrid();break;
		case 12:obtainCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));obtainCard(AbstractDungeon.gridSelectScreen.selectedCards.get(1));break;
	}
	if(activeNum>4&&activeNum!=7){
		int w=AbstractDungeon.miscRng.random(6);getOption("Place"+(activeNum-1)).optionsStrings[1]="Place"+w;getOption("Place"+(activeNum-1)).nextScreenNum=w;
		clearGrid();switch(w){
			case 0:for(AbstractCard c:purgablMD().group){if(c.rarity==BASIC){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
			case 1:for(AbstractCard c:purgablMD().group){if(c.rarity==COMMON){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
			case 2:for(AbstractCard c:purgablMD().group){if(c.rarity==UNCOMMON){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
			case 3:for(AbstractCard c:purgablMD().group){if(c.rarity==RARE){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
			case 4:for(AbstractCard c:purgablMD().group){if(c.rarity==SPECIAL){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
			case 5:for(AbstractCard c:purgablMD().group){if(c.rarity==CURSE){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;
			case 6:for(AbstractCard c:purgablMD().group){if(c.type==POWER){goi.addToTop(c);}}getOption("Place"+w).disabled=goi.isEmpty();break;}}
	if(activeNum==3){
		getOption("Place"+(activeNum-1)).optionsStrings[1]="Place"+7;getOption("Place"+(activeNum-1)).nextScreenNum=7;
		for(AbstractCard c:purgablMD().group){goi.addToTop(c);}getOption("Place"+7).disabled=goi.isEmpty();
	}
		switch(activeNum){
			case 1:activeNum=9;break;
			case 2:activeNum=10;break;
			case 3:exerciseOption("Place2");break;
			case 4:activeNum=11;break;
			case 5:exerciseOption("Place4");break;
			case 6:exerciseOption("Place5");break;
			case 7:activeNum=12;break;
			case 8:break;
			case 9:exerciseOption("Place0");break;
			case 10:exerciseOption("Place1");break;
			case 11:exerciseOption("Place3");break;
			case 12:exerciseOption("Place6");break;
		}
	
	}
}
}
