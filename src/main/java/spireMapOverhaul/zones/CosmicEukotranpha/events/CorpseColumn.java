package spireMapOverhaul.zones.CosmicEukotranpha.events;
import basemod.cardmods.InnateMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.colorless.*;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods.DecreaseCostMod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods.DrawWhenDrawnMod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods.PlayTwiceMod;

import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED;import spireMapOverhaul.SpireAnniversary6Mod;
public class CorpseColumn extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				CorpseColumn.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
				"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
public CardGroup al;public CardGroup bp;public CardGroup cu;public CardGroup dr;
public CorpseColumn(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";optionList.clear();goi.clear();AbstractDungeon.getCurrRoom().phase=AbstractRoom.RoomPhase.COMPLETE;
	al=new CardGroup(UNSPECIFIED);bp=new CardGroup(UNSPECIFIED);cu=new CardGroup(UNSPECIFIED);dr=new CardGroup(UNSPECIFIED);
	for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
		if(c instanceof DramaticEntrance||c instanceof FlashOfSteel||c instanceof MindBlast||c instanceof SwiftStrike||c instanceof HandOfGreed
						||c instanceof BandageUp||c instanceof Blind||c instanceof DarkShackles||c instanceof DeepBreath||c instanceof Discovery
						||c instanceof Enlightenment||c instanceof Finesse||c instanceof Forethought||c instanceof GoodInstincts||c instanceof Impatience
						||c instanceof JackOfAllTrades||c instanceof Madness||c instanceof Panacea||c instanceof PanicButton||c instanceof Purity||c instanceof Trip
						||c instanceof Apotheosis||c instanceof Chrysalis||c instanceof MasterOfStrategy||c instanceof Metamorphosis||c instanceof SecretTechnique
						||c instanceof SecretWeapon||c instanceof TheBomb||c instanceof ThinkingAhead||c instanceof Transmutation||c instanceof Violence
						||c instanceof Magnetism||c instanceof Mayhem||c instanceof Panache||c instanceof SadisticNature){
			if(c.cost>0){al.addToTop(c);}if(!c.isInnate){bp.addToTop(c);}if(!CardModifierManager.hasModifier(c,"CosmicZone:PlayTwiceMod")){cu.addToTop(c);}if(!CardModifierManager.hasModifier(c,"CosmicZone:DrawWhenDrawnMod")){dr.addToTop(c);}
		}}
	optionList.add(new CZMEOption("GraveA","[Grave A] Lose 5 max hp. Set cost of it to 0","\"You who I do not remember, I apologize for my actions which ended in your death. I do not remember a single thing about you but I remember everything I did. I held someone close to you hostage, Someone you loved dearly. Do not worry, They are safe. You however were always going to die. There was no way implemented to land back on Earth. I do not wish for forgivement, But I wish for your peaceful rest\"",new String[]{"GraveA","GraveB","GraveC","GraveD","Exit"},al.isEmpty(),null,null,
					"Start",0,0,1,"","",true){});
	optionList.add(new CZMEOption("GraveB","[Grave B] Add Decay to deck. Give it Innate","Chose Grave B",new String[]{"GraveA","GraveB","GraveC","GraveD","Exit"},bp.isEmpty(),null,null,
					"Start",0,0,2,"","",true){});
	optionList.add(new CZMEOption("GraveC","[Grave C] Lose 20 hp. It is played twice","Chose Grave C",new String[]{"GraveA","GraveB","GraveC","GraveD","Exit"},cu.isEmpty(),null,null,
					"Start",0,0,3,"","",true){});
	optionList.add(new CZMEOption("GraveD","[Grave D] Lose 3 hp. Lose 120 gold if possible. Draw 1 when it is drawn","Chose Grave D",new String[]{"GraveA","GraveB","GraveC","GraveD","Exit"},dr.isEmpty(),null,null,
					"Start",0,0,4,"","",true){});
	optionList.add(new CZMEOption("Exit","[Exit] Open Map","",null,false,null,null,
					"Start",0,0,0,"","",true){});
	sFOsL(new String[]{"GraveA","GraveB","GraveC","GraveD","Exit"});
}
protected void buttonEffect(int i){
	switch(part){case"Start":
		//(Repeatable)
		//Present one of the following cards
		//Dramatic Entrance, Flash of Steel, Mind Blast, Swift Strike, Hand of Greed
		//Bandage Up, Blind, Dark Shackles, Deep Breath, Discovery,
		//Enlightenment, Finesse, Forethought, Good Instincts, Impatience
		//Jack of All Trades, Madness, Panacea, Panic Button, Purity, Trip
		//Apotheosis, Chrysalis, Master of Strategy, Metamorphosis, Secret Technique
		//Secret Weapon, The Bomb, Thinking Ahead, Transmutation, Violence
		//Magnetism, Mayhem, Panache, Sadistic Nature
		//Grave A: Lose 5 max hp. Set cost of it to 0 (Non-0 costs)
		//Grave B: Add Decay to deck. Give it Innate (Any without Innate)
		//Grave C: Lose 20 hp. It is played twice (Any without double play)
		//Grave D: Lose 3 hp. Lose 120 gold if possible. Draw 1 when it is drawn (Any without draw 1 when drawn)
		//Exit: Bye!
		switch(i){
			case 0:loseMaxHp(5);openGrid(al,"AL");activeNum=1;break;
			case 1:obtainCard(new Decay());openGrid(bp,"BP");activeNum=2;break;
			case 2:loseHp(20);openGrid(cu,"CU");activeNum=3;break;
			case 3:loseHp(3);loseGold(120);openGrid(dr,"DR");activeNum=4;break;
			case 4:openMap();break;
		}break;
		case"End":upText("Hello, You're back");switch(i){case 0:openMap();}
	}}
public void update(){super.update();
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){switch(activeNum){
			case 1:evAddMod(AbstractDungeon.gridSelectScreen.selectedCards.get(0),new DecreaseCostMod(99));al.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));getOption("GraveA").disabled=al.isEmpty();exerciseOption("GraveA");break;
			case 2:evAddMod(AbstractDungeon.gridSelectScreen.selectedCards.get(0),new InnateMod());bp.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));getOption("GraveB").disabled=bp.isEmpty();exerciseOption("GraveB");break;
			case 3:evAddMod(AbstractDungeon.gridSelectScreen.selectedCards.get(0),new PlayTwiceMod());cu.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));getOption("GraveC").disabled=cu.isEmpty();exerciseOption("GraveC");break;
			case 4:evAddMod(AbstractDungeon.gridSelectScreen.selectedCards.get(0),new DrawWhenDrawnMod(1));dr.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));getOption("GraveD").disabled=dr.isEmpty();exerciseOption("GraveD");break;
		}activeNum=0;clearGrid();}
}
}
