package spireMapOverhaul.zones.CosmicEukotranpha.events;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED;
import static com.megacrit.cardcrawl.cards.CardGroup.getGroupWithoutBottledCards;
public class CosmicZoneEvent extends AbstractImageEvent{public int screenNum=0;public int screenFunc=0;public int activeNum=0;public String scn="";public String part="Start";public int internalOptionNumber=0;public CardGroup goi=new CardGroup(UNSPECIFIED);
public ArrayList<CZMEOption>optionList=new ArrayList<>();//public CZMEOption[]OptionList=new CZMEOption[]{};
public String ID="I spent 3 days trying to figure out the solution to a crash (NoSuchField) and it was this, I am so mad";
public CosmicZoneEvent(String title,String body,String imgurl){super(title,body,imgurl);noCardsInRewards=true;}
@Override protected void buttonEffect(int i){}

public CZMEOption getOption(String identifier){for(CZMEOption option:optionList){if(option.identifier.equals(identifier)){return option;}}return null;}

public void setOptions(String[]options){for(String s:options){imageEventText.setDialogOption(s);}}
public void setOption(CZMEOption option){imageEventText.setDialogOption(option.title,option.disabled,option.previewCard,option.previewRelic);}
public void setOption(CZMEOption option,boolean isDisabled){imageEventText.setDialogOption(option.title,isDisabled,option.previewCard,option.previewRelic);}
public void setOptions(CZMEOption[]options){for(CZMEOption op:options){imageEventText.setDialogOption(op.title,op.disabled,op.previewCard,op.previewRelic);}}
public void setOptions(CZMEOption[]options,boolean[]isDisableds){int weee=0;for(CZMEOption op:options){imageEventText.setDialogOption(op.title,isDisableds[weee],op.previewCard,op.previewRelic);weee++;}}
public void sFOL(String optionIdentifier){for(CZMEOption option:optionList){if(option.identifier.equals(optionIdentifier)){setOption(option);break;}}}
public void sFOsL(String[]optionIdentifiers){for(String opI:optionIdentifiers){for(CZMEOption option:optionList){if(option.identifier.equals(opI)){setCZMEOption(option);break;}}}}
public void setCZMEOption(CZMEOption czmeOption){imageEventText.setDialogOption(czmeOption.title,czmeOption.disabled,czmeOption.previewCard,czmeOption.previewRelic);}

public void setOption(String option){imageEventText.setDialogOption(option);}
public void setOption(String option,boolean isDisabled){imageEventText.setDialogOption(option,isDisabled);}
public void setDeadOption(String option){imageEventText.setDialogOption(option,true);}
public void setOption(String option,AbstractCard previewCard){imageEventText.setDialogOption(option,false,previewCard,null);}
public void setOption(String option,boolean isDisabled,AbstractCard previewCard){imageEventText.setDialogOption(option,isDisabled,previewCard,null);}
public void setOption(String option,AbstractRelic previewRelic){imageEventText.setDialogOption(option,false,null,previewRelic);}
public void setOption(String option,boolean isDisabled,AbstractRelic previewRelic){imageEventText.setDialogOption(option,isDisabled,null,previewRelic);}
public void setOption(String option,AbstractCard previewCard,AbstractRelic previewRelic){imageEventText.setDialogOption(option,false,previewCard,previewRelic);}
public void setOption(String option,boolean isDisabled,AbstractCard previewCard,AbstractRelic previewRelic){imageEventText.setDialogOption(option,isDisabled,previewCard,previewRelic);}

public void upText(String bodyText){imageEventText.updateBodyText(bodyText);}
public void upOption(String option){this.imageEventText.updateDialogOption(internalOptionNumber,option);internalOptionNumber++;}
public void upOption(String option,boolean isDisabled){this.imageEventText.updateDialogOption(internalOptionNumber,option,isDisabled);internalOptionNumber++;}
public void upOption(String option,AbstractCard previewCard){this.imageEventText.updateDialogOption(internalOptionNumber,option,previewCard);internalOptionNumber++;}
public void upOption(String option,boolean isDisabled,AbstractCard previewCard){
	if(!imageEventText.optionList.isEmpty()){
		if(imageEventText.optionList.size()>internalOptionNumber){
			imageEventText.optionList.set(internalOptionNumber,new LargeDialogOptionButton(internalOptionNumber,option,isDisabled,previewCard));
		}else{imageEventText.optionList.add(new LargeDialogOptionButton(internalOptionNumber,option,isDisabled,previewCard));}
	}else{imageEventText.optionList.add(new LargeDialogOptionButton(internalOptionNumber,option,isDisabled,previewCard));}
	internalOptionNumber++;}
public void upOption(String option,AbstractRelic previewRelic){
	if(!imageEventText.optionList.isEmpty()){
		if(imageEventText.optionList.size()>internalOptionNumber){
			imageEventText.optionList.set(internalOptionNumber,new LargeDialogOptionButton(internalOptionNumber,option,false,previewRelic));
		}else{imageEventText.optionList.add(new LargeDialogOptionButton(internalOptionNumber,option,false,previewRelic));}
	}else{imageEventText.optionList.add(new LargeDialogOptionButton(internalOptionNumber,option,false,previewRelic));}
	internalOptionNumber++;}
public void upOption(String option,boolean isDisabled,AbstractRelic previewRelic){
	if(!imageEventText.optionList.isEmpty()){
		if(imageEventText.optionList.size()>internalOptionNumber){
			imageEventText.optionList.set(internalOptionNumber,new LargeDialogOptionButton(internalOptionNumber,option,isDisabled,previewRelic));
		}else{imageEventText.optionList.add(new LargeDialogOptionButton(internalOptionNumber,option,isDisabled,previewRelic));}
	}else{imageEventText.optionList.add(new LargeDialogOptionButton(internalOptionNumber,option,isDisabled,previewRelic));}
	internalOptionNumber++;}
public void upOption(String option,AbstractCard previewCard,AbstractRelic previewRelic){
	if(!imageEventText.optionList.isEmpty()){
		if(imageEventText.optionList.size()>internalOptionNumber){
			imageEventText.optionList.set(internalOptionNumber,new LargeDialogOptionButton(internalOptionNumber,option,false,previewCard,previewRelic));
		}else{imageEventText.optionList.add(new LargeDialogOptionButton(internalOptionNumber,option,false,previewCard,previewRelic));}
	}else{imageEventText.optionList.add(new LargeDialogOptionButton(internalOptionNumber,option,false,previewCard,previewRelic));}
	internalOptionNumber++;}
public void upOption(String option,boolean isDisabled,AbstractCard previewCard,AbstractRelic previewRelic){
	CosmicZoneMod.logger.info("up option"+option);
	if(!imageEventText.optionList.isEmpty()){
		if(imageEventText.optionList.size()>internalOptionNumber){CosmicZoneMod.logger.info("A"+internalOptionNumber);
			imageEventText.optionList.set(internalOptionNumber,new LargeDialogOptionButton(internalOptionNumber,option,isDisabled,previewCard,previewRelic));
		}else{CosmicZoneMod.logger.info("B"+internalOptionNumber);imageEventText.optionList.add(new LargeDialogOptionButton(internalOptionNumber,option,isDisabled,previewCard,previewRelic));}
	}else{CosmicZoneMod.logger.info("C"+internalOptionNumber);imageEventText.optionList.add(new LargeDialogOptionButton(internalOptionNumber,option,isDisabled,previewCard,previewRelic));}
	internalOptionNumber++;}

public void upOption(CZMEOption option){upOption(option.title,option.disabled,option.previewCard,option.previewRelic);}

public void exerciseOption(String opI){
	for(CZMEOption option:optionList){if(option.identifier.equals(opI)){
		if(!option.des.isEmpty()){upText(option.des);}upScreenFOList(option.optionsStrings);part=option.nextPart;
		screenNum=option.nextScreenNum;screenFunc=option.nextScreenFunc;activeNum=option.nextActiveNum;
		if(!option.nextScn.isEmpty()){scn=option.nextScn;}if(!option.nextimgurl.isEmpty()){imageEventText.loadImage(option.nextimgurl);}
		if(option.willFinish){compEv();}break;}}}//This uses the option to do things (Is not the next option)

/*
public void upScreenFOList(String[]opIs){for(String opI:opIs){for(CZMEOption option:optionList){if(option.identifier.equals(opI)){
	CosmicZoneMod.logger.info("upCZME'd (Identifier:) "+opI);upCZMEOption(option);break;}}}clrRemOps();}//Adds new options
*/

public void upScreenFOList(String[]opIs){this.imageEventText.clearAllDialogs();for(String opI:opIs){for(CZMEOption option:optionList){if(option.identifier.equals(opI)){setCZMEOption(option);break;}}}}//Adds new options



public void upCZMEOption(CZMEOption option){upOption(option.title,option.disabled,option.previewCard,option.previewRelic);}
public void upCZMEOption(String opI){for(CZMEOption option:optionList){if(option.identifier.equals(opI)){upOption(option.title,option.disabled,option.previewCard,option.previewRelic);break;}}}

/*public void exerciseOption(CZMEOption option){exerciseOption(option,option.options);}
public void exerciseOption(CZMEOption option,CZMEOption[]nextOptions){
	if(!option.des.isEmpty()){upText(option.des);}
	if(option.willFinish){upScreen(option.des,nextOptions,option.nextPart);}else{endScreen(option.des,nextOptions,option.nextPart);}
	screenNum=option.nextScreenNum;screenFunc=option.nextScreenFunc;activeNum=option.nextActiveNum;
	if(!option.nextScn.isEmpty()){scn=option.nextScn;}
	if(!option.nextimgurl.isEmpty()){imageEventText.loadImage(option.nextimgurl);}
}*/



public void clrRemOps(){this.imageEventText.clearRemainingOptions();internalOptionNumber=0;}

public void newRewardScrn(RewardItem reward){clearRewards();addReward(reward);openRewards();}
public void newRewardScrn(RewardItem[]rewards){clearRewards();for(RewardItem ri:rewards){addReward(ri);}openRewards();}
public void addToRewards(RewardItem[]rewards){for(RewardItem ri:rewards){addReward(ri);}}
public void addAndOpenRewardScrn(RewardItem[]rewards){for(RewardItem ri:rewards){addReward(ri);}openRewards();}

public void upScreen(String bodyText,String[]options,String nextPart){upText(bodyText);for(String si:options){upOption(si);}clrRemOps();if(!nextPart.isEmpty()){part=nextPart;}}
public void upScreen(String bodyText,CZMEOption[]options,String nextPart){upText(bodyText);for(CZMEOption si:options){upOption(si);}clrRemOps();if(!nextPart.isEmpty()){part=nextPart;}}
public void endScreen(String bodyText,String[]options,String nextPart){upScreen(bodyText,options,nextPart);compEv();}
public void endScreen(String bodyText,CZMEOption[]options,String nextPart){upScreen(bodyText,options,nextPart);compEv();}

public void upScreen(String bodyText,String[]options,boolean[]disableds,String nextPart){upText(bodyText);for(String si:options){upOption(si);}clrRemOps();if(!nextPart.isEmpty()){part=nextPart;}}



public void loseHp(int hp){AbstractDungeon.player.damage(new DamageInfo((AbstractCreature)null,hp));}
public void heal(int hp){AbstractDungeon.player.heal(hp);}
public void incMaxHp(int hp){AbstractDungeon.player.increaseMaxHp(hp,false);}
public void loseMaxHp(int hp){AbstractDungeon.player.decreaseMaxHealth(hp);}
public void gainGold(int gold){AbstractDungeon.player.gainGold(gold);}public void loseGold(int gold){AbstractDungeon.player.loseGold(gold);}
public void clearRewards(){AbstractDungeon.getCurrRoom().rewards.clear();}
public void addReward(RewardItem rew){AbstractDungeon.getCurrRoom().rewards.add(rew);}
public void compEv(){AbstractDungeon.getCurrRoom().phase=AbstractRoom.RoomPhase.COMPLETE;}
public void openRewards(){AbstractDungeon.combatRewardScreen.open();}

public void openGrid(){if(goi.size()>0){AbstractDungeon.gridSelectScreen.open(goi,1,"TODO, Text",false);}else{AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck,1,"TODO, Text",false);}}public void openGrid(int am,String text){if(goi.size()>0){AbstractDungeon.gridSelectScreen.open(goi,am,text,false);}else{AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck,am,text,false);}}
public void openGrid(CardGroup go,String text){AbstractDungeon.gridSelectScreen.open(go,1,text,false);}public void openGrid(int am,CardGroup go,String text){AbstractDungeon.gridSelectScreen.open(go,am,text,false);}
public void openMD(){AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck,1,"TODO, Text",false);}public void openMD(int am,String text){AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck,am,text,false);}
public void openGridOp(){if(goi.size()>0){AbstractDungeon.gridSelectScreen.open(goi,1,"TODO, Text",false,false,true,false);}else{AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck,1,"TODO, Text",false,false,true,false);}}public void openGridOp(int am,String text){if(goi.size()>0){AbstractDungeon.gridSelectScreen.open(goi,am,text,false,false,true,false);}else{AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck,am,text,false,false,true,false);}}
public void openGridOp(CardGroup go,String text){AbstractDungeon.gridSelectScreen.open(go,1,text,false,false,true,false);}public void openGridOp(int am,CardGroup go,String text){AbstractDungeon.gridSelectScreen.open(go,am,text,false,false,true,false);}
public void openMDOp(){AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck,1,"TODO, Text",false,false,true,false);}public void openMDOp(int am,String text){AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck,am,text,false,false,true,false);}
public CardGroup purgablMD(){return getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).getPurgeableCards();}

