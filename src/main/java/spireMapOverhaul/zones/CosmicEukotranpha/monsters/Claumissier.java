package spireMapOverhaul.zones.CosmicEukotranpha.monsters;import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.blue.Claw;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.frameworks.GainPowerAtTurnStartPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.ClaumissierMalleableNebulaePower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.ClaumissierSharpClawsPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly.ClaumissierClawDancePower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly.ClaumissierClawPerformancePower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly.DrawOnPlayAtksInATurnPower;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicZoneGameActionHistory;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class Claumissier extends CosmicZoneMonster{public Claumissier(){this(0.0F,-40.0F);}public static final String ID=
        SpireAnniversary6Mod.makeID(Claumissier.class.getSimpleName());private static final MonsterStrings monsterStrings=CardCrawlGame.languagePack.getMonsterStrings(ID);public static final String NAME=monsterStrings.NAME;public static final String[]MOVES=monsterStrings.MOVES;public static final String[]DIALOG=monsterStrings.DIALOG;
public int clawsSeen=0;public int clawsSeenThisTurn=0;
public Claumissier(float x,float y){super(NAME,ID,80,0.0F,-15.0F,380.0F,290.0F,null,x,y);MOVERS=MOVES;
    this.type=EnemyType.NORMAL;hpRange(180,213,213,213);clawsSeen=0;clawsSeenThisTurn=0;
	this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/Claumissier/ClaumissierIdle0.png"));
    //Claumissier (Hard Normal Enemy, Balanced around Maw but slightly worse)
    //213 hp. 13 Curiosity.
    //Malleable Nebulae (After on play non-Claw Atk: 3 B, Increase it by 1 this turn only. [] On play Claw: Lose B)
    //Sharp Claws (Gain 2 Str on play Claw. "I, Claumissier, Want to see at least 5 Claws")
    //(Claws this creates retroactively gain Claw buffs)
    
    
    
    //Arm Strike/Slam On third card played this turn: If it wasn't a Claw: 13 D to player. (Slam: Gain 3 Str). Name changed to Claw Dance and Claw Performance
    //Malleable > On play non-Claw Atk: 3 B, Increase it by 1 this turn only. [] On play Claw: Lose B
    
    
    //Normal: 0,2,7,17. Elite: 0,3,8,18. Boss: 0,4,9,19
    //Not Fascinated: If A wasn't used last turn, A. Otherwise 50/50 A or B. Never uses A 3 times in a row
    //A: 13 D, Unaffected by D modifiers. Shuffle 3 Claws into deck (13,14,14,16)
    //B: 3 D (3,4,4,6)
    //Fascinated: 50/50 C or D. Never uses a move 3 times in a row
    //C: 13 D, Unaffected by D modifiers. Shuffle 3 Claws into deck. Gain 3 Strength if you didn't play a Claw this turn (13,14,14,16 6Str)
    //D: 9 D, Gain B equal to intent damage (9,10,11,12)
    //E: 1 DX2, Only used if Hysteric Surge was used this combat (1,3,3,3X3)
    //High Priority:
    //Play 5 Claws: F: Hysteric Surge: Player draws 2 after they play 3 Atks in a turn. Gain 6 Plated Armor at player turn start (A0,A2,7 Plated,20 Plated)
    genMove("Arm Strike",aM(13,14,14,16));
    genMove("Claw",aM(3,4,4,6));
    genMove("Arm Slam",aM(13,14,14,16));
    genMove("Claw Whack",aM(9,10,11,12));
    genMove("Multi Claw",aM(1,3,3,3));
    genMove("Hysteric Surge",0);
}
public void usePreBattleAction(){CosmicZoneGameActionHistory.gashAmount=0;testCosmicZonePassive();poS(new ClaumissierMalleableNebulaePower(this,this,3));poS(new ClaumissierSharpClawsPower(this,this,2));}
public void takeTurn(){switch(this.nextMove){
    //Normal: 0,2,7,17. Elite: 0,3,8,18. Boss: 0,4,9,19
    //Not Fascinated: If A wasn't used last turn, A. Otherwise 50/50 A or B. Never uses A 3 times in a row
    //A: 13 D, Unaffected by D modifiers. Shuffle 3 Claws into deck (13,14,14,16)
    //B: 3 D (3,4,4,6)
    //Fascinated: 50/50 C or D, If Hysteric Around 33% for each. Never uses a move 3 times in a row
    //C: 13 D, Unaffected by D modifiers. Shuffle 3 Claws into deck. Gain 3 Strength if you didn't play a Claw this turn (13,14,14,16 6Str)
    //D: 9 D, Gain B equal to intent damage (9,10,11,12)
    //E: 1 DX2, Only used if Hysteric Surge was used this combat (1,3,3,3X3)
    //High Priority:
    //Play 5 Claws: F: Hysteric Surge: Player draws 2 after they play 3 Atks in a turn. Gain 6 Plated Armor at player turn start (A0,A2,7 Plated,20 Plated)
    
    case 0:AbstractCard c=new Claw();c.baseDamage+=CosmicZoneGameActionHistory.gashAmount;c.applyPowers();makeInDeck(c.makeStatEquivalentCopy(),3);break;
    case 1:monsterDmg(1);break;
    case 2:AbstractCard ce=new Claw();ce.baseDamage+=CosmicZoneGameActionHistory.gashAmount;ce.applyPowers();makeInDeck(ce.makeStatEquivalentCopy(),3);break;
    case 3:monsterDmg(3);monsterBlck(damage.get(3).base);break;
    case 4:monsterDmg(4);monsterDmg(4);break;
    case 5:pofP(new DrawOnPlayAtksInATurnPower(AbstractDungeon.player,this,3,2));poS(new GainPowerAtTurnStartPower(this,this,new PlatedArmorPower(this,aM(6,6,7,20)),"",""));hysteria=2;break;
    default:loggeer("ERROR, takeTurn outside switch, Claumissier");
}clawsSeenThisTurn=0;lastMoveUpdt(nextMove);rollerMove();}
public void startOfTurnIntentCheck(){CosmicZoneMod.logger.info("Clau Intent Check. NextMove="+nextMove);if(nextMove==0){pofP(new ClaumissierClawDancePower(AbstractDungeon.player,this,aM(13,14,14,16)));}
    if(nextMove==2){pofP(new ClaumissierClawPerformancePower(AbstractDungeon.player,this,aM(13,14,14,16),3+rBAI(asc>16,3)));}}
protected void getMove(int num){
    if(clawsSeen>=5&&hysteria<2){decMve("Hysteric Surge",iteFIn(10));
    }else if(!fasc()){
        if(lastMove!=0){decMve("Arm Strike",iteFIn(8));
        }else{if(lastMoveInARow>1){decAtk("Claw",iteFIn(0),1);
        }else{if(num<50){decMve("Arm Strike",iteFIn(8));
        }else{decAtk("Claw",iteFIn(0),1);}}}
    }else{if(lastMoveInARow>1){
        if(lastMove==0||lastMove==2){
            if(hysteria>1){if(num<50){decAtk("Claw Whack",iteFIn(3),1);
            }else{decAtk("Multi Claw",iteFIn(0),2+rBAI(asc>16));}
            }else{decAtk("Claw Whack",iteFIn(3),1);}
        }else if(lastMove==1||lastMove==3){
            if(hysteria>1){if(num<50){decMve("Arm Slam",iteFIn(8));
            }else{decAtk("Multi Claw",iteFIn(0),2+rBAI(asc>16));}
            }else{decMve("Arm Slam",iteFIn(8));}
        }else if(lastMove==4){
            if(num<50){decMve("Arm Slam",iteFIn(8));
            }else{decAtk("Claw Whack",iteFIn(3),1);}}
    }else{if(hysteria>1){
        if(num<33){decMve("Arm Slam",iteFIn(8));
        }else if(num<66){decAtk("Claw Whack",iteFIn(3),1);
        }else{decAtk("Multi Claw",iteFIn(0),2+rBAI(asc>16));}
    }else{if(num<50){decMve("Arm Slam",iteFIn(8));
    }else{decAtk("Claw Whack",iteFIn(3),1);}
    }}
    }}
@Override public void update(){super.update();upTicks++;
	if(upTicks>60){upTicks=0;if(currImg==0){this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/Claumissier/ClaumissierIdle1.png"));currImg=1;
	}else{this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/Claumissier/ClaumissierIdle0.png"));currImg=0;}}
}

public void changeState(String key){}
public void damage(DamageInfo info){super.damage(info);if(info.owner!=null&&info.type!=DamageType.THORNS&&info.output>0){}}
public void die(){super.die();}
public void clawSeen(){}
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
}
