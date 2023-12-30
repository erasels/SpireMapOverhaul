package spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;

public class HairClipAppleMellowPacePower extends BasePower implements CloneablePowerInterface{
public static final String POWER_ID=SpireAnniversary6Mod.makeID(HairClipAppleMellowPacePower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
public int qqq=0;
public HairClipAppleMellowPacePower(AbstractCreature o,AbstractCreature s){super(POWER_ID);this.owner=o;this.source=s;
	this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=-1;updateDescription();}
@Override public void atEndOfTurn(boolean isPlayer){blck(p.hand.size()*10);remThis();}
@Override public void updateDescription(){description=DESCRIPTIONS[0];}
@Override public AbstractPower makeCopy(){return new HairClipAppleMellowPacePower(owner,source);}}

//Next turn end: 10(Hand size) B