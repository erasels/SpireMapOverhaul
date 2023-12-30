package spireMapOverhaul.zones.CosmicEukotranpha.powers;import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
public class ImperfectionPower extends BasePower implements CloneablePowerInterface,OnReceivePowerPower{
public static final String POWER_ID=SpireAnniversary6Mod.makeID(ImperfectionPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
private static int idOffset=0;
public ImperfectionPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
	this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.DEBUFF;this.amount=amount;updateDescription();}
@Override public boolean onReceivePower(AbstractPower abstractPower,AbstractCreature abstractCreature,AbstractCreature abstractCreature1){
	if(abstractPower.type==PowerType.BUFF&&abstractPower.owner==owner&&!(abstractPower instanceof FascinationPower)){this.addToBot(new RemoveSpecificPowerAction(owner,owner,abstractPower));reducePower();}return false;}
@Override public void updateDescription(){description=DESCRIPTIONS[0];}
@Override public AbstractPower makeCopy(){return new ImperfectionPower(owner,source,amount);}}





//On gain buff: Lose it and 1 of this