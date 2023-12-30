package spireMapOverhaul.zones.CosmicEukotranpha.powers.frameworks;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;

public class LosePowerAtNextTurnEndPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(LosePowerAtNextTurnEndPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;public String powerID;public String des;public String poPath;int amount=0;public boolean toZero;
    public LosePowerAtNextTurnEndPower(AbstractCreature owner,AbstractCreature source,String powerID,String description,String poPath){
        this(owner,source,powerID,description,poPath,-1,false);}
    public LosePowerAtNextTurnEndPower(AbstractCreature o,AbstractCreature s,String po,String d,String poPath,int amount,boolean toZero){super(POWER_ID,true);this.owner=o;this.source=s;
        this.powerID=po;this.des=d;this.poPath=poPath;this.type=PowerType.DEBUFF;this.toZero=toZero;
        this.canGoNegative=toZero;this.isTurnBased=false;this.amount=amount;updateDescription();}
    @Override public void atEndOfTurn(boolean isPlayer){if(amount==-1){addToBot(new RemoveSpecificPowerAction(owner,owner,powerID));
    }else{if(toZero){if(owner.hasPower(powerID)){addToBot(new ReducePowerAction(owner,owner,powerID,Math.min(amount,owner.getPower(powerID).amount)));}
    }else{addToBot(new ReducePowerAction(owner,owner,powerID,amount));}}remThis();updateDescription();}
    @Override public void updateDescription(){
        if(amount==-1){description=DESCRIPTIONS[0]+powerID+DESCRIPTIONS[2];
        }else{if(!toZero){description=DESCRIPTIONS[0]+amount+DESCRIPTIONS[1]+powerID+DESCRIPTIONS[2];
            }else{description=DESCRIPTIONS[0]+amount+DESCRIPTIONS[1]+powerID+DESCRIPTIONS[2]+DESCRIPTIONS[3];}}}
    @Override public AbstractPower makeCopy(){return new LosePowerAtNextTurnEndPower(owner,source,poPath,des,des,amount,toZero);}}

//"${ModID}:LosePowerAtNextTurnEndPower":{"NAME":"Losing Power",
//    "DESCRIPTIONS":["Lose "," "," at next turn end"," (to match 0)"]},

