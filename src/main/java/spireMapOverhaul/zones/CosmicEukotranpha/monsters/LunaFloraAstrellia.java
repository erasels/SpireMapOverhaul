package spireMapOverhaul.zones.CosmicEukotranpha.monsters;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.GainOrbSlotsToMatchEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.LunaFantasiaChangeMagicNumberEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.LunaFloraAstrelliaSummonInsectEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.LunaFloraAstrelliaSummonQueenEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.special.LunaFantasia;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.LunaFloraAstrelliaLunaBarrierPower;

import java.util.ArrayList;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class LunaFloraAstrellia extends CosmicZoneMonster{public LunaFloraAstrellia(){this(-50.0F,-300.0F);}public static final String ID=
        SpireAnniversary6Mod.makeID(LunaFloraAstrellia.class.getSimpleName());private static final MonsterStrings monsterStrings=CardCrawlGame.languagePack.getMonsterStrings(ID);public static final String NAME=monsterStrings.NAME;public static final String[]MOVES=monsterStrings.MOVES;public static final String[]DIALOG=monsterStrings.DIALOG;
public int clawsSeen=0;public int A=1;public int B=1;public int C=1;public int D=0;public int E=0;
public static float[]xPos;public static float[]yPos;public AbstractMonster[]insects=new AbstractMonster[7];
public LunaFloraAstrellia(float x,float y){super(NAME,ID,80,0.0F,-15.0F,380.0F,290.0F,null,x,y);MOVERS=MOVES;
    this.type=EnemyType.NORMAL;hpRange(20,20,30,30);A=1;B=1;C=1;D=0;E=0;
    xPos=new float[]{-466.0F,-280.0F,-466.0F,-280.0F,-466.0F,-280.0F,240.0F};
    yPos=new float[]{4.0F,6.0F,224.0F,226.0F,444.0F,446.0F,2.0F};
    this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/LunaFloraAstrellia/LunaFloraAstrelliaHyst0.png"));
    //Normal: 0,2,7,17. Elite: 0,3,8,18. Boss: 0,4,9,19
    //Each insect killed gains 1 point
    //Fascination gives +5 points
    //Queen Kill (Hysteric Surge) rewards a relic
    
    
    //Reworks:
    //B: 24B>12B
    //C: 30>20 (23)
    //E: 40B>20B
    //F: 30>20
    
    
    //Luna-Flora Astrellia
    //30 hp. 10 Luna-Barrier-{Can't lose more than 10 hp in a turn, Excess hp loss is applied to this as negative momentary Str}
    //Beckoning Scent-{Summons an Insect after this does it's intent}. Rng: 2/2/2 Reduced by 1 when used, Increased to match 2 when not used
    //A: Shuffle 2 Luna Fantasias to deck (A17: 3)
    //B: 8 DX3. 24 B when move is decided (8,9,9,10)
    //C: Give other monsters 30 temp hp (A17: 33)
    genMovePlus("Water Droplet Reflections",0,Intent.DEBUFF);
    genMovePlus("Triple Chomps",aM(8,9,9,10),Intent.ATTACK,3);
    genMovePlus("Pollen Pulse",0,Intent.BUFF);
    //Fascination: First move in Fascination is never E
    //D: Add 2 Luna Fantasias to top of deck (A17: and 1 to deck)
    //E: 40 D. 40 B when move is decided (40,44,44,50)
    //F: Give other monsters 30 temp hp. Summon 2 Insects (A17: 33)
    genMovePlus("Eclipse",0,Intent.STRONG_DEBUFF);
    genMovePlus("Vine Slam",aM(40,44,44,50),Intent.ATTACK);
    genMovePlus("Sweet Scent",0,Intent.BUFF);
    //G: Used when 6 Insects exist at turn start. Hysteric Surge. Summon the Queen. Gain 4(Hp) Temp hp. Heal to full. Give player 2 energy next turn and 2 draw at each turn start and orb slots to match 6. Shuffle 4 Luna Fantasias into deck. Change effect of Luna Fantasias into positive effects that channel orbs
    genMovePlus("Hysteric Surge",0,Intent.MAGIC);
    //Hyseria: First move after Hysteric Surge is never I
    //H: Shuffle 4 Luna Fantasias to deck. Give the player 2 Focus. Give all allies 2 Buffer (A17: 3 Buffer)
    //I: 40 DX2. 40 B when move is decided. Give the player 2 Strength when move is decided (40,44,44,50)
    //J: Give other monsters 40 temp hp. Summon 2 Insects. Give the player 2 Dexterity (A17: 45)
    genMovePlus("Lunar Blinding Lights",0,Intent.DEFEND_BUFF);
    genMovePlus("Vine Whacking",aM(40,44,44,50),Intent.ATTACK,2);
    genMovePlus("Honey Burst",0,Intent.BUFF);
    
    //Luna Fantasia
    //1 cost. Status. Exhaust. Turn end in hand: Lose 4 hp
    //Transformed:
    //10 D, Channel orb based on enemy (Each Insect, Queen, Other) targeted. Exhaust. Turn end in hand: Lose 2 hp
    //
    //
    //Insects: 40 hp. Passive power. Alternates between A first (Aggressive) then B (Something else). Insects are summoned in Solar System order (From the Sun). Unaffected by Zone passives. Gives player 2 Regen on death. Minion
    //Hydoail, Mercury
    //A: 14 D. Player draws 2 next turn
    //B: Gain 4 Plated Armor
    //Goldire, Venus
    //A: 14 D. Player gains 2 Temp hp
    //B: Gain 2 Buffer
    //Terniff, Earth
    //A: 14 D. Player Retains 2 next turn end
    //B: Gain 4 Thorns
    //Pyergy, Mars
    //A: 14 D. Player gains 4 Foresight
    //B: Gain 4 Strength
    //Barceq, Jupiter
    //A: 14 D. Player gains 2 energy next turn
    //B: Gain 1 After Image
    //Drittu, Saturn
    //A: 14 D. Player gains 1 Curiosity
    //B: Gain 2 Feel No Pain
    //Heazath, George
    //A: 14 D. Player gains 1 Ritual
    //B: Gain 1 Beat of Death
    //Ocianyvy, Neptune
    //A: 14 D. Apply 2 Constricted
    //B: Gain 4 Artifact
    //
    //
    //Queen:
    //200 hp. 4 Malleable. Slow. Unaffected by Zone passives. If Luna-Flora Astrellia is dead, Summons an Insect after this does it's intent
    //Do these
    //4 DX10. Apply Double Tap & Wave of the Hand. Next turn, On Exhaust: Draw 1
    //22 DX2. Apply Burst & 2 Rage. Next turn: On play Atk: Apply 2 Poison to foes
    //14 D. Apply -14 Momentary Str. Apply Amplify & 4 Rebound. Next turn, On gain B: 1 D to foes
    //Then this
    //Apply positive Wraith Form. Shuffle 4 Cosmic cards into deck. Next turn, Draw 4 and gain 4 energy next turn and Cards played are Exhausted
    //
    //
    //Dialogue:
    //"Let them "
}
public void usePreBattleAction(){testCosmicZonePassive();poS(new LunaFloraAstrelliaLunaBarrierPower(this,this,10));}
public void takeTurn(){switch(this.nextMove){
    //A: Shuffle 2 Luna Fantasias to deck (A17: 3)
    case 0:makeInDeck(new LunaFantasia(),2+rBAI(asc>16));A=0;B=1;C=1;break;
    //B: 8 DX3. 24 B when move is decided (8,9,9,10)
    case 1:monsterDmg();monsterDmg();monsterDmg();A=1;B=0;C=1;break;
    //C: Give other monsters 30 temp hp (A17: 33)
    case 2:for(AbstractMonster mo:monsterList()){if(mo!=this){atb(new AddTemporaryHPAction(mo,this,20+rBAI(asc>17,3)));}}A=1;B=1;C=0;break;
    //D: Add 2 Luna Fantasias to top of deck (A17: and 1 to deck)
    case 3:if(asc>16){makeInDeck(new LunaFantasia(),1);}makeInDeck(new LunaFantasia(),2,1);A=0;D=1;C=1;break;
    //E: 40 D. 40 B when move is decided (40,44,44,50)
    case 4:monsterDmg();A=1;D=0;C=1;break;
    //F: Give other monsters 30 temp hp. Summon 2 Insects (A17: 33)
    case 5:for(AbstractMonster mo:monsterList()){if(mo!=this){atb(new AddTemporaryHPAction(mo,this,20+rBAI(asc>17,3)));}}
        atb(new LunaFloraAstrelliaSummonInsectEffect(insects));atb(new LunaFloraAstrelliaSummonInsectEffect(insects));A=1;D=1;C=0;break;
    //G: Summon the Queen. Gain 4(Hp) Temp hp. Heal to full. Give player 2 energy next turn and 2 draw at each turn start and orb slots to match 6. Shuffle 4 Luna Fantasias into deck. Change effect of Luna Fantasias into positive effects that channel orbs
    case 6:atb(new LunaFloraAstrelliaSummonQueenEffect(insects));atb(new AddTemporaryHPAction(this,this,this.currentHealth*4));heal(maxHealth);
        pofP(new EnergizedPower(AbstractDungeon.player,2));pofP(new DrawCardNextTurnPower(AbstractDungeon.player,2));atb(new GainOrbSlotsToMatchEffect(6));
        makeInDeck(new LunaFantasia(),4);atb(new LunaFantasiaChangeMagicNumberEffect());hysteria=2;break;
    //H: Shuffle 4 Luna Fantasias to deck. Give the player 2 Focus. Give all allies 2 Buffer (A17: 3 Buffer)
    case 7:makeInDeck(new LunaFantasia(),4);pofP(new FocusPower(AbstractDungeon.player,2));for(AbstractMonster mo:monsterList()){poT(mo,this,new BufferPower(mo,2+rBAI(asc>17)));}A=0;E=1;C=1;break;
    //I: 40 DX2. 40 B when move is decided. Give the player 2 Strength when move is decided (40,44,44,50)
    case 8:monsterDmg();monsterDmg();A=1;E=0;C=1;break;
    //J: Give other monsters 40 temp hp. Summon 2 Insects. Give the player 2 Dexterity (A17: 45)
    case 9:for(AbstractMonster mo:monsterList()){if(mo!=this){atb(new AddTemporaryHPAction(mo,this,40+rBAI(asc>17,5)));}}atb(new LunaFloraAstrelliaSummonInsectEffect(insects));atb(new LunaFloraAstrelliaSummonInsectEffect(insects));pofP(new DexterityPower(AbstractDungeon.player,2));A=1;E=1;C=0;break;
    default:loggeer("ERROR, takeTurn outside switch, Luna-Flora Astrellia");
}atb(new LunaFloraAstrelliaSummonInsectEffect(insects));lastMoveUpdt(nextMove);rollerMove();}
public void startOfTurnIntentCheck(){switch(nextMove){
    case 1:monsterBlck(12);break;
    case 4:monsterBlck(20);break;
    case 7:monsterBlck(40);pofP(new StrengthPower(AbstractDungeon.player,2));break;}}
protected void getMove(int num){dontRedecide=false;
    if(hysteria<2){int w;for(w=0;w<insects.length;++w){if(insects[w]==null||insects[w].isDying){break;}}
        if(w>5){decMvePlus("Hysteric Surge");return;}}
    if(hysteria==2){applicableMoveList=new ArrayList<>();
        if(A>0){applicableMoveList.add("Lunar Blinding Lights");}
        if(E>0){applicableMoveList.add("Vine Whacking");}
        if(C>0){applicableMoveList.add("Honey Burst");}
        decMvePlus(getAppMove(applicableMoveList));
    }else if(fasc()){applicableMoveList=new ArrayList<>();
        if(A>0){applicableMoveList.add("Eclipse");}
        if(D>0){applicableMoveList.add("Vine Slam");}
        if(C>0){applicableMoveList.add("Sweet Scent");}
        decMvePlus(getAppMove(applicableMoveList));
    }else{applicableMoveList=new ArrayList<>();
        if(A>0){applicableMoveList.add("Water Droplet Reflections");}
        if(B>0){applicableMoveList.add("Triple Chomps");}
        if(C>0&&moveCount>0){applicableMoveList.add("Pollen Pulse");}
        decMvePlus(getAppMove(applicableMoveList));}
    moveChangeCheck();}
@Override public void update(){super.update();
    if(hysteria==2){upTicks++;if(upTicks>60){switch(currImg){
            case 0:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/LunaFloraAstrellia/LunaFloraAstrelliaHyst1.png"));upTicks=50;currImg++;break;
            case 1:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/LunaFloraAstrellia/LunaFloraAstrelliaHyst2.png"));upTicks=50;currImg++;break;
            case 2:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/LunaFloraAstrellia/LunaFloraAstrelliaHyst3.png"));upTicks=50;currImg++;break;
            case 3:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/LunaFloraAstrellia/LunaFloraAstrelliaHyst4.png"));upTicks=50;currImg++;break;
            case 4:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/LunaFloraAstrellia/LunaFloraAstrelliaHyst5.png"));upTicks=50;currImg++;break;
            case 5:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/LunaFloraAstrellia/LunaFloraAstrelliaHyst0.png"));upTicks=0;currImg=0;break;}}
    }else if(fasc()){this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/LunaFloraAstrellia/LunaFloraAstrelliaFasc.png"));
    }else{this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/LunaFloraAstrellia/LunaFloraAstrelliaIdle.png"));}}
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
}
