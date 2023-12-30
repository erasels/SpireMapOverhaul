package spireMapOverhaul.zones.CosmicEukotranpha.monsters;import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.WaveOfTheHandPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.frameworks.LosePowerAtNextTurnEndPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.LunaFloraAstrelliaQueenSolarSystemQueenPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly.DToFoesOnGainBUntilTurnEndPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly.ExhaustCardsPlayedUntilTurnEndPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly.PoisonToFoesOnPlayAtkUntilTurnEndPower;

import java.util.ArrayList;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class Queen extends CosmicZoneMonster{public Queen(){this(0.0F,0.0F);}public static final String ID=
        SpireAnniversary6Mod.makeID(Queen.class.getSimpleName());private static final MonsterStrings monsterStrings= CardCrawlGame.languagePack.getMonsterStrings(ID);public static final String NAME=monsterStrings.NAME;public static final String[]MOVES=monsterStrings.MOVES;public static final String[]DIALOG=monsterStrings.DIALOG;
    public int A=1;public int B=1;public int C=1;
    public Queen(float x,float y){super(NAME,ID,80,0.0F,-15.0F,380.0F,290.0F,null,x,y);MOVERS=MOVES;
        this.type=EnemyType.NORMAL;hpRange(200,200,214,214);A=1;B=1;C=1;
        //Normal: 0,2,7,17. Elite: 0,3,8,18. Boss: 0,4,9,19
        //200 hp. 4 Malleable. Slow. Unaffected by Zone passives. Solar System Queen-{If Luna-Flora Astrellia is dead, Summons an Insect after this does it's intent}
        //Do these
        //4 DX10. Apply Double Tap & Wave of the Hand, Momentary Dark Embrace
        //22 DX2. Apply Burst & 2 Rage. Next turn: On play Atk: Apply 2 Poison to foes
        //14 D. Apply -14 Momentary Str. Apply Amplify & 4 Rebound. Next turn: On gain B: 1 D to foes
        //Then this
        //Apply positive Wraith Form. Shuffle 4 Cosmic cards into deck. Next turn, Draw 4 and gain 4 energy next turn and Cards played are Exhausted
        genMovePlus("Queen's Buzz",4,Intent.ATTACK,10+rBAI(asc>16));
        genMovePlus("Double Hack",aM(22,24,24,27),Intent.ATTACK,2);
        genMovePlus("Jittery Noise",aM(14,15,15,17),Intent.ATTACK_DEBUFF);
        genMovePlus("Hive of Heaven",0,Intent.STRONG_DEBUFF);
    }
    public void usePreBattleAction(){poS(new MalleablePower(this,4));poS(new SlowPower(this,0));poS(new LunaFloraAstrelliaQueenSolarSystemQueenPower(this,this));}
    public void takeTurn(){switch(this.nextMove){
        //4 DX10. Apply Double Tap & Wave of the Hand, Momentary Dark Embrace
        case 0:monsterDmg();monsterDmg();monsterDmg();monsterDmg();monsterDmg();
            monsterDmg();monsterDmg();monsterDmg();monsterDmg();monsterDmg();if(asc>16){monsterDmg();}pofP(new DoubleTapPower(AbstractDungeon.player,1));pofP(new WaveOfTheHandPower(AbstractDungeon.player,1));pofP(new DarkEmbracePower(AbstractDungeon.player,1));pofP(new LosePowerAtNextTurnEndPower(AbstractDungeon.player,this,"Dark Embrace","","",-1,true));A=0;break;
        //22 DX2. Apply Burst & 2 Rage. Next turn: On play Atk: Apply 2 Poison to foes
        case 1:monsterDmg();monsterDmg();pofP(new BurstPower(AbstractDungeon.player,1));pofP(new RagePower(AbstractDungeon.player,2));pofP(new PoisonToFoesOnPlayAtkUntilTurnEndPower(AbstractDungeon.player,this,2));B=0;break;
        //14 D. Apply -14 Momentary Str. Apply Amplify & 4 Rebound. Next turn: On gain B: 1 D to foes
        case 2:monsterDmg();pofP(new StrengthPower(AbstractDungeon.player,-14));if(!AbstractDungeon.player.hasPower("Artifact")){pofP(new GainStrengthPower(AbstractDungeon.player,14));}pofP(new AmplifyPower(AbstractDungeon.player,1));pofP(new ReboundPower(AbstractDungeon.player));pofP(new ReboundPower(AbstractDungeon.player));pofP(new ReboundPower(AbstractDungeon.player));pofP(new ReboundPower(AbstractDungeon.player));pofP(new DToFoesOnGainBUntilTurnEndPower(AbstractDungeon.player,this,1));C=0;break;
        //Apply positive Wraith Form. Shuffle 4 Cosmic cards into deck. Next turn, Draw 4 and gain 4 energy next turn and Cards played are Exhausted
        case 3:pofP(new WraithFormPower(AbstractDungeon.player,1));makeInDeck(randomCosmicCardWeightedRNG());pofP(new DrawCardNextTurnPower(AbstractDungeon.player,4));pofP(new EnergizedPower(AbstractDungeon.player,4));pofP(new ExhaustCardsPlayedUntilTurnEndPower(AbstractDungeon.player,this,1));A=1;B=1;C=1;break;
        default:loggeer("ERROR, takeTurn outside switch, Queen");
    }lastMoveUpdt(nextMove);rollerMove();}
    protected void getMove(int num){dontRedecide=false;if(A==B&&B==C&&C==0){decMvePlus("Hive of Heaven");
        }else{applicableMoveList=new ArrayList<>();
            if(A>0){applicableMoveList.add("Queen's Buzz");}
            if(B>0){applicableMoveList.add("Double Hack");}
            if(C>0){applicableMoveList.add("Jittery Noise");}
            decMvePlus(getAppMove(applicableMoveList));}
        moveChangeCheck();}
    public void changeState(String key){}
    public void damage(DamageInfo info){super.damage(info);if(info.owner!=null&&info.type!=DamageType.THORNS&&info.output>0){}}
    public void die(){super.die();}
    //Dialogue:
}
