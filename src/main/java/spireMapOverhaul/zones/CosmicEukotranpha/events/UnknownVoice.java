package spireMapOverhaul.zones.CosmicEukotranpha.events;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.Iterator;import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class UnknownVoice extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				UnknownVoice.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
				"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
public UnknownVoice(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";optionList.clear();goi.clear();
	optionList.add(new CZMEOption("Ask","[Ask for Clarification] (-4hp. Choose card to add to deck)","Chose Ask",new String[]{"Converse0","Meet0"},false,null,null,
					"Alma",0,0,1,"","",false){});
	optionList.add(new CZMEOption("Converse0","[Converse] -8 hp. Transform 1 of 3 random cards in deck","Chose Converse 0",new String[]{"Converse1","Meet1"},false,null,null,
					"Bulti",0,0,2,"","",false){});
	optionList.add(new CZMEOption("Converse1","[Converse] -8 hp. Transform","Chose Converse 1",new String[]{"Converse0","Meet0"},false,null,null,
					"Alma",0,0,2,"","",true){});
	optionList.add(new CZMEOption("Meet0","[Meet] Shame to deck","Chose Meet 0",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Meet1","[Meet] -16 hp","Chose Meet 1",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Leave","[Leave] +9 hp. Doubt to deck","Chose Leave",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Exit","[Exit] Open Map","Bye!",null,false,null,null,
					"End",0,0,0,"","",true){});
	sFOsL(new String[]{"Ask","Leave"});
}
protected void buttonEffect(int i){
	switch(part){
		case"Start":
			//Ask for Clarification: Lose 4 hp. Choose 1 of 6 cards not in your deck to add to deck
			//Converse: Lose 8 hp. Transform up to 1 of 3 randomly selected cards in deck. Repeatable
			//Converse has different text when repeated
			//Meet (Alternates): Add Shame to deck
			//Meet (Alternates): Lose 16 hp
			//Leave: Heal 9 hp. Add Doubt to deck
			switch(i){
				case 0:loseHp(4);
					for(int we=0;we<6;++we){AbstractCard card=AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();boolean containsDupe=true;int repeat=0;
						while(true){while(containsDupe){containsDupe=false;
								for(AbstractCard c:goi.group){
									if(c.cardID.equals(card.cardID)){containsDupe=true;card=AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();break;
									}else{for(AbstractCard v:AbstractDungeon.player.masterDeck.group){
											if(v.cardID.equals(card.cardID)&&repeat<6){containsDupe=true;repeat++;card=AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();
											}}}}}
							if(goi.contains(card)){--we;}else{for(AbstractRelic r:AbstractDungeon.player.relics){r.onPreviewObtainCard(card);}goi.addToBottom(card);}
							break;}}
					Iterator var8=goi.group.iterator();
					while(var8.hasNext()){
						AbstractCard c=(AbstractCard)var8.next();
						UnlockTracker.markCardAsSeen(c.cardID);
					}openGrid();exerciseOption("Ask");break;
				case 1:heal(9);obtainCard(new Doubt());exerciseOption("Leave");break;
			}break;
		case"Alma":
			switch(i){
				case 0:loseHp(8);openGrid(1,xRanCardsFCGroup(3),"TODO");exerciseOption("Converse0");break;
				case 1:obtainCard(new Shame());exerciseOption("Meet0");break;
			}break;
		case"Bulti":
			switch(i){
				case 0:loseHp(8);openGrid(1,xRanCardsFCGroup(3),"TODO");exerciseOption("Converse1");break;
				case 1:loseHp(16);exerciseOption("Meet1");break;
			}break;
		case"End":upText("Hello, You're back");switch(i){case 0:openMap();}
	}}
public void update(){super.update();
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
		if(activeNum==1){obtainCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeCopy());
		}else if(activeNum==2){transformCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));}
		clearGrid();activeNum=0;}}
}
