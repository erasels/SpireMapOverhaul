package spireMapOverhaul.zones.CosmicEukotranpha.powers.frameworks;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.CosmicZonePlayTargetEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;


import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.getRanMon;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class PlayCardAtTurnStartPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(PlayCardAtTurnStartPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;public AbstractCard card;public String des;public String poPath;public int turns;public int times;
    public PlayCardAtTurnStartPower(AbstractCreature owner,AbstractCreature source,AbstractCard card,String description,String poPath){
        this(owner,source,card,description,poPath,-1,1);}
    public PlayCardAtTurnStartPower(AbstractCreature o,AbstractCreature s,AbstractCard card,String d,String poPath,int turns,int times){super(POWER_ID,true);this.owner=o;this.source=s;
        this.card=card;this.des=d;this.poPath=poPath;this.type=PowerType.BUFF;this.turns=turns;
        this.canGoNegative=true;this.isTurnBased=false;this.amount=-1;this.times=times;updateDescription();}
    @Override public void atStartOfTurn(){if(turns!=-1){turns--;if(turns<1){remThis();}}for(int i=0;i<times;i++){addToBot(new CosmicZonePlayTargetEffect(card,getRanMon()));}updateDescription();}
    @Override public void updateDescription(){if(turns==-1){
            if(times!=1){description=DESCRIPTIONS[0]+card.name+DESCRIPTIONS[1];
            }else{description=DESCRIPTIONS[0]+card.name+DESCRIPTIONS[4]+times+DESCRIPTIONS[5];}
        }else{if(times!=1){description=DESCRIPTIONS[0]+card.name+DESCRIPTIONS[2]+turns+DESCRIPTIONS[3];
            }else{description=DESCRIPTIONS[0]+card.name+DESCRIPTIONS[2]+turns+DESCRIPTIONS[3]+DESCRIPTIONS[4]+times+DESCRIPTIONS[5];}}}
    @Override public AbstractPower makeCopy(){return new PlayCardAtTurnStartPower(owner,source,card,des,poPath,turns,times);}}

//"DESCRIPTIONS":["Play "," at next turn start",
//      " at next "," turns start",
//      " "," times"]},

