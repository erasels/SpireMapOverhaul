package spireMapOverhaul.zones.CosmicEukotranpha.events;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.*;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.cards.purple.*;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class CubeOfReflectionsEvent extends CosmicZoneEvent{public static final String ID=SpireAnniversary6Mod.makeID(
				CubeOfReflectionsEvent.class.getSimpleName());public static String IMG=CosmicZoneMod.imagePath(
								"events/TODO.png");//TODO
private static final EventStrings eventStrings=CardCrawlGame.languagePack.getEventString(ID);private static final String NAME=eventStrings.NAME;private static final String[]DESCRIPTIONS=eventStrings.DESCRIPTIONS;private static final String[]OPTIONS=eventStrings.OPTIONS;
public CubeOfReflectionsEvent(){super(NAME,DESCRIPTIONS[0],"images/events/sensoryStone.jpg");
	screenNum=0;screenFunc=0;scn="";part="Start";
	setOptions(new String[]{"Touch A (16 hp)","Touch B (34 hp)","Touch C (49 hp)","Touch D (to 1 hp)","Leave A","Leave B"});}
protected void buttonEffect(int i){
	switch(part){
		case"Start":
			//Lose 16 hp. Add 3 Colorless cards to deck
			//Lose 34 hp. Duplicate your deck
			//Lose 49 hp. Add any Rare card from any color (Red, Green, Blue, and Purple) to deck
			//Lose (Your hp)-1 hp. Gain a cosmic relic (Rarity based on health lost), Another for every 30 health
			//Leave: Heal 30 hp. Choose a curse to add to deck, Writhe, Normality, Pride
			//RUN AWAY: Heal 20 hp. Gain 20 Max hp. Choose from above curses then again twice of Decay Parasite Regret, Then Doubt Pain Shame
			switch(i){
				case 0:loseHp(16);newRewardScrn(new RewardItem[]{new RewardItem(COLORLESS),new RewardItem(COLORLESS),new RewardItem(COLORLESS)});
				endScreen("Chose Touch A",new String[]{"Leave Now"},"End");break;
				case 1:loseHp(34);for(AbstractCard c:AbstractDungeon.player.masterDeck.group){AbstractCard ci=c.makeStatEquivalentCopy();ci.inBottleFlame=false;ci.inBottleLightning=false;ci.inBottleTornado=false;obtainCard(ci);}
					endScreen("Chose Touch B",new String[]{"Leave Now"},"End");break;
				case 2:loseHp(49);goi.clear();
					goi.addToTop(new Bludgeon());goi.addToTop(new Feed());goi.addToTop(new FiendFire());goi.addToTop(new Immolate());goi.addToTop(new Reaper());
					goi.addToTop(new DoubleTap());goi.addToTop(new Exhume());goi.addToTop(new Impervious());goi.addToTop(new LimitBreak());goi.addToTop(new Offering());
					goi.addToTop(new Barricade());goi.addToTop(new Berserk());goi.addToTop(new Brutality());goi.addToTop(new Corruption());goi.addToTop(new DemonForm());goi.addToTop(new Juggernaut());
					goi.addToTop(new DieDieDie());goi.addToTop(new GlassKnife());goi.addToTop(new GrandFinale());goi.addToTop(new Unload());
					goi.addToTop(new Adrenaline());goi.addToTop(new Alchemize());goi.addToTop(new BulletTime());goi.addToTop(new Burst());goi.addToTop(new CorpseExplosion());goi.addToTop(new Doppelganger());goi.addToTop(new Malaise());goi.addToTop(new Nightmare());goi.addToTop(new PhantasmalKiller());goi.addToTop(new StormOfSteel());
					goi.addToTop(new AThousandCuts());goi.addToTop(new AfterImage());goi.addToTop(new Envenom());goi.addToTop(new ToolsOfTheTrade());goi.addToTop(new WraithForm());
					goi.addToTop(new AllForOne());goi.addToTop(new CoreSurge());goi.addToTop(new Hyperbeam());goi.addToTop(new MeteorStrike());goi.addToTop(new ThunderStrike());
					goi.addToTop(new Amplify());goi.addToTop(new Fission());goi.addToTop(new MultiCast());goi.addToTop(new Rainbow());goi.addToTop(new Reboot());goi.addToTop(new Seek());
					goi.addToTop(new BiasedCognition());goi.addToTop(new Buffer());goi.addToTop(new CreativeAI());goi.addToTop(new EchoForm());goi.addToTop(new Electrodynamics());goi.addToTop(new MachineLearning());
					goi.addToTop(new Brilliance());goi.addToTop(new LessonLearned());goi.addToTop(new Ragnarok());
					goi.addToTop(new Alpha());goi.addToTop(new Blasphemy());goi.addToTop(new ConjureBlade());goi.addToTop(new DeusExMachina());goi.addToTop(new Judgement());goi.addToTop(new Omniscience());goi.addToTop(new Scrawl());goi.addToTop(new SpiritShield());goi.addToTop(new Vault());goi.addToTop(new Wish());
					goi.addToTop(new DevaForm());goi.addToTop(new Devotion());goi.addToTop(new Establishment());goi.addToTop(new MasterReality());
					for(AbstractCard c:goi.group){for(AbstractRelic r:AbstractDungeon.player.relics){r.onPreviewObtainCard(c);}UnlockTracker.markCardAsSeen(c.cardID);}
					endScreen("Chose Touch C",new String[]{"Leave Now"},"End");openGrid();break;
				case 3:loseHp(AbstractDungeon.player.currentHealth-1);endScreen("Chose Touch D, Does nothing right now",new String[]{"Leave Now"},"End");break;
				case 4:heal(30);goi.clear();goi.group.add(new Writhe());goi.group.add(new Normality());goi.group.add(new Pride());activeNum=1;openGrid();
					endScreen("Chose Leave A",new String[]{"Leave Now"},"End");break;
				case 5:heal(20);incMaxHp(20);goi.clear();
					goi.group.add(new Writhe());goi.group.add(new Normality());goi.group.add(new Pride());
					activeNum=2;openGrid();endScreen("Chose Leave B",new String[]{"Leave Now"},"End");break;
			}break;
		case"End":switch(i){case 0:openMap();}
}}
public void update(){super.update();
	if(activeNum>0&&!AbstractDungeon.isScreenUp&&!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
		AbstractCard c=AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeCopy();obtainCard(c);clearGrid();
		if(activeNum==2){activeNum++;goi.clear();goi.group.add(new Decay());goi.group.add(new Parasite());goi.group.add(new Regret());openGrid();
		}else if(activeNum==3){activeNum++;goi.clear();goi.group.add(new Doubt());goi.group.add(new Pain());goi.group.add(new Shame());openGrid();
		}}}
}
