package spireMapOverhaul.zones.CosmicEukotranpha.powers;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
public class TakeDamageAtNextTurnEndPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(TakeDamageAtNextTurnEndPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;
    public TakeDamageAtNextTurnEndPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.DEBUFF;this.amount=amount;updateDescription();}
    @Override public void atEndOfTurn(boolean isPlayer){dmg(owner,amount);remThis();}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];}
    @Override public AbstractPower makeCopy(){return new TakeDamageAtNextTurnEndPower(owner,source,amount);}}

//Take X D at next turn end