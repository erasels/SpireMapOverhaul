package spireMapOverhaul.zones.CosmicEukotranpha.util;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.LoseHPEffectCosmicZone;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.*;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.Frames.CosmicZoneTargetTopCardsOfDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
public abstract class CosmicShortcuts{
AbstractCard ciu=AbstractDungeon.player.cardInUse;
CardGroup dck=AbstractDungeon.player.drawPile;ArrayList<AbstractCard>dckg=dck.group;
CardGroup hnd=AbstractDungeon.player.hand;ArrayList<AbstractCard>hndg=hnd.group;
CardGroup dp=AbstractDungeon.player.discardPile;ArrayList<AbstractCard>dpg=dp.group;
CardGroup ep=AbstractDungeon.player.exhaustPile;ArrayList<AbstractCard>epg=ep.group;
CardGroup lmb=AbstractDungeon.player.limbo;ArrayList<AbstractCard>lmbg=lmb.group;
CardGroup mdck=AbstractDungeon.player.masterDeck;ArrayList<AbstractCard>mdeckg=mdck.group;
public static int asc=AbstractDungeon.ascensionLevel;
public static void addToBot(AbstractGameAction action){AbstractDungeon.actionManager.addToBottom(action);}public static void addToTop(AbstractGameAction action){AbstractDungeon.actionManager.addToTop(action);}
public static void atb(AbstractGameAction action){addToBot(action);}public static void att(AbstractGameAction action){addToTop(action);}
public static void makeInHand(AbstractCard c,int i){atb((AbstractGameAction)new MakeTempCardInHandAction(c,i));}public static void makeInHand(AbstractCard c){makeInHand(c,1);}
public static void makeInDeck(AbstractCard c){makeInDeck(c,1,0);}public static void makeInDeck(AbstractCard c,int i){makeInDeck(c,i,0);}public static void makeInDeck(AbstractCard c,int i,int key){switch(key){
    case 1:atb(new MakeTempCardInDrawPileAction(c,i,false,true));break;
    case 2:atb(new MakeTempCardInDrawPileAction(c,i,false,false,true));break;
    default:atb(new MakeTempCardInDrawPileAction(c,i,true,false));}}
public static void makeInDp(AbstractCard c){atb(new MakeTempCardInDiscardAction(c,1));}public static void makeInDp(AbstractCard c,int i){atb(new MakeTempCardInDiscardAction(c,i));}
public static AbstractCard randomCosmicCardWeightedRNG(){return new Strike_Red();}//TODO: Not red strike
public static ArrayList<AbstractMonster>monsterList(){ArrayList<AbstractMonster>q=new ArrayList<>();for(AbstractMonster m:(AbstractDungeon.getMonsters()).monsters){if(!m.isDeadOrEscaped()){q.add(m);}}return q;}
public static void poT(AbstractCreature t,AbstractCreature s,AbstractPower po){if(po.amount!=0){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)t,(AbstractCreature)s,po,po.amount,true));}}
public static void poP(AbstractPower po){if(po.amount!=0){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)AbstractDungeon.player,(AbstractCreature)AbstractDungeon.player,po,po.amount,true));}}
//public void poFs(AbstractPower po){if(po.amount!=0){for(AbstractMonster mo:monsterList()){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)mo,(AbstractCreature)AbstractDungeon.player,po,po.amount,true));}}}
//public void poFCanApplyZero(AbstractMonster m,AbstractPower po){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m,(AbstractCreature)AbstractDungeon.player,po,po.amount,true));}
//public void poSCanApplyZero(AbstractPower po){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)AbstractDungeon.player,(AbstractCreature)AbstractDungeon.player,po,po.amount,true));}
//public void poFsCanApplyZero(AbstractPower po){for(AbstractMonster mo:monsterList()){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)mo,(AbstractCreature)AbstractDungeon.player,po,po.amount,true));}}
//public void poA(AbstractPower po){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)AbstractDungeon.player,(AbstractCreature)AbstractDungeon.player,po,po.amount,true));
//    for(AbstractMonster mo:monsterList()){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)mo,(AbstractCreature)AbstractDungeon.player,po,po.amount,true));}}
public static void choice(AbstractCard[]co){ArrayList<AbstractCard>Ch=new ArrayList<>();Collections.addAll(Ch,co);atb(new ChooseOneAction(Ch));}
public static void choice(ArrayList<AbstractCard>co){atb(new ChooseOneAction(co));}
public static void choiceAP(AbstractCard[]co){ArrayList<AbstractCard>Ch=new ArrayList<>();Collections.addAll(Ch,co);for(AbstractCard c:co){c.applyPowers();}atb(new ChooseOneAction(Ch));}
public static void choiceAP(ArrayList<AbstractCard>co){for(AbstractCard c:co){c.applyPowers();}atb(new ChooseOneAction(co));}
public static void loseHP(AbstractCreature t,int am){atb(new LoseHPEffectCosmicZone(t,AbstractDungeon.player,am));}

