/*package spireMapOverhaul.zones.CosmicEukotranpha.events;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods.ShuffleFirstTurnEndsInDPMod;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.*;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.POWER;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.addModT;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.randomCosmicCardWeightedRNG;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class FallingInvitation extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				FallingInvitation.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
				"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
public AbstractPotion pot;public AbstractCard c0;public AbstractCard c1;public AbstractCard c2;public AbstractCard c3;public AbstractRelic re;public int jooHp;public int currentBid;public int jooBuys;public int jooCurrMaxBid;public int thefts;public int[]boughts;public int currItem;
public FallingInvitation(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";optionList.clear();goi.clear();
	optionList.add(new CZMEOption("Intro","[Read] Continue","Rules",new String[]{"Stage"},false,null,null,
					"Start0",0,0,0,"","",false){});
	optionList.add(new CZMEOption("Stage","[Listen] Continue","Auction Now",new String[]{"Bid5","Bid10","Pass","Steal"},false,null,null,
					"Auction",0,0,0,"","",false){});
	optionList.add(new CZMEOption("Bid5","[Bid 5]","Bidded 5",new String[]{"AfterAuc0"},false,null,null,
					"Auction",0,0,1,"","",false){});
	optionList.add(new CZMEOption("Bid10","[Bid 10]","Bidded 10",new String[]{"AfterAuc0"},false,null,null,
					"Auction",0,0,1,"","",false){});
	optionList.add(new CZMEOption("Pass","[Pass]","Passed",new String[]{"AfterAuc0"},false,null,null,
					"Auction",0,0,1,"","",false){});
	optionList.add(new CZMEOption("Steal","[Steal]","Stole",new String[]{"AfterAuc0"},false,null,null,
					"Auction",0,0,1,"","",false){});
	optionList.add(new CZMEOption("AfterAuc","[]",":C",new String[]{"Epilogue"},false,null,null,
					"Auction",0,0,0,"","",true){});
	optionList.add(new CZMEOption("AfterAuc0","[]","Bye",new String[]{"Epilogue"},false,null,null,
					"Auction",0,0,0,"","",true){});
	optionList.add(new CZMEOption("AfterAuc1","[]","Bye Bye",new String[]{"Epilogue"},false,null,null,
					"Auction",0,0,0,"","",true){});
	optionList.add(new CZMEOption("AfterAuc2","[]","Bye Bye Bye",new String[]{"Epilogue"},false,null,null,
					"Auction",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Epilogue","[Exit]","",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Exit","[Exit] Open Map","Bye!",null,false,null,null,
					"End",0,0,0,"","",true){});
	int roll=AbstractDungeon.cardRandomRng.random(99);if(roll<55){c0=CardLibrary.getAnyColorCard(COMMON);}else if(roll<85){c0=CardLibrary.getAnyColorCard(UNCOMMON);}else{c0=CardLibrary.getAnyColorCard(RARE);}
	roll=AbstractDungeon.cardRandomRng.random(99);if(roll<55){c1=AbstractDungeon.getCard(COMMON);}else if(roll<85){c1=AbstractDungeon.getCard(UNCOMMON);}else{c1=AbstractDungeon.getCard(RARE);}
	roll=AbstractDungeon.cardRandomRng.random(99);if(roll<55){/*c2=CardLibrary.getAnyColorCard(POWER,COMMON);*//*c2=CardLibrary.getAnyColorCard(COMMON);}else if(roll<85){c2=CardLibrary.getAnyColorCard(POWER,UNCOMMON);}else{c2=CardLibrary.getAnyColorCard(POWER,RARE);}
	pot=AbstractDungeon.returnRandomPotion();c3=randomCosmicCardWeightedRNG();re=randomRelic();jooHp=100;currentBid=0;jooBuys=0;jooCurrMaxBid=0;thefts=0;boughts=new int[]{0,0,0,0,0,0,0};currItem=-1;//Pot,c0,c1,c2,c3,re,girl
	sFOsL(new String[]{"Intro"});
}
protected void buttonEffect(int i){
	switch(part){case"Start":switch(i){case 0:currItem=AbstractDungeon.miscRng.random(4);currentBid=0;
		switch(currItem){
			case 0:jooCurrMaxBid=10;if(pot.rarity==AbstractPotion.PotionRarity.UNCOMMON){jooCurrMaxBid+=4;}if(pot.rarity==AbstractPotion.PotionRarity.RARE){jooCurrMaxBid+=8;}
				if(AbstractDungeon.miscRng.random(1)<1){jooCurrMaxBid+=jooCurrMaxBid/AbstractDungeon.miscRng.random(5,15);
					for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(4)<3){jooCurrMaxBid++;}else{break;}}
				}else{jooCurrMaxBid-=jooCurrMaxBid/AbstractDungeon.miscRng.random(5,15);
					for(int e=jooCurrMaxBid;e>0;e--){if(AbstractDungeon.miscRng.random(4)<3){jooCurrMaxBid--;}else{break;}}}break;
			case 1:jooCurrMaxBid=5;if(c0.rarity==UNCOMMON){jooCurrMaxBid+=5;}if(c0.rarity==RARE){jooCurrMaxBid+=10;}
				if(AbstractDungeon.miscRng.random(1)<1){jooCurrMaxBid+=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
					for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid++;}else{break;}}
				}else{jooCurrMaxBid-=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
					for(int e=jooCurrMaxBid;e>0;e--){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid--;}else{break;}}}break;
			case 2:jooCurrMaxBid=5;if(c1.rarity==UNCOMMON){jooCurrMaxBid+=5;}if(c1.rarity==RARE){jooCurrMaxBid+=10;}
				if(AbstractDungeon.miscRng.random(1)<1){jooCurrMaxBid+=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
					for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid++;}else{break;}}
				}else{jooCurrMaxBid-=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
					for(int e=jooCurrMaxBid;e>0;e--){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid--;}else{break;}}}break;
			case 3:jooCurrMaxBid=5;if(c2.rarity==UNCOMMON){jooCurrMaxBid+=5;}if(c2.rarity==RARE){jooCurrMaxBid+=10;}
				if(AbstractDungeon.miscRng.random(1)<1){jooCurrMaxBid+=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
					for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid++;}else{break;}}
				}else{jooCurrMaxBid-=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
					for(int e=jooCurrMaxBid;e>0;e--){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid--;}else{break;}}}break;
			case 4:jooCurrMaxBid=5;if(c3.rarity==UNCOMMON){jooCurrMaxBid+=5;}if(c3.rarity==RARE){jooCurrMaxBid+=10;}
				if(AbstractDungeon.miscRng.random(1)<1){jooCurrMaxBid+=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
					for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid++;}else{break;}}
				}else{jooCurrMaxBid-=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
					for(int e=jooCurrMaxBid;e>0;e--){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid--;}else{break;}}}break;
		}exerciseOption("Intro");break;}break;
		//Set the following: Current Item, Current Joo Max Bid, CurrBid to 0
		case"Start0":switch(i){case 0:exerciseOption("Stage");break;}break;
		//Items: 1 potion, 3 cards, 1 relic, and the girl
		//The relic appears in 3-5th round, The girl appears in 6th round
		//"Rules: You can bid 5 or 10 HP at a time!
		//Payment is done once nobody else is willing to pay more on the item, We are not responsible for any payment resulting in death.
		//You are forced to receive anything you buy (Even if you are dead).
		//If there is only one person, The auction will be cancelled"
		//The Man has 100 max hp and will bid a specified percentage of his current health,
		//10% for potions, 5% for cards, 30% for relics, Increasing based on rarity, Will pay 60% for girl (+-10% then inc/dec by 1 with 60% repeat chance)
		//(10 14 18, 5 10 20, 30 35 40)
		//Will leave after buying 2 items
		//You can only steal once (The auction still continues)
		case"Auction":switch(i){
			//Bid 5 hp
			//Bid 10 hp
			//Pass
			//Steal
			case 0:currentBid+=5;if(jooCurrMaxBid>=currentBid){upText("Current Item: "+currItem+". Current Bid: "+currentBid+". jooCurrMaxBid: "+jooCurrMaxBid);
				}else{loseHp(currentBid);upText("Bought it");this.imageEventText.clearAllDialogs();setOption("Next");part="AfterAuc0";boughts[currItem]=1;
					switch(currItem){
						case 0:AbstractDungeon.player.obtainPotion(pot);break;
						case 1:obtainCard(c0);break;
						case 2:obtainCard(c1);break;
						case 3:obtainCard(c2);break;
						case 4:obtainCard(c3);break;
						case 5:AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)Settings.WIDTH/2.0F,(float)Settings.HEIGHT/2.0F,re);break;
						case 6:obtainCard(c3);upText("Bought Girl");part="AfterAuc2";break;
					}}break;
			case 1:currentBid+=10;if(jooCurrMaxBid>=currentBid){upText("Current Item: "+currItem+". Current Bid: "+currentBid+". jooCurrMaxBid: "+jooCurrMaxBid);
			}else{loseHp(currentBid);upText("Bought it");this.imageEventText.clearAllDialogs();setOption("Next");part="AfterAuc0";boughts[currItem]=1;
				switch(currItem){
					case 0:AbstractDungeon.player.obtainPotion(pot);break;
					case 1:obtainCard(c0);break;
					case 2:obtainCard(c1);break;
					case 3:obtainCard(c2);break;
					case 4:obtainCard(c3);break;
					case 5:AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)Settings.WIDTH/2.0F,(float)Settings.HEIGHT/2.0F,re);break;
					case 6:obtainCard(c3);upText("Bought Girl");part="AfterAuc2";break;
				}}break;
			case 2:if(currentBid>0){upText("Joo'ed it");jooHp-=currentBid;jooBuys++;if(jooBuys>1){upText("Joo'ed it and Left");part="AfterAuc1";}}else{upText("Nobody bought it");}boughts[currItem]=1;if(currItem==6){upText("Joo'ed girl");part="AfterAuc2";}this.imageEventText.clearAllDialogs();setOption("Next");break;
			case 3:upText("Stole it");this.imageEventText.clearAllDialogs();setOption("Next");part="AfterAuc0";thefts++;boughts[currItem]=1;
				switch(currItem){
					case 0:AbstractDungeon.player.obtainPotion(pot);break;
					case 1:obtainCard(c0);break;
					case 2:obtainCard(c1);break;
					case 3:obtainCard(c2);break;
					case 4:obtainCard(c3);break;
					case 5:AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)Settings.WIDTH/2.0F,(float)Settings.HEIGHT/2.0F,re);break;
					case 6:obtainCard(c3);upText("Stole Girl");part="AfterAuc2";break;
				}break;
			//A: Joo wants to bid
			// > Increase Curr Bid by 5 and give priority to you
			//B: Joo doesn't want to bid
			// > Get item & Pay
			// > Goes to next item
			//C: Pass
			// > Joo Pays and Leaves if 2 buys
			//	> Else goes to next item
			//D: Nobody Bids
			// > Next item
			//E: Steal
			// > Disable steal then next item
		}break;
		case"AfterAuc0":switch(i){case 0:currentBid=0;currItem=AbstractDungeon.miscRng.random(5);int we=0;
			if(boughts[currItem]==1){
				while(true){currItem++;if(currItem==6){currItem=0;}we++;
					if(boughts[currItem]==0){break;}
					if(we>5){currItem=6;break;}}}
			switch(currItem){
				case 0:jooCurrMaxBid=jooHp/10;if(pot.rarity==AbstractPotion.PotionRarity.UNCOMMON){jooCurrMaxBid+=jooHp*2/5;}if(pot.rarity==AbstractPotion.PotionRarity.RARE){jooCurrMaxBid+=jooHp*2/5;}
					if(AbstractDungeon.miscRng.random(1)<1){jooCurrMaxBid+=Math.min(jooCurrMaxBid/AbstractDungeon.miscRng.random(5,15),jooHp);
						for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(4)<3){jooCurrMaxBid++;}else{break;}}
					}else{jooCurrMaxBid-=jooCurrMaxBid/AbstractDungeon.miscRng.random(5,15);
						for(int e=jooCurrMaxBid;e>0;e--){if(AbstractDungeon.miscRng.random(4)<3){jooCurrMaxBid--;}else{break;}}}break;
				case 1:jooCurrMaxBid=jooHp/20;if(c0.rarity==UNCOMMON){jooCurrMaxBid+=jooHp/20;}if(c0.rarity==RARE){jooCurrMaxBid+=jooHp/10;}
					if(AbstractDungeon.miscRng.random(1)<1){jooCurrMaxBid+=Math.min(jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10,jooHp);
						for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid++;}else{break;}}
					}else{jooCurrMaxBid-=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
						for(int e=jooCurrMaxBid;e>0;e--){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid--;}else{break;}}}break;
				case 2:jooCurrMaxBid=jooHp/20;if(c1.rarity==UNCOMMON){jooCurrMaxBid+=jooHp/20;}if(c1.rarity==RARE){jooCurrMaxBid+=jooHp/10;}
					if(AbstractDungeon.miscRng.random(1)<1){jooCurrMaxBid+=Math.min(jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10,jooHp);
						for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid++;}else{break;}}
					}else{jooCurrMaxBid-=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
						for(int e=jooCurrMaxBid;e>0;e--){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid--;}else{break;}}}break;
				case 3:jooCurrMaxBid=jooHp/20;if(c2.rarity==UNCOMMON){jooCurrMaxBid+=jooHp/20;}if(c2.rarity==RARE){jooCurrMaxBid+=jooHp/10;}
					if(AbstractDungeon.miscRng.random(1)<1){jooCurrMaxBid+=Math.min(jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10,jooHp);
						for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid++;}else{break;}}
					}else{jooCurrMaxBid-=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
						for(int e=jooCurrMaxBid;e>0;e--){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid--;}else{break;}}}break;
				case 4:jooCurrMaxBid=jooHp/20;if(c3.rarity==UNCOMMON){jooCurrMaxBid+=jooHp/20;}if(c3.rarity==RARE){jooCurrMaxBid+=jooHp/10;}
					if(AbstractDungeon.miscRng.random(1)<1){jooCurrMaxBid+=Math.min(jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10,jooHp);
						for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid++;}else{break;}}
					}else{jooCurrMaxBid-=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
						for(int e=jooCurrMaxBid;e>0;e--){if(AbstractDungeon.miscRng.random(9)<7){jooCurrMaxBid--;}else{break;}}}break;
				case 5:jooCurrMaxBid=jooHp/5;if(re.tier==AbstractRelic.RelicTier.UNCOMMON){jooCurrMaxBid+=jooHp/20;}if(re.tier==AbstractRelic.RelicTier.RARE){jooCurrMaxBid+=jooHp/20;}
					if(AbstractDungeon.miscRng.random(1)<1){jooCurrMaxBid+=Math.min(jooCurrMaxBid*AbstractDungeon.miscRng.random(1,10)/10,jooHp);
						for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(9)<8){jooCurrMaxBid++;}else{break;}}
					}else{jooCurrMaxBid-=jooCurrMaxBid*AbstractDungeon.miscRng.random(10)/10;
						for(int e=jooCurrMaxBid;e>0;e--){if(AbstractDungeon.miscRng.random(9)<6){jooCurrMaxBid--;}else{break;}}}break;
				case 6:jooCurrMaxBid=jooHp*3/5;
					jooCurrMaxBid+=Math.min(jooCurrMaxBid*AbstractDungeon.miscRng.random(1,10)/10,jooHp);
					for(int e=jooCurrMaxBid;e<jooHp;e++){if(AbstractDungeon.miscRng.random(9)<8){jooCurrMaxBid++;}else{break;}}
			}}break;
		case"AfterAuc1":exerciseOption("Epilogue");openMap();break;
		case"AfterAuc2":exerciseOption("Epilogue");openMap();break;
		case"End":upText("Hello, You're back");switch(i){case 0:openMap();}
	}}
public void update(){super.update();
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
		if(activeNum==1){for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){addModT(c,new ShuffleFirstTurnEndsInDPMod(1));}}
		clearGrid();activeNum=0;}}
}
*/