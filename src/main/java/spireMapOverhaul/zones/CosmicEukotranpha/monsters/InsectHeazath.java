package spireMapOverhaul.zones.CosmicEukotranpha.monsters;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.GoodbyeNewFriendPower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.loggeer;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class InsectHeazath extends CosmicZoneMonster{public InsectHeazath(){this(0.0F,0.0F);}public static final String ID=
        SpireAnniversary6Mod.makeID(InsectHeazath.class.getSimpleName());private static final MonsterStrings monsterStrings=CardCrawlGame.languagePack.getMonsterStrings(ID);public static final String NAME=monsterStrings.NAME;public static final String[]MOVES=monsterStrings.MOVES;public static final String[]DIALOG=monsterStrings.DIALOG;
    public int A=0;
    public InsectHeazath(float x,float y){super(NAME,ID,80,0.0F,-15.0F,120.0F,80.0F,null,x,y);MOVERS=MOVES;
        this.type=EnemyType.NORMAL;hpRange(40,40,42,42);A=0;
        //Normal: 0,2,7,17. Elite: 0,3,8,18. Boss: 0,4,9,19
        //Insects: 40 hp. Unaffected by Zone passives. Minion. Goodbye New Friend-{Gives player 2 Regen on death}
        //AI: Alternates between A first (Aggressive) then B (Something else). Insects are summoned in Solar System order (From the Sun)
        //Hydoail, Mercury | A: 14 D. Player draws 2 next turn | B: Gain 4 Plated Armor
        //Goldire, Venus | A: 14 D. Player gains 2 Temp hp | B: Gain 2 Buffer
        //Terniff, Earth | A: 14 D. Player Retains 2 next turn end | B: Gain 4 Thorns
        //Pyergy, Mars | A: 14 D. Player gains 4 Foresight | B: Gain 4 Strength
        //Barceq, Jupiter | A: 14 D. Player gains 2 energy next turn | B: Gain 1 After Image  --  Actually 1 Energy 2 After Image
        //Drittu, Saturn | A: 14 D. Player gains 1 Curiosity | B: Gain 2 Feel No Pain
        //Heazath, George | A: 14 D. Player gains 1 Ritual | B: Gain 1 Beat of Death
        //Ocianyvy, Neptune | A: 14 D. Apply 2 Constricted | B: Gain 4 Artifact
        genMovePlus("Cosmic Buzz",aM(14,15,15,17),Intent.ATTACK);
        genMovePlus("Orbit",aM(8,9,9,10),Intent.BUFF);
    }
    public void usePreBattleAction(){poS(new GoodbyeNewFriendPower(this,this,2));}
    public void takeTurn(){switch(this.nextMove){
        case 0:monsterDmg();pofP(new RitualPower(AbstractDungeon.player,1,true));A=1;break;
        case 1:poS(new BeatOfDeathPower(this,1));A=0;break;
        default:loggeer("ERROR, takeTurn outside switch, Insect Heazath, George");
    }lastMoveUpdt(nextMove);rollerMove();}
    protected void getMove(int num){dontRedecide=false;if(A==0){decMvePlus("Cosmic Buzz");}else{decMvePlus("Orbit");}moveChangeCheck();}
    public void changeState(String key){}
    public void damage(DamageInfo info){super.damage(info);if(info.owner!=null&&info.type!=DamageType.THORNS&&info.output>0){}}
    public void die(){super.die();}
    //Dialogue:
}