public void clearGrid(){AbstractDungeon.gridSelectScreen.selectedCards.clear();goi.clear();}public void clearOnlyGrid(){AbstractDungeon.gridSelectScreen.selectedCards.clear();}
public void obtainCard(AbstractCard c){AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c,(float)Settings.WIDTH/2.0F,(float)Settings.HEIGHT/2.0F));}
public void obtainCard(AbstractCard c,float x,float y){AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c,x,y));}
public void dupliCard(AbstractCard c){AbstractCard ce=c.makeStatEquivalentCopy();ce.inBottleFlame=false;ce.inBottleLightning=false;ce.inBottleTornado=false;AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(ce,(float)Settings.WIDTH/2.0F,(float)Settings.HEIGHT/2.0F));}
public void dupliCard(AbstractCard c,float x,float y){AbstractCard ce=c.makeStatEquivalentCopy();ce.inBottleFlame=false;ce.inBottleLightning=false;ce.inBottleTornado=false;AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(ce,x,y));}
public void removeCard(AbstractCard c){AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c,(float)(Settings.WIDTH/2),(float)(Settings.HEIGHT/2)));AbstractDungeon.player.masterDeck.removeCard(c);}
public void removeCard(AbstractCard c,float x,float y){AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c,x,y));AbstractDungeon.player.masterDeck.removeCard(c);}
public void transformCard(AbstractCard c){AbstractDungeon.player.masterDeck.removeCard(c);AbstractDungeon.transformCard(c,false,AbstractDungeon.miscRng);AbstractCard transCard=AbstractDungeon.getTransformedCard();obtainCard(transCard);}
public void transformCard(AbstractCard c,float x,float y){AbstractDungeon.player.masterDeck.removeCard(c);AbstractDungeon.transformCard(c,false,AbstractDungeon.miscRng);AbstractCard transCard=AbstractDungeon.getTransformedCard();obtainCard(transCard,x,y);}
public void upgradeRandomCards(int amount){ArrayList<AbstractCard>upgradableCards=new ArrayList<>();
	for(AbstractCard c:AbstractDungeon.player.masterDeck.group){if(c.canUpgrade()){upgradableCards.add(c);}}
	Collections.shuffle(upgradableCards,new Random(AbstractDungeon.miscRng.randomLong()));
	if(!upgradableCards.isEmpty()){for(int i=0;i<amount;i++){upgradableCards.get(0).upgrade();AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
			AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy()));AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH/2.0F,(float)Settings.HEIGHT/2.0F));
		}}}
