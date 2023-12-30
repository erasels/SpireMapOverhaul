package spireMapOverhaul.zones.CosmicEukotranpha.monsters;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.blue.Rebound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.CuriosityPower;
import com.megacrit.cardcrawl.powers.MalleablePower;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.CZDiscardTEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.Frames.TargetLeftMostInHandEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.special.CuriousMind;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.SevenHandedTwinheadIntriguedPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly.PlayTopCardsOfDeckNextTurnEndPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly.SevenHandedTwinheadBrainlockedPower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class SevenHandedTwinhead extends CosmicZoneMonster{public SevenHandedTwinhead(){this(0.0F,0.0F);}public static final String ID=
        SpireAnniversary6Mod.makeID(SevenHandedTwinhead.class.getSimpleName());private static final MonsterStrings monsterStrings=CardCrawlGame.languagePack.getMonsterStrings(ID);public static final String NAME=monsterStrings.NAME;public static final String[]MOVES=monsterStrings.MOVES;public static final String[]DIALOG=monsterStrings.DIALOG;
public int powersSeen=0;public int shufflesSeen=0;public int sabo=0;
public SevenHandedTwinhead(float x,float y){super(NAME,ID,80,0.0F,-15.0F,380.0F,290.0F,null,x,y);MOVERS=MOVES;
    this.type=EnemyType.NORMAL;hpRange(70,70,77,77);powersSeen=0;shufflesSeen=0;sabo=0;
    this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/SevenHandedTwinhead/SevenHandedTwinheadIdle0.png"));
    //Normal: 0,2,7,17. Elite: 0,3,8,18. Boss: 0,4,9,19
    
    //Reworked Brainlocked (Don't draw at turn start (Gain No Draw at turn start before drawing, Then lose it at turn start after drawing))
    //Reworked as it doesn't work for some reason: Next card played is sent to top of deck > Add Rebound to hand
    //Reworked because game rules > Play top 2 cards of deck next turn end
    
    //Seven Handed Twinhead (Normal non-Easy)
    //70 hp. 7 Malleable. 7 Curiosity (77 hp)
    //7 Intrigued-{When deck is shuffled: Gain 7 Str "I want to see your Powers, and for you to Shuffle our minds"}
    //Not Fascinated: A then B or C
    //A: Apply Brainlocked-{Gain No Draw at turn start before drawing. Seek 4 at turn start} (A0,Apply 2 Constricted,A7,Seek 3 & Apply 7 Constricted)
    //B: Add Curious Mind-{Gain 2 Curiosity. Choose 1 of 3 cards to add to hand and add the other 2 to deck} to deck. 7 DX2
    //C: 7 D. 7 B. Next card played is sent to top of deck
    //Fascinated:
    //D: 7 DX2. You must choose 7 non-power cards in deck to discard
    //E: 7 BX2. Play the top 2 cards of deck. Discard 2 left most cards in hand
    genMove("Think Like Us",0);
    genMove("Crunches",7);
    genMove("Scruffle Pulse",7);
    genMove("Feast",7);
    genMove("Code 7: Sabotage",0);
    genMove("Hysteric Surge",0);
}
public void usePreBattleAction(){testCosmicZonePassive();poS(new MalleablePower(this,7));poS(new CuriosityPower(this,7));poS(new SevenHandedTwinheadIntriguedPower(this,this,7));}
public void takeTurn(){switch(this.nextMove){
    //Seven Handed Twinhead (Normal non-Easy)
    //70 hp. 7 Malleable. 7 Curiosity (77 hp)
    //7 Intrigued-{When deck is shuffled: Gain 7 Str "I want to see your Powers, and for you to Shuffle our minds"}
    //Not Fascinated: A then B or C. Don't use a move 3 turns in a row
    //A: Apply Brainlocked-{Gain No Draw at turn start before drawing. Seek 4 at turn start} (A0,Apply 2 Constricted,A7,Seek 3 & Apply 7 Constricted)
    //B: Add Curious Mind-{Gain 2 Curiosity. Choose 1 of 3 cards to add to hand and add the other 2 to deck} to deck. 7 DX2
    //C: 7 D. 7 B. Next card played is sent to top of deck
    //Fascinated: Don't use a move 3 turns in a row
    //D: 7 DX2. You must choose 7 non-power cards in deck to discard
    //E: 7 BX2. Play the top 2 cards of deck. Discard 2 left most cards in hand
    case 0:pofP(new SevenHandedTwinheadBrainlockedPower(AbstractDungeon.player,this,4-rBAI(asc>16)));if(asc>1){pofP(new ConstrictedPower(AbstractDungeon.player,this,2+rBAI(asc>16,5)));}break;
    case 1:makeInDeck(new CuriousMind(),2);monsterDmg();monsterDmg();break;
    case 2:monsterDmg();monsterBlck(7);makeInHand(new Rebound());break;
    case 3:monsterDmg();monsterDmg();CardGroup co=new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);for(AbstractCard c:AbstractDungeon.player.drawPile.group){if(c.type!=AbstractCard.CardType.POWER){co.addToBottom(c);}}discard(co,7);break;
    case 4:monsterBlck(7);monsterBlck(7);atb(new TargetLeftMostInHandEffect(new CZDiscardTEffect(null),2));break;
    default:loggeer("ERROR, takeTurn outside switch, SevenHandedTwinhead");
}lastMoveUpdt(nextMove);rollerMove();}
public void startOfTurnIntentCheck(){if(nextMove==4){pofP(new PlayTopCardsOfDeckNextTurnEndPower(AbstractDungeon.player,this,2));}}
protected void getMove(int num){
    if(moveCount==0){decMve("Think Like Us",iteFIn(10));
    }else if(!fasc()){
        if(limitUseInARow(1,2)){decAtk("Scruffle Pulse",iteFIn(3),1);
        }else if(limitUseInARow(2,2)){decAtk("Crunches",iteFIn(0),2);
        }else{if(num<50){decAtk("Crunches",iteFIn(0),2);}else{decAtk("Scruffle Pulse",iteFIn(3),1);}}
    }else{
        if(limitUseInARow(3,2,1)){decMve("Code 7: Sabotage",iteFIn(6));
        }else if(limitUseInARow(4,2,2)){decAtk("Feast",iteFIn(2),2);
        }else{if(num<50){decAtk("Feast",iteFIn(2),2);}else{decMve("Code 7: Sabotage",iteFIn(6));}}
    }}
