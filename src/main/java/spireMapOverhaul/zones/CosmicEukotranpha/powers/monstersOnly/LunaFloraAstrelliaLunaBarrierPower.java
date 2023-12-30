package spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
public class LunaFloraAstrelliaLunaBarrierPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(LunaFloraAstrelliaLunaBarrierPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;public int initialAmount;
    public LunaFloraAstrelliaLunaBarrierPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=amount;initialAmount=amount;updateDescription();}
    public int onAttackedToChangeDamage(DamageInfo info,int damageAmount){if(damageAmount>this.amount){
        poO(new StrengthPower(owner,amount-damageAmount));if(!owner.hasPower("Artifact")){poO(new GainStrengthPower(owner,-amount+damageAmount));}
        damageAmount=this.amount;}
        this.amount-=damageAmount;if(this.amount<0){this.amount=0;}
        this.updateDescription();return damageAmount;}
    public void atStartOfTurn(){this.amount=this.initialAmount;this.updateDescription();}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+initialAmount+DESCRIPTIONS[1]+(10-amount)+DESCRIPTIONS[2]+initialAmount+DESCRIPTIONS[3];}
    @Override public AbstractPower makeCopy(){return new LunaFloraAstrelliaLunaBarrierPower(owner,source,amount);}}

//Can't lose more than 10 hp in a turn, Excess hp loss is applied to this as negative momentary Str

//"${ModID}:LunaFloraAstrelliaLunaBarrierPower":{"NAME":"Luna Barrier",
//    "DESCRIPTIONS":["Can't lose more than "," hp in a turn (","/","). Excess hp loss is applied to this as negative momentary Str"]},