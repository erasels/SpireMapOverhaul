package spireMapOverhaul.zones.CosmicEukotranpha.monsters;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.frameworks.LosePowerAtNextTurnEndPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.*;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly.*;

import java.util.ArrayList;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class HairClipApple extends CosmicZoneMonster{public HairClipApple(){this(0.0F,0.0F);}public static final String ID=
        SpireAnniversary6Mod.makeID(HairClipApple.class.getSimpleName());private static final MonsterStrings monsterStrings=CardCrawlGame.languagePack.getMonsterStrings(ID);public static final String NAME=monsterStrings.NAME;public static final String[]MOVES=monsterStrings.MOVES;public static final String[]DIALOG=monsterStrings.DIALOG;
    public int enraged=0;public int lalenai=0;public int A=0;public int B=0;public int C=0;
    public HairClipApple(float x,float y){super(NAME,ID,80,0.0F,-15.0F,380.0F,290.0F,null,x,y);MOVERS=MOVES;
        this.type=EnemyType.ELITE;hpRange(78,78,78,78);enraged=0;lalenai=0;A=1;B=0;C=1;
        //Normal: 0,2,7,17. Elite: 0,3,8,18. Boss: 0,4,9,19

        //Compress Powers:
        //Murderous Aura-{Heals 10(Energy) hp at turn end. On Atk played: Player draws 2}
        //B can't be used first turn
        //A: 50 B/100 B
        //Walk: Applies a power that does 10(Hand size) B

        //Hair Clip Apple
        //78 hp
        //10 Energy Eater-{Heals 10(Energy) hp at turn end}
        //2 Aura of Murder-{On Atk played: Player draws 2}
        //Ample the Diving I will a Doom-{Can only lose up to 77 hp, Then Enrages if not using Hysteric Surge}
        //10 Tasty-{Whenever this deals unblocked attack D: Gain 10 Oranges "I want 100 Oranges, Let me eat you"}
        //Phase 1 (This monster is unaffected by ascension phase 1)
        //Not Fascinated:
        //A: 18 D. Apply 2 Tailwind-{2 B on play Skl}. Turn start when decided: 90 B to player. Gain 10 Momentary Thorns
        //B: 105 D. Turn start when decided: Apply 1 Winds of Terror-{The third Skl played this turn is played three times}
        //C: 43 D. Turn start when decided: Apply 1 Fleet Foot-{1 B to player on draw card this turn}
        //Every 4 turns: D: 10 D, Deals no unblocked attack D and instead gains that much Oranges (Viewed as Power). Apply 1 Depleted-{Gain 1 Blur at turn end if you have no energy at turn end. Turn start with 20 or more B: Add 2 Voids to deck}. When decided: 10 B to player. On play Atk: Gain 5 Momentary Str
        genMovePlus("Jump In",18,Intent.ATTACK_BUFF);
        genMovePlus("Big Mouth",105,Intent.ATTACK);
        genMovePlus("Don't Run",43,Intent.ATTACK);
        genMovePlus("Proxy Devour",10,Intent.ATTACK_DEBUFF);
        //Fascinated:
        //E: 18X2 D. Apply 4 Tailwind. Turn start when decided: 180 B to player. Gain 20 Momentary Thorns
        //F: 219 D. Turn start when decided: Apply 99 Winds of Terror
        //G: 90 D. Turn start when decided: Apply 2 Fleet Foot
        //Every 4 turns: H: 10 D, Deals no unblocked attack D and instead gains that much Oranges. Apply 1 Hollow-{Lose 1 Str and Dex at turn end. Gain 1 energy at turn start}. When decided: 10 B to player. On play Atk: Gain 5 Momentary Str
        genMovePlus("Into my Maw",18,Intent.ATTACK_BUFF,2);
        genMovePlus("The Gate to Hell",219,Intent.ATTACK);
        genMovePlus("You Shouldn't Flee",90,Intent.ATTACK);
        genMovePlus("Soul Gorge",10,Intent.ATTACK_DEBUFF);
        //Has 100 Oranges: I: Hysteric Surge: Adds tons of Fruits into deck each with different permanent effects (Adds a card draw Fruit to hand). Then leaves next turn
        genMovePlus("Hysteric Surge",0,Intent.MAGIC);
        //Phase 2
        //Enrage: Set Max hp to 104 (116)
        //All powers this applied is removed
        //Will a Stu Chipper E Antlion-{While Fascinated: Add Clumsy to deck at turn end}
        //Starting move: Apply Barricade, Swan Beam Stretch Still Action Bother-{Turn start with 100 or more Block: Lose all Block and Hair Clip Apple gains 50 Beat of Death} (50,60,60,80 or more B and 80 BoD)
        //Walk > Jog > Sprint
        //Walk: 10(Hand size) B to player. 32 D. Turn start when decided: Player gains 1 Equilibrium
        //Jog: (Player B) B to player. 8 DX7. Gain 5 Str
        //Sprint: 95 D. Turn start when decided: Apply 5 Momentary After Image
        genMovePlus("Fury Anger Rage Seethe",0,Intent.BUFF);
        genMovePlus("Doctor Batts Wink a Fiddle",0,Intent.STRONG_DEBUFF);
        genMovePlus("Walk",32,Intent.ATTACK);
        genMovePlus("Jog",8,Intent.ATTACK_BUFF,7);
        genMovePlus("Sprint",95,Intent.ATTACK);
    }
    public void usePreBattleAction(){testCosmicZonePassive();poS(new HairClipAppleMurderousAuraPower(this,this));poS(new HairClipAppleAmpleTheDivingIWillADoomPower(this,this));poS(new HairClipAppleTastyPower(this,this,10));}
    public void takeTurn(){switch(this.nextMove){
        //A: 18 D. Apply 2 Tailwind. Turn start when decided: 90 B to player. Gain 10 Momentary Thorns
        case 0:monsterDmg();pofP(new HairClipAppleTailwindPower(AbstractDungeon.player,this,2));A=0;B=1;C=1;lalenai++;break;
        //B: 105 D. Turn start when decided: Apply 1 Winds of Terror-{The third Skl played this turn is played three times}
        case 1:monsterDmg();A=1;B=0;C=1;lalenai++;break;
        //C: 43 D. Turn start when decided: Apply 1 Fleet Foot-{1 B to player on draw card this turn}
        case 2:monsterDmg();A=1;B=1;C=0;lalenai++;break;
        //Every 4 turns: D: 10 D, Deals no unblocked attack D and instead gains that much Oranges (Viewed as Power). Apply 1 Depleted-{Gain 1 Blur at turn end if you have no energy at turn end. Turn start with 20 or more B: Add 2 Voids to deck}. When decided: 10 B to player. On play Atk: Gain 5 Momentary Str
        case 3:monsterDmg();pofP(new HairClipAppleDepletedPower(AbstractDungeon.player,this));A=1;B=1;C=1;lalenai=0;break;
        //E: 18X2 D. Apply 4 Tailwind. Turn start when decided: 180 B to player. Gain 20 Momentary Thorns
        case 4:monsterDmg();monsterDmg();pofP(new HairClipAppleTailwindPower(AbstractDungeon.player,this,4));A=0;B=2;C=2;lalenai++;break;
        //F: 219 D. Turn start when decided: Apply 99 Winds of Terror
        case 5:monsterDmg();A=2;B=0;C=2;lalenai++;break;
        //G: 90 D. Turn start when decided: Apply 2 Fleet Foot
        case 6:monsterDmg();A=2;B=2;C=0;lalenai++;break;
        //Every 4 turns: H: 10 D, Deals no unblocked attack D and instead gains that much Oranges. Apply 1 Hollow-{Lose 1 Str and Dex at turn end. Gain 1 energy at turn start}. When decided: 10 B to player. On play Atk: Gain 5 Momentary Str
        case 7:monsterDmg();pofP(new HairClipAppleHollowPower(AbstractDungeon.player,this));A=2;B=2;C=2;lalenai=0;break;
        //Has 100 Oranges: I: Hysteric Surge: Adds tons of Fruits into deck each with different permanent effects (Adds a card draw Fruit to hand). Then leaves next turn
        case 8:break;//TODO: Hysteric Surge
        //Enrage: Set Max hp to 104 (116). Remove all powers related to this
        case 9:maxHealth=104+rBAI(asc>17,12);heal(maxHealth);poS(new HairClipAppleWillAStuChipperEAntlionPower(this,this));
            for(AbstractPower po:AbstractDungeon.player.powers){if(po instanceof HairClipAppleDepletedPower||po instanceof HairClipAppleFleetFootPower||po instanceof HairClipAppleHollowPower||po instanceof HairClipAppleTailwindPower||po instanceof HairClipAppleWindsOfTerrorPower){atb(new RemoveSpecificPowerAction(AbstractDungeon.player,this,po));}}
            for(AbstractPower po:this.powers){if(po instanceof HairClipAppleAmpleTheDivingIWillADoomPower||(po instanceof ThornsPower&&po.owner==this)||po instanceof HairClipAppleMurderousAuraPower||po instanceof HairClipAppleAuraOfMurderPower||po instanceof HairClipAppleEgoEaterIntentPower||po instanceof HairClipAppleEnergyEaterPower||po instanceof HairClipAppleMomentaryStrOnPlayAtkThisTurnPower||po instanceof HairClipAppleOrangesPower||po instanceof HairClipAppleTastyPower){atb(new RemoveSpecificPowerAction(this,this,po));}}enraged=2;break;
        //Starting move: Apply Barricade, Swan Beam Stretch Still Action Bother-{Turn start with 100 or more Block: Lose all Block and Hair Clip Apple gains 50 Beat of Death} (50,60,60,80 or more B and 80 BoD)
        case 10:pofP(new BarricadePower(AbstractDungeon.player));pofP(new HairClipAppleSwanBeamStretchStillActionBotherPower(AbstractDungeon.player,this,100-rBAI(asc>17,20),aM(50,60,60,80)));lalenai=7;break;
        //Walk: 10(Hand size) B to player. 32 D. Turn start when decided: Player gains 1 Equilibrium
        case 11:monsterDmg();lalenai=8;break;
        //Jog: (Player B) B to player. 8 DX7. Gain 5 Str
        case 12:monsterBlck(AbstractDungeon.player.currentBlock,AbstractDungeon.player);monsterDmg();monsterDmg();
            monsterDmg();monsterDmg();monsterDmg();monsterDmg();monsterDmg();poS(new StrengthPower(this,5));lalenai=9;break;
        //Sprint: 100 D. Turn start when decided: Apply 5 Momentary After Image
        case 13:monsterDmg();lalenai=7;break;
        default:loggeer("ERROR, takeTurn outside switch, Claumissier");
    }lastMoveUpdt(nextMove);rollerMove();}
    public void startOfTurnIntentCheck(){switch(nextMove){
            case 0:monsterBlck(50,AbstractDungeon.player);poS(new ThornsPower(this,10));poS(new LosePowerAtNextTurnEndPower(this,this,"Thorns","","",10,true));break;
            case 1:pofP(new HairClipAppleWindsOfTerrorPower(AbstractDungeon.player,this,1));break;
            case 2:pofP(new HairClipAppleFleetFootPower(AbstractDungeon.player,this,1));break;
            case 3:monsterBlck(10);poS(new HairClipAppleEgoEaterIntentPower(this,this));poS(new HairClipAppleMomentaryStrOnPlayAtkThisTurnPower(this,this,5));break;
            case 4:monsterBlck(100,AbstractDungeon.player);poS(new ThornsPower(this,20));poS(new LosePowerAtNextTurnEndPower(this,this,"Thorns","","",20,true));break;
            case 5:pofP(new HairClipAppleWindsOfTerrorPower(AbstractDungeon.player,this,99));break;
            case 6:pofP(new HairClipAppleFleetFootPower(AbstractDungeon.player,this,2));break;
            case 7:monsterBlck(10);poS(new HairClipAppleEgoEaterIntentPower(this,this));poS(new HairClipAppleMomentaryStrOnPlayAtkThisTurnPower(this,this,5));break;
            case 8:break;//TODO: Hysteric Surge Cards
            case 9:break;
            case 10:break;
            case 11:pofP(new EquilibriumPower(AbstractDungeon.player,1));pofP(new HairClipAppleMellowPacePower(AbstractDungeon.player,this));break;
            case 12:break;
            case 13:pofP(new AfterImagePower(this,5));pofP(new LosePowerAtNextTurnEndPower(this,this,"After Image","","",5,true));break;
        }}
    protected void getMove(int num){dontRedecide=false;
        if(hasPowAmount(this,"CosmicZone:HairClipAppleOrangesPower",100)){
            decMvePlus("Hysteric Surge");
        }else if(this.currentHealth<=1&&enraged<2){//I mean if somehow it gets here
            decMvePlus("Fury Anger Rage Seethe");
        }else if(enraged==2){if(lalenai<7){decMvePlus("Doctor Batts Wink a Fiddle");
            }else{switch(lalenai){case 7:decMvePlus("Walk");break;case 8:decMvePlus("Jog");break;default:decMvePlus("Sprint");}}
        }else if(fasc()){if(lalenai>2){decMvePlus("Soul Gorge");
            }else{applicableMoveList=new ArrayList<>();
                if(A!=0){applicableMoveList.add("Into my Maw");}
                if(B!=0){applicableMoveList.add("The Gate to Hell");}
                if(C!=0){applicableMoveList.add("You Shouldn't Flee");}
                decMvePlus(getAppMove(applicableMoveList));}
        }else{if(lalenai>2){decMvePlus("Proxy Devour");
            }else{applicableMoveList=new ArrayList<>();
                if(A!=0){applicableMoveList.add("Jump In");}
                if(B!=0){applicableMoveList.add("Big Mouth");}
                if(C!=0){applicableMoveList.add("Don't Run");}
                decMvePlus(getAppMove(applicableMoveList));}}
        moveChangeCheck();}
    public void changeState(String key){}
    public void damage(DamageInfo info){super.damage(info);if(info.owner!=null&&info.type!=DamageType.THORNS&&info.output>0){if(this.currentHealth<=1&&enraged<1){dontRedecide=false;decMvePlus("Fury Anger Rage Seethe");this.createIntent();this.applyPowers();this.enraged=1;}}}
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
}