public static void scry(int am){if(am>0)atb(new ScryAction(am));}public static void draw(int am){atb(new DrawCardAction(am));}public static void retain(int am){atb(new CosmicZoneRetainEffect(am));}
public static void mill(int am){atb(new CosmicZoneTargetTopCardsOfDeck(am,new CZDiscardTEffect(null)));}
public static void pivotDpRandom(int am){atb(new CosmicZonePivotDpEffect(am,false,true));}
public static void seek(int am){atb(new CosmicZoneSeekEffect(am));}
public static void discard(int am){atb(new CosmicZoneDiscardEffect(am));}public static void discardOp(int am){atb(new CosmicZoneDiscardEffect(am,false,true));}
public static void discard(CardGroup cardGroup,int am){atb(new CosmicZoneDiscardCardGroupEffect(cardGroup,am));}public static void discardOp(CardGroup cardGroup,int am){atb(new CosmicZoneDiscardCardGroupEffect(cardGroup,am,false,true));}


public static void pivotT(AbstractCard card){atb(new CZPivotTEffect(card));}
public static void discardT(AbstractCard card){atb(new CZDiscardTEffect(card));}
public static void exhaustT(AbstractCard card){atb(new CZExhaustTEffect(card));}

public static void pivotDP(int am){atb(new CosmicZonePivotDpEffect(am));}public static void pivotDPOp(int am){atb(new CosmicZonePivotDpEffect(am,true));}
/*
public void seek(int am){atb(new SeekEffectUnhanged(am));}public void seekOp(int am){atb(new SeekOptionalEffect(am));}
public void fetch(int am){atb(new FetchEffectUnhanged(am));}public void fetchOp(int am){atb(new FetchEffectUnhanged(am,true,true));}
public void exhume(int am){atb(new ExhumeEffect(am,false));}public void exhumeOp(int am){atb(new ExhumeEffect(am,true));}
public void cycle(int am){atb(new CycleEffect(am));}public void cycleOp(int am){atb(new CycleEffect(am,false,true));}
public void pivot(int am){atb(new PivotEffectUnhanged(am));}public void pivotOp(int am){atb(new PivotEffectUnhanged(am,false,true));}
public void pivotEP(int am){atb(new PivotEpEffect(am,false));}public void pivotEPOp(int am, boolean optional){atb(new PivotEpEffect(am,true));}
public void spin(int am){atb(new SpinEffectUnhanged(am));}public void spinOp(int am){atb(new SpinEffectUnhanged(am,false,true));}
public void spinDeck(int am){atb(new SpinDeckEffectUnhanged(am));}public void spinDeckOp(int am){atb(new SpinDeckEffectUnhanged(am,true));}
public void spinDp(int am){atb(new SpinDpEffectUnhanged(am));}public void spinDpOp(int am){atb(new SpinDpEffectUnhanged(am,true));}
public void rotate(int am){atb(new RotateEffect(am));}public void rotateOp(int am){atb(new RotateEffect(am,true));}
public void rotateDeck(int am){atb(new RotateDeckEffect(am));}
public void rotateDP(int am){atb(new RotateDPEffect(am));}public void rotateDPOp(int am){atb(new RotateDPEffect(am,true));}
public void discard(int am){atb(new DiscardEffectUnhanged(am));}public void discard(int am,boolean op){atb(new DiscardEffectUnhanged(am,false,op));}
public void discardDeck(int am){atb(new DiscardDeckEffectUnhanged(am));}public void discardDeck(int am,boolean optional){atb(new DiscardDeckEffectUnhanged(am,optional));}
public void exhaust(int am){atb(new ExhaustEffectUnhanged(am));}public void exhaust(int am,boolean optional){atb(new ExhaustEffectUnhanged(am,false,optional));}
public void exhaustDP(int am){atb(new ExhaustInDPEffect(am));}public void exhaustDPOp(int am){atb(new ExhaustInDPEffect(am,true));}
public void exhaustDeck(int am){atb(new ExhaustInDeckEffect(am));}public void exhaustDeckOp(int am){atb(new ExhaustInDeckEffect(am,true));}
public void discardT(AbstractCard c){atb(new DiscardTargetEffect(c));}
public void exhaustT(AbstractCard c){atb(new ExhaustTargetCardEffect(c));}
public void exhumeT(AbstractCard c){atb(new ExhumeTargetEffect(c));}
public void fetchT(AbstractCard c){atb(new FetchTargetEffect(c));}
public void maintainT(AbstractCard c){atb(new MaintainTargetEffect(c));}
public void pivotT(AbstractCard c){atb(new PivotTarget(c));}
public void seekT(AbstractCard c){atb(new SeekTargetEffect(c));}
public void spinT(AbstractCard c){atb(new SpinTarget(c));}
public void rotateT(AbstractCard c){atb(new RotateTarget(c));}
public void discardR(int am, CardGroup co){atb(new DiscardRestrictedEffect(co,am));}
//public void exhaustR(int am,CardGroup co){atb(new DiscardRestrictedEffect(co,am));}
//public void exhumeR(int am,CardGroup co){atb(new DiscardRestrictedEffect(co,am));}
public void fetchR(int am,CardGroup co){atb(new FetchRestrictedEffectUnhanged(am,co));}
//public void maintainR(int am,CardGroup co){atb(new DiscardRestrictedEffect(co,am));}
//public void pivotR(int am,CardGroup co){atb(new DiscardRestrictedEffect(co,am));}
//public void seekR(int am,CardGroup co){atb(new DiscardRestrictedEffect(co,am));}
//public void spinR(int am,CardGroup co){atb(new DiscardRestrictedEffect(co,am));}
//public void rotateR(int am,CardGroup co){atb(new DiscardRestrictedEffect(co,am));}
*/
public static int rBAI(boolean k){if(k){return 1;}else{return 0;}}public static int rBAI(boolean k,int magnif){if(k){return magnif;}else{return 0;}}//returnBooleanAsInt
/*
public void ccigtwe(int amount,CardGroup tmp,AmalgamAbstractGameAction action){atb(new CcigTwE(amount,tmp,action,1));}public void ccigtweAll(int amount,AmalgamAbstractGameAction action){atb(new CcigTwE(amount,AbstractDungeon.player.hand,action,1,1));}public void ccigtweOp(int amount,CardGroup tmp,AmalgamAbstractGameAction action){atb(new CcigTwE(amount,tmp,action,0));}public void ccigtweAllOp(int amount,AmalgamAbstractGameAction action){atb(new CcigTwE(amount,AbstractDungeon.player.hand,action,0,1));}
public void ccihtwe(int amount,AmalgamAbstractGameAction action){atb(new CcihTwE(amount,action,1));}public void ccihtweOp(int amount,AmalgamAbstractGameAction action){atb(new CcihTwE(amount,action,0));}
public void ccihtwe(int amount,AmalgamAbstractGameAction action,String[]txt){atb(new CcihTwE(amount,action,1,txt));}public void ccihtweOp(int amount,AmalgamAbstractGameAction action,String[]txt){atb(new CcihTwE(amount,action,0,txt));}
public void cycle(int am,int draa){atb(new CcihTwEtdE(am,new AmDiscardTargetEffect(null),new DrawCardAction(0),1,draa));}public void cycle(int am,int draa,String[]txt){atb(new CcihTwEtdE(am,new AmDiscardTargetEffect(null),new DrawCardAction(0),1,draa,txt));}
public void ccigtwetde(int amount,CardGroup tmp,AmalgamAbstractGameAction action,AbstractGameAction action2,int optional,int magnification){atb(new CcigTwEtdE(amount,tmp,action,action2,optional,magnification));}
public void ccigtwetde(int amount,CardGroup tmp,AmalgamAbstractGameAction action,AbstractGameAction action2,int optional,int magnification,String[]txt){atb(new CcigTwEtdE(amount,tmp,action,action2,optional,magnification,txt));}
public void ccihtwetde(int amount,AmalgamAbstractGameAction action,AbstractGameAction action2,int optional,int magnification){atb(new CcihTwEtdE(amount,action,action2,optional,magnification));}
public void ccihtwetde(int amount,AmalgamAbstractGameAction action,AbstractGameAction action2,int optional,int magnification,String[]txt){atb(new CcihTwEtdE(amount,action,action2,optional,magnification,txt));}
*/
public static AbstractCard getTopOfDeck(){if(!AbstractDungeon.player.drawPile.isEmpty()){return AbstractDungeon.player.drawPile.getTopCard();}return null;}
public static int atksInGroup(CardGroup c0){int i=0;for(AbstractCard c:c0.group){if(c.type== AbstractCard.CardType.ATTACK){i++;}}return i;}public static int atksInGroup(ArrayList<AbstractCard> c0){int i=0;for(AbstractCard c:c0){if(c.type== AbstractCard.CardType.ATTACK){i++;}}return i;}
public static int sklsInGroup(CardGroup c0){int i=0;for(AbstractCard c:c0.group){if(c.type== AbstractCard.CardType.SKILL){i++;}}return i;}public static int sklsInGroup(ArrayList<AbstractCard> c0){int i=0;for(AbstractCard c:c0){if(c.type== AbstractCard.CardType.SKILL){i++;}}return i;}
public static int powsInGroup(CardGroup c0){int i=0;for(AbstractCard c:c0.group){if(c.type== AbstractCard.CardType.POWER){i++;}}return i;}public static int powsInGroup(ArrayList<AbstractCard> c0){int i=0;for(AbstractCard c:c0){if(c.type== AbstractCard.CardType.POWER){i++;}}return i;}
public static int stasInGroup(CardGroup c0){int i=0;for(AbstractCard c:c0.group){if(c.type== AbstractCard.CardType.STATUS){i++;}}return i;}public static int stasInGroup(ArrayList<AbstractCard> c0){int i=0;for(AbstractCard c:c0){if(c.type== AbstractCard.CardType.STATUS){i++;}}return i;}
public static int cursInGroup(CardGroup c0){int i=0;for(AbstractCard c:c0.group){if(c.type== AbstractCard.CardType.CURSE){i++;}}return i;}public static int cursInGroup(ArrayList<AbstractCard> c0){int i=0;for(AbstractCard c:c0){if(c.type== AbstractCard.CardType.CURSE){i++;}}return i;}
public static int nonAtksInGroup(CardGroup c0){int i=0;for(AbstractCard c:c0.group){if(c.type!= AbstractCard.CardType.ATTACK){i++;}}return i;}public static int nonAtksInGroup(ArrayList<AbstractCard>c0){int i=0;for(AbstractCard c:c0){if(c.type!= AbstractCard.CardType.ATTACK){i++;}}return i;}
public static ArrayList<AbstractCard.CardType>typesInGroup(CardGroup c0){ArrayList<AbstractCard.CardType>co=new ArrayList<>();for(AbstractCard c:c0.group){co.add(c.type);}return co;}public static ArrayList<AbstractCard.CardType>typesInGroup(ArrayList<AbstractCard>c0){ArrayList<AbstractCard.CardType>co=new ArrayList<>();for(AbstractCard c:c0){co.add(c.type);}return co;}
public static ArrayList<AbstractCard.CardType>uniqueTypesInGroup(CardGroup c0){ArrayList<AbstractCard.CardType>co=new ArrayList<>();for(AbstractCard c:c0.group){if(!co.contains(c.type)){co.add(c.type);}}return co;}public static ArrayList<AbstractCard.CardType>uniqueTypesInGroup(ArrayList<AbstractCard>c0){ArrayList<AbstractCard.CardType>co=new ArrayList<>();for(AbstractCard c:c0){if(!co.contains(c.type)){co.add(c.type);}}return co;}
public static ArrayList<AbstractCard.CardRarity>uniqueRaritiesInGroup(CardGroup c0){ArrayList<AbstractCard.CardRarity>co=new ArrayList<>();for(AbstractCard c:c0.group){if(!co.contains(c.rarity)){co.add(c.rarity);}}return co;}public static ArrayList<AbstractCard.CardRarity>uniqueRaritiesInGroup(ArrayList<AbstractCard>c0){ArrayList<AbstractCard.CardRarity>co=new ArrayList<>();for(AbstractCard c:c0){if(!co.contains(c.rarity)){co.add(c.rarity);}}return co;}
public static ArrayList<Integer>uniqueCostsInGroup(CardGroup c0){ArrayList<Integer>co=new ArrayList<>();for(AbstractCard c:c0.group){if(!co.contains(c.costForTurn)){co.add(c.costForTurn);}}return co;}public static ArrayList<Integer>uniqueCostsInGroup(ArrayList<AbstractCard>c0){ArrayList<Integer>co=new ArrayList<>();for(AbstractCard c:c0){if(!co.contains(c.costForTurn)){co.add(c.costForTurn);}}return co;}
public static int costOfGroup(CardGroup c0){int co=0;for(AbstractCard c:c0.group){co+=c.costForTurn;}return co;}public static int costOfGroup(ArrayList<AbstractCard>c0){int co=0;for(AbstractCard c:c0){co+=c.costForTurn;}return co;}
public static int TCRInGroup(CardGroup c0, AbstractCard.CardType type, int cost, AbstractCard.CardRarity rarity){int i=0;for(AbstractCard c:c0.group){int vvvvvv=0;if(type!=null){if(c.type==type){vvvvvv++;}}else{vvvvvv++;}if(cost!=99){if(c.costForTurn==cost){vvvvvv++;}}else{vvvvvv++;}if(rarity!=null){if(c.rarity==rarity){vvvvvv++;}}else{vvvvvv++;}if(vvvvvv==3){i++;}}return i;}
public static CardGroup getTCRInGroup(CardGroup c0, AbstractCard.CardType type, int cost, AbstractCard.CardRarity rarity){CardGroup e=new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);for(AbstractCard c:c0.group){int vvvvvv=0;if(type!=null){if(c.type==type){vvvvvv++;}}else{vvvvvv++;}if(cost!=99){if(c.costForTurn==cost){vvvvvv++;}}else{vvvvvv++;}if(rarity!=null){if(c.rarity==rarity){vvvvvv++;}}else{vvvvvv++;}if(vvvvvv==3){e.addToBottom(c);}}return e;}
public static int getNoOfCostInGroup(CardGroup c0,int costToGet){int i=0;for(AbstractCard c:c0.group){if(c.costForTurn==costToGet){i++;}}return i;}public static int getNoOfCostInGroup(ArrayList<AbstractCard>c0,int costToGet){int i=0;for(AbstractCard c:c0){if(c.costForTurn==costToGet){i++;}}return i;}
public static ArrayList<AbstractCard>cardsPlayedThisTurn(){return AbstractDungeon.actionManager.cardsPlayedThisTurn;}
//public ArrayList<AbstractCard>cardsPlayedLastTurn(){if(GameActionManager.turn>0&&AmalgamGameActionHistory.CardsPlayedLastCombatEachTurn.size()>0){if(AmalgamGameActionHistory.CardsPlayedLastCombatEachTurn.get(AmalgamGameActionHistory.CardsPlayedLastCombatEachTurn.size()-1)!=null){return AmalgamGameActionHistory.CardsPlayedLastCombatEachTurn.get(AmalgamGameActionHistory.CardsPlayedLastCombatEachTurn.size()-1);}else{return new ArrayList<AbstractCard>();}}else{return new ArrayList<AbstractCard>();}}
//public ArrayList<AbstractCard>cardsPlayedLastTurn(){if(GameActionManager.turn>1){return AmalgamGameActionHistory.CardsPlayedInCombatEachTurn.get(GameActionManager.turn-2);}else{return new ArrayList<AbstractCard>();}}
public static CardGroup getTXCoCG(int cards,CardGroup co){if(co.size()<=cards){return co;}else{CardGroup go=new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);for(int i=0;i<cards;i++){go.addToTop(co.getNCardFromTop(co.size()+1-i));}return go;}}//getTopXCardsOfCardGroup
public static void addModT(AbstractCard card,AbstractCardModifier modifier){atb(new CZAddModTEffect(card,modifier));}
public static AbstractPower getLowestPower(AbstractCreature c){AbstractPower pow=null;int l=0;for(AbstractPower po:c.powers){if(pow==null || l>po.amount){pow=po;l=po.amount;}}return pow;}
public static AbstractPower getHighestPower(AbstractCreature c){AbstractPower pow=null;int l=0;for(AbstractPower po:c.powers){if(pow==null || l<po.amount){pow=po;l=po.amount;}}return pow;}
public static AbstractPower getLowestBuff(AbstractCreature c){AbstractPower pow=null;int l=0;for(AbstractPower po:c.powers){if(po.type==AbstractPower.PowerType.BUFF && (pow==null || l>po.amount)){pow=po;l=po.amount;}}return pow;}
public static AbstractPower getHighestBuff(AbstractCreature c){AbstractPower pow=null;int l=0;for(AbstractPower po:c.powers){if(po.type==AbstractPower.PowerType.BUFF && (pow==null || l<po.amount)){pow=po;l=po.amount;}}return pow;}
public static AbstractPower getHighestNonInvincibleBuff(AbstractCreature c){AbstractPower pow=null;int l=0;for(AbstractPower po:c.powers){if(po.type==AbstractPower.PowerType.BUFF && (pow==null || l<po.amount)&&!(po instanceof InvinciblePower)){pow=po;l=po.amount;}}return pow;}//So Specific
public static AbstractPower getLowestDebuff(AbstractCreature c){AbstractPower pow=null;int l=0;for(AbstractPower po:c.powers){if(po.type==AbstractPower.PowerType.DEBUFF && (pow==null || l>po.amount)){pow=po;l=po.amount;}}return pow;}
public static AbstractPower getHighestDebuff(AbstractCreature c){AbstractPower pow=null;int l=0;for(AbstractPower po:c.powers){if(po.type==AbstractPower.PowerType.DEBUFF && (pow==null || l<po.amount)){pow=po;l=po.amount;}}return pow;}
public static AbstractPower getAbsHighestDebuff(AbstractCreature c){AbstractPower pow=null;int l=0;for(AbstractPower po:c.powers){if(po.type==AbstractPower.PowerType.DEBUFF && (pow==null || l<Math.abs(po.amount))){pow=po;l=Math.abs(po.amount);}}return pow;}
public static AbstractCard getMasterDeckEquivalent(AbstractCard playingCard){Iterator var1=AbstractDungeon.player.masterDeck.group.iterator();AbstractCard c;do{if(!var1.hasNext()){return null;}c=(AbstractCard)var1.next();}while(!c.uuid.equals(playingCard.uuid));return c;}
public static CardGroup arrayToCG(ArrayList<AbstractCard>Ch){CardGroup Ci=new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);for(AbstractCard c:Ch){Ci.addToBottom(c);}return Ci;}
public static AbstractMonster getRanMon(){return AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster)null,true,AbstractDungeon.cardRandomRng);}
public static boolean hasPowAmount(AbstractCreature owner,String ID,int amount){return hasPowAmount(owner,ID,amount,false);}
public static boolean hasPowAmount(AbstractCreature o,String ID,int am,boolean orLower){if(orLower){return o.hasPower(ID)&&o.getPower(ID).amount<=am;}else{return o.hasPower(ID)&&o.getPower(ID).amount>=am;}}
public static int getPowAmount(AbstractCreature owner,String ID){if(owner.hasPower(ID)){return owner.getPower(ID).amount;}else{return 0;}}
public static int getAbsDebuffAmount(AbstractCreature c){int l=0;for(AbstractPower po:c.powers){if(po.type==AbstractPower.PowerType.DEBUFF){l+=Math.abs(po.amount);}}return l;}