@Override public void update(){super.update();upTicks++;
    if(upTicks>60){upTicks=0;if(currImg==0){this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/SevenHandedTwinhead/SevenHandedTwinheadIdle1.png"));currImg=1;
    }else{this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/SevenHandedTwinhead/SevenHandedTwinheadIdle0.png"));currImg=0;}}
}
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

//"I'm curious of what Powers you show"
//"Indeed, Will you Shuffle our minds?"
//
//Play Power:
//"That rivals the Power of our sphere"
//Shuffle deck:
//"Heh, Guess that's a way to do so. I'm pleased"
//
//Both have conditions fulfilled or Fascinated: Start talking to each other
//"Attacks are temporary, Powers are eternal!"
//"Have you heard of, Talk to the Hand?"
//
//"Perhaps you need to take a Deep Breath?"
//"Perhaps you can shut up now"
//
//"Why did the chicken cross the road?"
//"You do realize WE were meant to do a stand-up comedy"
//"I'm doing it right now"
//"No, like what happened to the story of the psychopath dog?"
//"The what?"
//"Goddammit"
//
//Use intent E:
//"I'd say \[Card 1] would be a delicious if it was a Cuisine"
//"No way, \[Card 2] would be much better"
//
//Killed before Fascinated: -Cosmic
//"NPC"
//"Third wheel"
//Killed while Fascinated: Neutral
//"And then I said "I'm curious but I'll Astral-ater!""
//"... I need some Space from you"
//Killed after Deck was Shuffled and Power was played (Even if not Fascinated): +Cosmic
//"My Gratitudes, That was entertaining!"
//"Indeed, I would rather have you instead of this guy"
//"Hey!"
}
