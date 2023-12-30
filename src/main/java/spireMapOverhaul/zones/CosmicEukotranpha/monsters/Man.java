package spireMapOverhaul.zones.CosmicEukotranpha.monsters;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.purple.Omniscience;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.IncreaseCostOfTopOfDeckWithCostNextTurnOnlyEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.MillNonStatusesEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.special.*;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.ImperfectionPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.frameworks.GainPowerAtTurnStartPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.frameworks.PlayCardAtTurnStartPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.ManHalveNonAtkHPLossPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.ManInsultPower;

import java.util.ArrayList;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class Man extends CosmicZoneMonster{public Man(){this(0.0F,0.0F);}public static final String ID=
        SpireAnniversary6Mod.makeID(Man.class.getSimpleName());private static final MonsterStrings monsterStrings= CardCrawlGame.languagePack.getMonsterStrings(ID);public static final String NAME=monsterStrings.NAME;public static final String[]MOVES=monsterStrings.MOVES;public static final String[]DIALOG=monsterStrings.DIALOG;
public int currentInsult=-1;public int intentA=4;public int intentB=4;public int intentC=4;
public Man(float x,float y){super(NAME,ID,80,0.0F,-15.0F,380.0F,290.0F,null,x,y);MOVERS=MOVES;
    this.type=EnemyType.NORMAL;hpRange(20,35,25,40);currentInsult=-1;intentA=4;intentB=4;intentC=4;
	this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/Man/ManIdle0.png"));
    //Normal: 0,2,7,17. Elite: 0,3,8,18. Boss: 0,4,9,19
    //Man (Hard Normal)
    //30 hp. 25 Block at combat start. 30 Metallicize (30 Block, 35 Metallicize)
    //5 Curiosity. Gains 5 Metallicize at turn end. Hp loss by means other than attack damage is halved
    
    //Meta 25 30 5 > 24 28 4
    //NoDes 3>2
    
    //Not Fascinated:
    //A: Corpse Rally. 10 D. Mill 3. Add random card to dp (10,12,12,15 & Card is common or below)
    //B: No Destination. Add 3 Burns to deck. Increase cost of top 2 of deck with cost by 1 next turn only (A0,A2,A7,Gain 1 Blur)
    //Turn 4: C: Relentless Mockery. Apply 5 Weak, Vuln, and Frail. Activate Insult (A17: 99)
    //Turn 5: D: Snicker. Apply and gain 2 Imperfect-{On gain buff: Lose it and 1 of this} (A17: Heal 5 hp)
    //Fascinated:
    //E: Nudiustertian Reminiscence. Apply 2 Hello World. Gain 40 Temp hp (40,40,45,45 and Heal 5 hp)
    //F: Steel of Destiny. 7 D. Mill non-Statuses in top 5 of deck. Add "Sun from Steel" to deck (7,8,8,9 and Mill top 5 non-Statuses instead)
    //G: Final Mission. Pay 20 Metallicize; 50 D (50,55,55,68)
    //Hysteric Surge: H: Remove Weak, Vuln, and Frail from player. Apply "Play Omniscience at turn start". Increase rewards. Increase power of Fascinated moves
    //I (Empowered E): Apply 2 Hello World. Gain 70 Temp hp (70,70,80,80 and Heal 10 and gain 10 Metallicize)
    //J (Empowered F): 30 D. Mill non-Statuses in top 5 of deck. Add "Sun from Steel" to deck (30,34,34,40 and Mill top 5 non-Statuses instead)
    //K (Empowered G): Pay 40 Metallicize; 90 D (90,100,100,120)
    genMovePlus("Corpse Rally",aM(10,12,12,15),Intent.ATTACK_DEBUFF);
    genMovePlus("No Destination",0,Intent.DEBUFF);
    genMovePlus("Relentless Mockery",0,Intent.STRONG_DEBUFF);
    genMovePlus("Snicker",0,Intent.DEBUFF);
    genMovePlus("Nudiustertian Recall",0,Intent.DEFEND_DEBUFF);
    genMovePlus("Scrap of Destiny",aM(7,8,8,9),Intent.ATTACK_DEBUFF);
    genMovePlus("One Way Flight",aM(50,55,55,68),Intent.ATTACK);
    genMovePlus("Nudiustertian Reminiscence",0,Intent.DEFEND_DEBUFF);
    genMovePlus("Steel of Destiny",aM(30,34,34,40),Intent.ATTACK_DEBUFF);
    genMovePlus("Final Mission",aM(90,100,100,120),Intent.ATTACK);
    genMovePlus("Hysteric Surge",0,Intent.MAGIC);
    //"First Page"
    //"Second Page"
    //"Third Page"
    //"Fourth Page"
    
    //"Sun from Steel"
    //Heal 8 hp. After this has been drawn 3 times: Exhaust. [] When Exhausted: Take 40 D at next turn end [] Turn end in hand: Send this to deck
}
//25 Block at combat start. 30 Metallicize (30 Block, 35 Metallicize)
//5 Curiosity. Gains 5 Metallicize at turn end. Hp loss by means other than attack damage is halved
public void usePreBattleAction(){testCosmicZonePassive();monsterBlck(24+rBAI(asc>6,4));poS(new MetallicizePower(this,28+rBAI(asc>6,4)));
    poS(new GainPowerAtTurnStartPower(this,this,new MetallicizePower(this,4),"",""));poS(new ManHalveNonAtkHPLossPower(this,this));}
//"You're so forgettable I'm sure more of my friends know of the word [Graupel] than they do of you"

//Gaucherie
//Play three different type cards in a row
//Granularity
//Play three zero cost cards in a row
//Grapnel
//Play three Upgraded cards in a row
//Graupel
//Play three one cost Skills in a row

//"Icky [Oddment]. Do you not know what that means? Then use context clues you vitamin deficient noodle armed minotaur"

//Obeisant
//At turn end, Hand contains more skills than attacks (Before discarding due to game rules)
//Oddment
//At turn end, Hand contains one card (Before discarding due to game rules)
//Oddness
//At turn end, Hand size is odd (After discarding due to game rules)
//Odometry
//At turn end, Hand contains a zero cost card (Before discarding due to game rules)

//"Your shocking [Puissant] can't even be compared to the likes of The Wyvernslayer, As even when multiplied, zero stays the same"

//Paisans
//While an enemy is attacking, Play an attack whose attack damage is equal to the enemy's intent
//Puisne
//While an enemy is attacking, Play an attack whose attack damage is less than the enemy's intent
//Puissant
//While an enemy is attacking, Play an attack whose attack damage is more than the enemy's intent
//Pursuivant
//While an enemy is attacking, Deal unblocked attack damage which is less than the enemy's intent

//"I'll give you a free win, As long as you agree to a [Noyade]. Lol, No I won't tell you what it means"

//Noisette
//After playing four attacks in a turn, The square root of discard pile size is an integer
//Nopales
//After playing four skills in a turn, Discard pile contains more attacks than skills
//Notarize
//After playing four non-attacks in a turn, Discard pile size matches another pile (Draw pile, Hand, or Exhaust pile)
//Noyade
//After playing four cards in a turn, Discard pile is bigger than draw pile
public void startOfTurnIntentCheck(){if(GameActionManager.turn==1){makeInDeck(new PageA());makeInDeck(new PageB());makeInDeck(new PageC());makeInDeck(new PageD());}}



public void takeTurn(){switch(this.nextMove){
    //A: Corpse Rally. 10 D. Mill 3. Add random card to dp (10,12,12,15 & Card is common or below)
    case 0:monsterDmg();mill(3);int roll=AbstractDungeon.cardRandomRng.random(99);//30% Curse 30% Basic 20% Common 15% Uncommon 5% Rare
        if(roll<30){makeInDp(CardLibrary.getCurse().makeCopy());
        }else if(roll<60){makeInDp(CardLibrary.getAnyColorCard(AbstractCard.CardRarity.BASIC));
        }else if(roll<80||asc>16){makeInDp(CardLibrary.getAnyColorCard(AbstractCard.CardRarity.COMMON));
        }else if(roll<95){makeInDp(CardLibrary.getAnyColorCard(AbstractCard.CardRarity.UNCOMMON));
        }else{makeInDp(CardLibrary.getAnyColorCard(AbstractCard.CardRarity.RARE));}break;
    //B: No Destination. Add 3 Burns to deck. Increase cost of top 2 of deck with cost by 1 next turn only (A0,A2,A7,Gain 1 Blur)
    case 1:makeInDeck(new Burn(),2);atb(new IncreaseCostOfTopOfDeckWithCostNextTurnOnlyEffect(2));if(asc>16){poS(new BlurPower(this,1));}break;
    //Turn 4: C: Relentless Mockery. Apply 5 Weak, Vuln, and Frail. Activate Insult (A17: 99)
    case 2:pofP(new WeakPower(AbstractDungeon.player,5+rBAI(asc>16,94),true));pofP(new VulnerablePower(AbstractDungeon.player,5+rBAI(asc>16,94),true));pofP(new FrailPower(AbstractDungeon.player,5+rBAI(asc>16,94),true));
        int q=AbstractDungeon.cardRandomRng.random(99);if(q<25){atb(new TalkAction(this,"Insult 0 [Graupel], q="+q, 0.75F, 2.5F));poS(new ManInsultPower(this,this,0));
    }else if(q<50){atb(new TalkAction(this,"Insult 1 [Oddment], q="+q, 0.75F, 2.5F));poS(new ManInsultPower(this,this,1));
    }else if(q<75){atb(new TalkAction(this,"Insult 2 [Puissant], q="+q, 0.75F, 2.5F));poS(new ManInsultPower(this,this,2));
    }else{atb(new TalkAction(this,"Insult 3 [Noyade], q="+q, 0.75F, 2.5F));poS(new ManInsultPower(this,this,3));}break;
    //Turn 5: D: Snicker. Apply and gain 2 Imperfect (A17: Heal 5 hp)
    case 3:pofP(new ImperfectionPower(AbstractDungeon.player,this,2));poS(new ImperfectionPower(this,this,2));if(asc>16){heal(5,true);}break;
    //E: Nudiustertian Reminiscence. Apply 2 Hello World. Gain 40 Temp hp (40,40,45,45 and Heal 5 hp)
    case 4:pofP(new HelloPower(AbstractDungeon.player,2));atb(new AddTemporaryHPAction(this,this,40+rBAI(asc>6,5)));if(asc>16){heal(5,true);}if(intentA>1){intentA--;}break;
    //F: Steel of Destiny. 7 D. Mill non-Statuses in top 5 of deck. Add "Sun from Steel" to deck (7,8,8,9 and Mill top 5 non-Statuses instead)
    case 5:monsterDmg();atb(new MillNonStatusesEffect(5,asc>16));makeInDeck(new SunFromSteel());if(intentB>1){intentB--;}break;
    //G: Final Mission. Pay 20 Metallicize; 50 D (50,55,55,68)
    case 6:atb(new ReducePowerAction(this,this,"Metallicize",20));monsterDmg();if(intentC>1){intentC--;}break;
    //Hysteric Surge: H: Remove Weak, Vuln, and Frail from player. Apply "Play Omniscience at turn start". Increase rewards. Increase power of Fascinated moves
    case 7:atb(new RemoveSpecificPowerAction(AbstractDungeon.player,this,"Weakened"));atb(new RemoveSpecificPowerAction(AbstractDungeon.player,this,"Vulnerable"));atb(new RemoveSpecificPowerAction(AbstractDungeon.player,this,"Frail"));
        pofP(new PlayCardAtTurnStartPower(AbstractDungeon.player,this,new Omniscience(),"",""));hysteria=2;break;
    //I (Empowered E): Apply 2 Hello World. Gain 70 Temp hp (70,70,80,80 and Heal 10 and gain 10 Metallicize)
    case 8:pofP(new HelloPower(AbstractDungeon.player,2));atb(new AddTemporaryHPAction(this,this,70+rBAI(asc>6,10)));if(asc>16){heal(10,true);poS(new MetallicizePower(this,10));}if(intentA>1){intentA--;}break;
    //J (Empowered F): 30 D. Mill non-Statuses in top 5 of deck. Add "Sun from Steel" to deck (30,34,34,40 and Mill top 5 non-Statuses instead)
    case 9:monsterDmg();atb(new MillNonStatusesEffect(5,asc>16));makeInDeck(new SunFromSteel());if(intentB>1){intentB--;}break;
    //K (Empowered G): Pay 40 Metallicize; 90 D (90,100,100,120)
    case 10:atb(new ReducePowerAction(this,this,"Metallicize",40));monsterDmg();if(intentC>1){intentC--;}break;
    default:loggeer("ERROR, takeTurn outside switch, Man");
}lastMoveUpdt(nextMove);rollerMove();}
protected void getMove(int num){
    //Hysteric Surge
    //Turn 4: C
    //Turn 5: D
    //Not Fascinated: A then B then 50/50
    //Fascinated: E/I then Weird Weighted rng 3:4:4 then reduced by 1 to match 1 when used. Don't use G/K if cost can't be paid
    dontRedecide=false;
    if(hysteria==1){decMvePlus("Hysteric Surge");
    }else if(moveCount==3){decMvePlus("Relentless Mockery");
    }else if(moveCount==4){decMvePlus("Snicker");
    }else{if(!fasc()){
        if(moveCount==0){decMvePlus("Corpse Rally");
        }else if(moveCount==1||num<50){decMvePlus("No Destination");
        }else{decMvePlus("Corpse Rally");}
    }else{if(hysteria<2){
        if(intentA==4){decMvePlus("Nudiustertian Recall");
        }else{applicableMoveList=new ArrayList<>();appMoveWeights=new ArrayList<>();
            applicableMoveList.add("Nudiustertian Recall");appMoveWeights.add(intentA);
            applicableMoveList.add("Scrap of Destiny");appMoveWeights.add(intentB);
            if(hasPowAmount(this,"Metallicize",20)){applicableMoveList.add("One Way Flight");appMoveWeights.add(intentC);}
            decMvePlus(getAppMoveUseWeights());}
    }else{
        if(intentA==4){decMvePlus("Nudiustertian Reminiscence");
        }else{applicableMoveList=new ArrayList<>();appMoveWeights=new ArrayList<>();
            applicableMoveList.add("Nudiustertian Reminiscence");appMoveWeights.add(intentA);
            applicableMoveList.add("Steel of Destiny");appMoveWeights.add(intentB);
            if(hasPowAmount(this,"Metallicize",40)){applicableMoveList.add("Final Mission");appMoveWeights.add(intentC);}
            decMvePlus(getAppMoveUseWeights());}
    }}}
    moveChangeCheck();}
public void fulfillMeaning(int insult){atb(new TalkAction(this,"Incredible, Do you not think so?",0.75F,2.5F));hysteria=1;}
@Override public void update(){super.update();upTicks++;
    if(upTicks>180){switch(currImg){
        case 0:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/Man/ManIdle1.png"));upTicks=120;currImg=1;break;
        case 1:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/Man/ManIdle2.png"));upTicks=175;currImg=2;break;
        case 2:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/Man/ManIdle1.png"));upTicks=145;currImg=3;break;
        case 3:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/Man/ManIdle0.png"));upTicks=0;currImg=1;break;
    }}}
public void changeState(String key){}
public void damage(DamageInfo info){super.damage(info);if(info.owner!=null&&info.type!=DamageType.THORNS&&info.output>0){}}
public void die(){super.die();}




//Dialogue:

//Combat start:
//""
//Fascination:
//""
//Use Hysteric Surge:
//""
//Normal Kill:
//""
//Fascination Kill:
//""
//Hysteric Kill:
//""
//Anti Cosmic monster card used:
//""
//Player death:
//""
//Player death while Fascinated or Hysteric
//""

//(Wip)
//A Dictionary is added to your deck, When played it shuffles 4 cards with Ethereal in your deck. On turn 4, Man says one of the 4 insults
//
//"You're so forgettable I'm sure more of my friends know of the word [Graupel] than they do of you"
//"Icky [Oddment]. Do you not know what that means? Then use context clues you vitamin deficient noodle armed minotaur"
//"Your shocking [Puissant] can't even be compared to the likes of The Wyvernslayer, As even when multiplied, zero stays the same"
//"I'll give you a free win, As long as you agree to a [Noyade]. Lol, No I won't tell you what it means"
//
//Find the correct page and play it, It then shows what to do. It's always the same. Then if you do the turn ends and she goes to the next phase (Harder) & Gives you a power to play Omniscience at turn start & the rewards are increased
//
//Killed early:
//"Geez, Ultracrepidarian"
}
