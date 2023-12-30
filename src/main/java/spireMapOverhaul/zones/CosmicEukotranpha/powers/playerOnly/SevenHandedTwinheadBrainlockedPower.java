package spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.seek;
public class SevenHandedTwinheadBrainlockedPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(SevenHandedTwinheadBrainlockedPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;
    public SevenHandedTwinheadBrainlockedPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;if(amount<1){this.type=PowerType.DEBUFF;}else{this.type=PowerType.BUFF;}this.amount=amount;updateDescription();}
    @Override public void atStartOfTurn(){poO(new NoDrawPower(owner));seek(amount);}
    @Override public void atStartOfTurnPostDraw(){atb(new RemoveSpecificPowerAction(owner,owner,"No Draw"));}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];}
    @Override public AbstractPower makeCopy(){return new SevenHandedTwinheadBrainlockedPower(owner,source,amount);}}


//Don't draw at turn start (Gain No Draw at turn start before drawing, Then lose it at turn start after drawing)

//Gain No Draw at turn start before drawing. Seek 3 at turn start