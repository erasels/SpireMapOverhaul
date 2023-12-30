package spireMapOverhaul.zones.CosmicEukotranpha.events;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.FadeWipeParticle;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods.ShuffleFirstTurnEndsInDPMod;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.addModT;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.asc;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class MonochromeRift extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				MonochromeRift.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
				"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
public MonochromeRift(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";optionList.clear();goi.clear();
	optionList.add(new CZMEOption("Front","[Enter] Go to the boss. Gain 10 Max hp","You're Ready",new String[]{"Exit"},false,null,null,
					"Ready",0,0,0,"","",false){});
	optionList.add(new CZMEOption("Back","[Enter from the back] You see a forest... (Does nothing right now)","Chose Back",new String[]{"Exit"},false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Water","[Water] Does nothing","Chose Water",new String[]{"Exit"},false,null,null,
					"End",0,0,1,"","",true){});
	optionList.add(new CZMEOption("Leave","[Leave] Heal 6 hp. (High asc: Add Dazed to deck)","Your head feels funny, Something isn't right",new String[]{"Exit"},false,null,null,
					"End",0,0,1,"","",true){});
	optionList.add(new CZMEOption("Ready","[Exit] Fight","Bye!",null,false,null,null,
					"End",0,0,0,"","",true){});
	optionList.add(new CZMEOption("Exit","[Exit] Open Map","Bye!",null,false,null,null,
					"End",0,0,0,"","",true){});
	sFOsL(new String[]{"Front","Back","Water","Leave"});
}
protected void buttonEffect(int i){
	switch(part){
		case"Start":
			//Enter from the front: Go to the boss. Gain 10 Max hp
			//Enter from the back: Monochrome Forest
			//??? (Pink Rain): Gain Wilted Stems
			//Leave: Heal 6 hp. (High asc: Add Dazed to deck)
			switch(i){
				case 0:incMaxHp(10);exerciseOption("Front");break;
				case 1:exerciseOption("Back");break;
				case 2:exerciseOption("Water");break;
				case 3:heal(6);if(asc>14){obtainCard(new Dazed());}exerciseOption("Leave");break;
			}break;
		case"Ready":AbstractDungeon.getCurrRoom().phase=AbstractRoom.RoomPhase.COMPLETE;MapRoomNode node=new MapRoomNode(-1,15);node.room=new MonsterRoomBoss();
			AbstractDungeon.nextRoom=node;CardCrawlGame.music.fadeOutTempBGM();AbstractDungeon.pathX.add(1);AbstractDungeon.pathY.add(15);
			AbstractDungeon.topLevelEffects.add(new FadeWipeParticle());AbstractDungeon.nextRoomTransitionStart();break;
		case"End":upText("Hello, You're back");switch(i){case 0:openMap();}
	}}
public void update(){super.update();
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
		if(activeNum==1){for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){addModT(c,new ShuffleFirstTurnEndsInDPMod(1));}}
		clearGrid();activeNum=0;}}
}
