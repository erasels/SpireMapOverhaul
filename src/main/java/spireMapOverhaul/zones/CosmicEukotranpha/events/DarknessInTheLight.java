package spireMapOverhaul.zones.CosmicEukotranpha.events;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.special.LightbornButchery;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.*;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class DarknessInTheLight extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				DarknessInTheLight.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
				"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
public int killed=0;
public DarknessInTheLight(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";optionList.clear();goi.clear();killed=0;
	optionList.add(new CZMEOption("Stars","[Look at the Stars] Lose 10 max hp. Duplicate a card in deck","Chose Stars",new String[]{"Exit"},false,null,null,
					"End",0,0,1,"","",true){});
	optionList.add(new CZMEOption("bOt","[Touch the Construct] Heal half your missing hp. Add Decay to deck","Chose Touch",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("kOllmOk","[Talk] Lose half your hp. Transform an attack, skill, and power","Chose Talk",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Darkling","[Kill Darkling] Lose 2 hp. Add \"Lightborn Butchery\" to deck. Continue event","Murder",new String[]{"Stars","bOt","kOllmOk","Darkling"},false,null,null,
					"End",0,0,0,"","",false){});
	optionList.add(new CZMEOption("Exit","[Exit] Open Map","Bye!",null,false,null,null,
					"End",0,0,0,"","",true){});
	sFOsL(new String[]{"Stars","bOt","kOllmOk","Darkling"});
}
protected void buttonEffect(int i){
	switch(part){
		case"Start":
			//Look at the Stars: Lose 10 max hp (12). Duplicate a card in deck
			//Touch the Construct: Heal half your missing hp (Half -5). Add Decay to deck
			//Talk to the person: Lose half your hp (Half your hp +5). Transform an attack, skill, and power
			//Kill a Darkling: Lose 2 hp. Add "Lightborn Butchery" to deck. Repeatable but increases hp cost by 4 (6) each time
			switch(i){
				case 0:loseMaxHp(10);openGrid();exerciseOption("Stars");break;
				case 1:heal((AbstractDungeon.player.maxHealth-AbstractDungeon.player.currentHealth)/2);obtainCard(new Decay());exerciseOption("bOt");break;
				case 2:loseHp(AbstractDungeon.player.currentHealth/2);exerciseOption("kOllmOk");
				for(AbstractCard c:purgablMD().group){if(c.type==ATTACK){goi.addToTop(c);}}
				if(!goi.isEmpty()){activeNum=2;openGrid();
				}else{for(AbstractCard c:purgablMD().group){if(c.type==SKILL){goi.addToTop(c);}}
					if(!goi.isEmpty()){activeNum=3;openGrid();
					}else{for(AbstractCard c:purgablMD().group){if(c.type==POWER){goi.addToTop(c);}}
						if(!goi.isEmpty()){activeNum=4;openGrid();}}}break;
				case 3:loseHp(2+killed);obtainCard(new LightbornButchery());exerciseOption("Darkling");killed+=4;break;
			}break;
		case"End":upText("Hello, You're back");switch(i){case 0:openMap();}
	}}
public void update(){super.update();
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
		switch(activeNum){
			case 1:dupliCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));clearGrid();activeNum=0;break;
			case 2:transformCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));clearGrid();
				for(AbstractCard c:purgablMD().group){if(c.type==SKILL){goi.addToTop(c);}}
				if(!goi.isEmpty()){activeNum=3;openGrid();
				}else{for(AbstractCard c:purgablMD().group){if(c.type==POWER){goi.addToTop(c);}}
					if(!goi.isEmpty()){activeNum=4;openGrid();}}break;
			case 3:transformCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));clearGrid();
				for(AbstractCard c:purgablMD().group){if(c.type==POWER){goi.addToTop(c);}}
				if(!goi.isEmpty()){activeNum=4;openGrid();}break;
			case 4:transformCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));clearGrid();break;
		}}}
}