public void upgradeRandomCards(int amount,float x,float y){ArrayList<AbstractCard>upgradableCards=new ArrayList<>();
	for(AbstractCard c:AbstractDungeon.player.masterDeck.group){if(c.canUpgrade()){upgradableCards.add(c);}}
	Collections.shuffle(upgradableCards,new Random(AbstractDungeon.miscRng.randomLong()));
	if(!upgradableCards.isEmpty()){for(int i=0;i<amount;i++){upgradableCards.get(0).upgrade();AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
		AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy()));AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x,y));
	}}}
//public void obtainRelic(AbstractRelic relic){AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);}public void obtainRelic(AbstractRelic relic,float x,float y){AbstractDungeon.getCurrRoom().spawnRelicAndObtain(x,y, relic);}
public AbstractRelic randomRelic(){return AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());}
public void addRandomCardsToGoi(int am){
	for(int we=0;we<am;++we){AbstractCard card=AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();boolean containsDupe=true;
		while(true){while(containsDupe){containsDupe=false;
			for(AbstractCard c:goi.group){
				if(c.cardID.equals(card.cardID)){containsDupe=true;card=AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();break;}}}
			if(goi.contains(card)){--we;}else{for(AbstractRelic r:AbstractDungeon.player.relics){r.onPreviewObtainCard(card);}goi.addToBottom(card);}
			break;}}}

public CardGroup xRanCardsFCGroup(int amount){if(goi.size()>0){return xRanCardsFCGroup(amount,goi);}else{return xRanCardsFCGroup(amount,AbstractDungeon.player.masterDeck);}}
public CardGroup xRanCardsFCGroup(int amount,CardGroup go){amount=Math.min(amount,go.size());CardGroup b=new CardGroup(UNSPECIFIED);ArrayList<Integer>d=new ArrayList<>();
	for(int i=0;i<go.size();i++){d.add(i);}Collections.shuffle(d,new java.util.Random(AbstractDungeon.shuffleRng.randomLong()));
	for(int i=0;i<amount;i++){b.addToBottom(go.group.get(d.get(i)));}return b;
	
	
	/*amount=Math.min(amount,go.size());CardGroup v=go;CardGroup b=new CardGroup(UNSPECIFIED);
	for(int i=0;i<amount;i++){
		int e=AbstractDungeon.miscRng.random(v.size()-b.size()-1);
		b.addToBottom(v.group.get(e));
		Collections.swap(v.group,e,v.size()-b.size()-2);
	}return b;*/}

public void evAddMod(AbstractCard c,AbstractCardModifier mod){CardModifierManager.addModifier(c,mod);}
	






}