//Old
//Hair Clip Apple
//78 hp. Heals 10(Energy) hp at turn end. Player draws 2 whenever you play an Atk. Can only lose up to 77 hp, Then Enrages if not Hysteric
//Whenever this deals unblocked attack D: Gain 10 Oranges
//Phase 1
//Not Fascinated:
//A: 18 D. Apply 2 After Image. When decided: 90 B to player. Gain 10 Momentary Thorns
//B: 105 D. When decided: The third Skl played this turn is played three times
//C: 43 D. When decided: 1 B to player on draw card
//Every 4 turns: D: 10 D, Deals no unblocked attack D and instead gains that much Oranges. Apply "Gain 1 Blur at turn end if you have no energy at turn end. Turn start with 20 or more B: Add 2 Voids to deck". When decided: On play Atk: Gain 5 Momentary Str
//Fascinated:
//E: 18X2 D. Apply 4 After Image. When decided: 180 B to player. Gain 20 Momentary Thorns
//F: 219 D. When decided: Every 3 Skls played this turn is played three times
//G: 90 D. When decided: 2 B to player on draw card
//Every 4 turns: H: 10 D, Deals no unblocked attack D and instead gains that much Oranges. Apply "Lose 1 Str and Dex at turn end. Gain 1 energy at turn start". When decided: On play Atk: Gain 5 Momentary Str
//
//Has 100 Oranges: I: Hysteric Surge: Become Immune. Adds tons of Fruits into deck each with different permanent effects (Adds a card draw Fruit to hand). Then leaves next turn
//
//Phase 2
//Enrage: Set Max hp to 204

//204 hp. Combat is based around not having over 100 Block rather than taking damage
//Remove all powers this applied on transformation. Unaffected by Fascination
//Starting move: Apply Barricade, "Turn start with 100 or more Block: Lose all Block and Hair Clip Apple gains 50 Beat of Death"
//Walk > Jog > Sprint
//Walk: 10(Hand size) B to player. 32 D
//Jog: (Player B) B to player. 8 DX7. Gain 5 Str
//Sprint: Player has 5 Momentary After Image while intent is decided. 100 D