public static void loggeer(String log){CosmicZoneMod.logger.info(log);}
public String getCardString(String ID,int key,int index){switch(key){
    case 1:return CardCrawlGame.languagePack.getCardStrings(SpireAnniversary6Mod.makeID(ID)).DESCRIPTION;
    case 2:return CardCrawlGame.languagePack.getCardStrings(SpireAnniversary6Mod.makeID(ID)).UPGRADE_DESCRIPTION;
    case 3:return CardCrawlGame.languagePack.getCardStrings(SpireAnniversary6Mod.makeID(ID)).EXTENDED_DESCRIPTION[index];
    default:return CardCrawlGame.languagePack.getCardStrings(SpireAnniversary6Mod.makeID(ID)).NAME;}}
public String getStringString(String ID,int type,int key,int index){switch(type){
    //0:Card, 1:Keyword, 2:Power, 3:Relic, 4:Potion,
    //5:Monster, 6:Event, 7:Character, 8:Orb, 9:Stance
    //10:UI, 11:Blight, 12:Mod, 13:Tutorial, 14:ScoreBonuses
    //15:Achievements, 16:Credits
    case 0:switch(key){case 0:return CardCrawlGame.languagePack.getCardStrings(SpireAnniversary6Mod.makeID(ID)).NAME;
        case 1:return CardCrawlGame.languagePack.getCardStrings(SpireAnniversary6Mod.makeID(ID)).DESCRIPTION;
        case 2:return CardCrawlGame.languagePack.getCardStrings(SpireAnniversary6Mod.makeID(ID)).UPGRADE_DESCRIPTION;
        case 3:return CardCrawlGame.languagePack.getCardStrings(SpireAnniversary6Mod.makeID(ID)).EXTENDED_DESCRIPTION[index];}
    case 1:return CardCrawlGame.languagePack.getKeywordString(SpireAnniversary6Mod.makeID(ID)).TEXT[index];
    case 2:switch(key){case 0:return CardCrawlGame.languagePack.getPowerStrings(SpireAnniversary6Mod.makeID(ID)).NAME;
        case 1:return CardCrawlGame.languagePack.getPowerStrings(SpireAnniversary6Mod.makeID(ID)).DESCRIPTIONS[index];}
    case 3:switch(key){case 0:return CardCrawlGame.languagePack.getRelicStrings(SpireAnniversary6Mod.makeID(ID)).NAME;
        case 1:return CardCrawlGame.languagePack.getRelicStrings(SpireAnniversary6Mod.makeID(ID)).DESCRIPTIONS[index];
        case 2:return CardCrawlGame.languagePack.getRelicStrings(SpireAnniversary6Mod.makeID(ID)).FLAVOR;}
    case 4:switch(key){case 0:return CardCrawlGame.languagePack.getPotionString(SpireAnniversary6Mod.makeID(ID)).NAME;
        case 1:return CardCrawlGame.languagePack.getPotionString(SpireAnniversary6Mod.makeID(ID)).DESCRIPTIONS[index];}
    case 5:switch(key){case 0:return CardCrawlGame.languagePack.getMonsterStrings(SpireAnniversary6Mod.makeID(ID)).NAME;
        case 1:return CardCrawlGame.languagePack.getMonsterStrings(SpireAnniversary6Mod.makeID(ID)).MOVES[index];
        case 2:return CardCrawlGame.languagePack.getMonsterStrings(SpireAnniversary6Mod.makeID(ID)).DIALOG[index];}
    case 6:switch(key){case 0:return CardCrawlGame.languagePack.getEventString(SpireAnniversary6Mod.makeID(ID)).NAME;
        case 1:return CardCrawlGame.languagePack.getEventString(SpireAnniversary6Mod.makeID(ID)).DESCRIPTIONS[index];
        case 2:return CardCrawlGame.languagePack.getEventString(SpireAnniversary6Mod.makeID(ID)).OPTIONS[index];}
    case 7:switch(key){case 0:return CardCrawlGame.languagePack.getCharacterString(SpireAnniversary6Mod.makeID(ID)).NAMES[index];
        case 1:return CardCrawlGame.languagePack.getCharacterString(SpireAnniversary6Mod.makeID(ID)).TEXT[index];
        case 2:return CardCrawlGame.languagePack.getCharacterString(SpireAnniversary6Mod.makeID(ID)).OPTIONS[index];
        case 3:return CardCrawlGame.languagePack.getCharacterString(SpireAnniversary6Mod.makeID(ID)).UNIQUE_REWARDS[index];}
    case 8:switch(key){case 0:return CardCrawlGame.languagePack.getOrbString(SpireAnniversary6Mod.makeID(ID)).NAME;
        case 1:return CardCrawlGame.languagePack.getOrbString(SpireAnniversary6Mod.makeID(ID)).DESCRIPTION[index];}
    case 9:switch(key){case 0:return CardCrawlGame.languagePack.getStanceString(SpireAnniversary6Mod.makeID(ID)).NAME;
        case 1:return CardCrawlGame.languagePack.getStanceString(SpireAnniversary6Mod.makeID(ID)).DESCRIPTION[index];}
    case 10:switch(key){case 0:return CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID(ID)).TEXT[index];
        case 1:return CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID(ID)).EXTRA_TEXT[index];
        /*case 0:return CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID(ID)).TEXT_DICT;*/}
    case 11:switch(key){case 0:return CardCrawlGame.languagePack.getBlightString(SpireAnniversary6Mod.makeID(ID)).NAME;
        case 1:return CardCrawlGame.languagePack.getBlightString(SpireAnniversary6Mod.makeID(ID)).DESCRIPTION[index];}
    case 12:switch(key){case 0:return CardCrawlGame.languagePack.getRunModString(SpireAnniversary6Mod.makeID(ID)).NAME;
        case 1:return CardCrawlGame.languagePack.getRunModString(SpireAnniversary6Mod.makeID(ID)).DESCRIPTION;}
    case 13:switch(key){case 0:return CardCrawlGame.languagePack.getTutorialString(SpireAnniversary6Mod.makeID(ID)).TEXT[index];
        case 1:return CardCrawlGame.languagePack.getTutorialString(SpireAnniversary6Mod.makeID(ID)).LABEL[index];}
    case 14:switch(key){case 0:return CardCrawlGame.languagePack.getScoreString(SpireAnniversary6Mod.makeID(ID)).NAME;
        case 1:return CardCrawlGame.languagePack.getScoreString(SpireAnniversary6Mod.makeID(ID)).DESCRIPTIONS[index];}
    case 15:switch(key){case 0:return CardCrawlGame.languagePack.getAchievementString(SpireAnniversary6Mod.makeID(ID)).NAMES[index];
        case 1:return CardCrawlGame.languagePack.getAchievementString(SpireAnniversary6Mod.makeID(ID)).TEXT[index];}
    case 16:switch(key){case 0:return CardCrawlGame.languagePack.getCreditString(SpireAnniversary6Mod.makeID(ID)).NAMES[index];
        case 1:return CardCrawlGame.languagePack.getCreditString(SpireAnniversary6Mod.makeID(ID)).HEADER;}}
    return "ERROR COSMICZONE GETSTRINGSTRING: ID: "+ID+", TYPE:"+type+", KEY:"+key+", INDEX"+index;}
    /*In cards

    private DamageInfo makeInfo(DamageInfo.DamageType type){return makeInfo((AbstractCreature)AbstractDungeon.player,this.damage,type);}
    private DamageInfo makeInfo(AbstractCreature s,int dm,DamageInfo.DamageType type){return new DamageInfo(s,dm,type);}
    public void dmg(AbstractCreature m){dmg(m,AbstractGameAction.AttackEffect.NONE);}
    public void dmg(AbstractCreature m,AbstractGameAction.AttackEffect fx){atb((AbstractGameAction)new DamageAction(m,makeInfo(),fx));}
    public void dmgAll(){dmgAll(AbstractGameAction.AttackEffect.NONE);}
    public void dmgAll(AbstractGameAction.AttackEffect fx){atb((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)AbstractDungeon.player,this.multiDamage,this.damageTypeForTurn,fx));}
    public void dmgAllNoMulti(){for(AbstractMonster mo:monsterList()){dmg(mo);}}
    public void blck(){blck(AbstractDungeon.player,AbstractDungeon.player);}
    public void blck(AbstractCreature t,AbstractCreature s){atb((AbstractGameAction)new GainBlockAction(t,s,this.block,true));}
    public DamageInfo makeInfo(){return makeInfo(this.damageTypeForTurn);}
    public boolean isInHand(){return AbstractDungeon.player.hand.contains(this);}
    public boolean isInDeck(){return AbstractDungeon.player.drawPile.contains(this);}
    public boolean isInDp(){return AbstractDungeon.player.discardPile.contains(this);}
    public boolean isInEp(){return AbstractDungeon.player.exhaustPile.contains(this);}

    In powers:
    public void stackPower(int stackAmount){if(this.amount==-1&&(!canGoNegative||(canGoNegative&&overrideCanNegWithUnique))){}else{this.fontScale=8.0F;this.amount+=stackAmount;}}
    public void reducePower(int reduceAmount){if(this.amount==-1&&(!canGoNegative||(canGoNegative&&overrideCanNegWithUnique))){}else if(this.amount-reduceAmount<=0){this.fontScale=8.0F;this.amount=0;}else{this.fontScale=8.0F;this.amount-=reduceAmount;}if(amount==0){remThis();}}

    */
    
}
