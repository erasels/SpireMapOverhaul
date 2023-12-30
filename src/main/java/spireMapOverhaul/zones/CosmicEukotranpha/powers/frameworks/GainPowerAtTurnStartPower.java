package spireMapOverhaul.zones.CosmicEukotranpha.powers.frameworks;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;

public class GainPowerAtTurnStartPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(GainPowerAtTurnStartPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;public AbstractPower power;public String des;public String poPath;public int turns;public int toMatch;
    public GainPowerAtTurnStartPower(AbstractCreature owner,AbstractCreature source,AbstractPower power,String description,String poPath){
        this(owner,source,power,description,poPath,-1,-1);}
    public GainPowerAtTurnStartPower(AbstractCreature o,AbstractCreature s,AbstractPower po,String d,String poPath,int turns,int toMatch){super(POWER_ID,true);this.owner=o;this.source=s;
        this.power=po;this.des=d;this.poPath=poPath;this.type=po.type;
        this.canGoNegative=false;this.isTurnBased=false;this.turns=turns;this.toMatch=toMatch;this.amount=-1;updateDescription();}
    @Override public void atStartOfTurn(){if(turns!=-1){turns--;if(turns<1){remThis();}}
        if(toMatch==-1){poO(power);//this doesn't work with negative toMatch
        }else{if(owner.hasPower(power.ID)){
            if(toMatch>=amount+owner.getPower(power.ID).amount){poO(power);
            }else{AbstractPower power0=power;power0.amount=toMatch-owner.getPower(power.ID).amount;poO(power0);}
        }else{AbstractPower power0=power;power0.amount=toMatch;poO(power0);}
        }updateDescription();}
    @Override public void updateDescription(){description=DESCRIPTIONS[0];if(!power.canGoNegative&&amount!=-1){description+=power.amount+DESCRIPTIONS[1];}description+=power.name;
        if(turns==-1){description+=DESCRIPTIONS[2];if(toMatch!=-1){description+=DESCRIPTIONS[3]+toMatch;}
        }else{description+=DESCRIPTIONS[4]+turns+DESCRIPTIONS[5];if(toMatch!=-1){description+=DESCRIPTIONS[6]+toMatch;}}}
    @Override public AbstractPower makeCopy(){return new GainPowerAtTurnStartPower(owner,source,power,des,poPath,turns,toMatch);}}
//"${ModID}:GainPowerAtTurnStartPower":{"NAME":"Gaining Power",
//    "DESCRIPTIONS":["Gain "," "," at turn start "," to match ",
//      " at next "," turn starts"," to match "]},

//"DESCRIPTIONS":["Gain "," at turn start (Amount: ",")"," to match ",")",
//      " at next "," turn starts (Amount: ",")"," to match ",")"]},

//description=DESCRIPTIONS[0]+power.name;
//        if(turns==-1){if(toMatch==-1){description+=DESCRIPTIONS[1]+power.amount+DESCRIPTIONS[2];
//            }else{description+=DESCRIPTIONS[1]+power.amount+DESCRIPTIONS[3]+toMatch+DESCRIPTIONS[4];}
//        }else{if(toMatch==-1){description+=DESCRIPTIONS[5]+turns+DESCRIPTIONS[6]+power.amount+DESCRIPTIONS[7];
//            }else{description+=DESCRIPTIONS[5]+turns+DESCRIPTIONS[6]+power.amount+DESCRIPTIONS[8]+toMatch+DESCRIPTIONS[9];}}

