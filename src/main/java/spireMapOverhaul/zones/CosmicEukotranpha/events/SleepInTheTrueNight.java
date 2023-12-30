package spireMapOverhaul.zones.CosmicEukotranpha.events;
import basemod.cardmods.EtherealMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods.GraveMod;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.LunaFloraAstrellia;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.addModT;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class SleepInTheTrueNight extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				SleepInTheTrueNight.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
				"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
public SleepInTheTrueNight(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";optionList.clear();goi.clear();
	optionList.add(new CZMEOption("Fight","[Fight]","Chose Fight",new String[]{"Exit"},false,null,null,
					"Fight",0,0,0,"","",false){});
	optionList.add(new CZMEOption("Sleep","[Sleep] Lose 20 hp. Add random Uncommon to deck. (If more than 1 Uncommon in deck: )Transform an Uncommon card. Transform an Uncommon card","Chose Sleep",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("PassBy","[Pass By] Heal 7 hp. Add Pain with Grave and Regret with Ethereal to master deck. Upgrade 2 random cards in deck","Chose Pass By",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Exit","[Exit] Open Map","Bye!",null,false,null,null,
					"End",0,0,0,"","",true){});
	sFOsL(new String[]{"Fight","Sleep","PassBy"});
}
protected void buttonEffect(int i){
	switch(part){case"Start":
			//Fight: Luna-Flora Astrellia. Rewards based on how you finished it
			//Sleep: Lose 20 hp. Add random Uncommon to deck. (If more than 1 Uncommon in deck: )Transform an Uncommon card. Transform an Uncommon card
			//Pass by: Heal 7 hp. Add Pain with Grave and Regret with Ethereal to master deck. Upgrade 2 random cards in deck
			switch(i){
				case 0:exerciseOption("Fight");AbstractDungeon.getCurrRoom().monsters=new MonsterGroup(new LunaFloraAstrellia());
					AbstractDungeon.getCurrRoom().rewards.clear();//TODO: Add better rewards later
					this.enterCombatFromImage();break;
				case 1:loseHp(20);for(AbstractCard c:AbstractDungeon.player.masterDeck.group){if(c.rarity==AbstractCard.CardRarity.UNCOMMON){activeNum=1;break;}}
					obtainCard(AbstractDungeon.getCard(AbstractCard.CardRarity.UNCOMMON));exerciseOption("Sleep");break;
				case 2:heal(7);AbstractCard c=new Pain();obtainCard(c);addModT(c,new GraveMod());AbstractCard c1=new Regret();obtainCard(c1);addModT(c,new EtherealMod());
					upgradeRandomCards(2);exerciseOption("PassBy");break;
			}break;
		case"End":upText("Hello, You're back");switch(i){case 0:openMap();}
	}}
public void update(){super.update();
	if(activeNum==1){for(AbstractCard c:AbstractDungeon.player.masterDeck.group){if(c.rarity==AbstractCard.CardRarity.UNCOMMON){goi.addToBottom(c);}}openGrid();activeNum++;}
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
		if(activeNum==2){activeNum++;clearGrid();transformCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
			for(AbstractCard c:AbstractDungeon.player.masterDeck.group){if(c.rarity==AbstractCard.CardRarity.UNCOMMON){goi.addToBottom(c);}}openGrid();
		}else if(activeNum==3){transformCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));activeNum=0;clearGrid();}}
		}
}
