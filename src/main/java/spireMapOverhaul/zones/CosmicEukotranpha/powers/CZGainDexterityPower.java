package spireMapOverhaul.zones.CosmicEukotranpha.powers;import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
public class CZGainDexterityPower extends BasePower implements CloneablePowerInterface{
public static final String POWER_ID=SpireAnniversary6Mod.makeID("CZGainDexterityPower");private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
private static int idOffset=0;public int shoo=0;
public CZGainDexterityPower(AbstractCreature o,AbstractCreature s,int am){super(POWER_ID);this.owner=o;this.source=s;
	this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.DEBUFF;this.amount=am;
	if(this.amount>=999){this.amount=999;}if(this.amount<=-999){this.amount=-999;}updateDescription();}
public void playApplyPowerSfx(){CardCrawlGame.sound.play("POWER_SHACKLE",0.05F);}
public void stackPower(int stackAmount){this.fontScale=8.0F;this.amount+=stackAmount;if(this.amount==0){this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, "anniv6:CZGainDexterityPower"));}
	if(this.amount>=999){this.amount=999;}if(this.amount<=-999){this.amount=-999;}}
public void reducePower(int reduceAmount) {this.fontScale=8.0F;this.amount-=reduceAmount;
	if(this.amount==0){this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, NAME));}
	if(this.amount>=999){this.amount=999;}if(this.amount<=-999){this.amount=-999;}}
public void updateDescription(){this.description=DESCRIPTIONS[0]+this.amount+DESCRIPTIONS[1];}
public void atEndOfTurn(boolean isPlayer){this.flash();this.addToBot(new ApplyPowerAction(this.owner,this.owner,new DexterityPower(this.owner,this.amount),this.amount));
	this.addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,"anniv6:CZGainDexterityPower"));}
@Override public AbstractPower makeCopy(){return new CZGainDexterityPower(owner,source,amount);}}

