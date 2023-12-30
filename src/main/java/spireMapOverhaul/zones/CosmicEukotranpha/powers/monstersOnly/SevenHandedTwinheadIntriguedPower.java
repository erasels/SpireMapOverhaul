package spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnDrawPileShufflePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
public class SevenHandedTwinheadIntriguedPower extends BasePower implements CloneablePowerInterface,OnDrawPileShufflePower{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(SevenHandedTwinheadIntriguedPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;
    public SevenHandedTwinheadIntriguedPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=amount;updateDescription();}
    @Override public void onShuffle(){poO(new StrengthPower(owner,amount));}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];}
    @Override public AbstractPower makeCopy(){return new SevenHandedTwinheadIntriguedPower(owner,source,amount);}}


//When deck is shuffled: Gain 7 Str. "I want to see your Powers, and for you to Shuffle our minds"