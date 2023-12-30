package spireMapOverhaul.zones.CosmicEukotranpha.powers;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.loggeer;
public class WillGainFascinationPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID("WillGainFascinationPower");private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;public int shoo=0;
    public WillGainFascinationPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=true;this.type=PowerType.BUFF;this.amount=amount;updateDescription();}
    @Override public void atStartOfTurn(){if(GameActionManager.turn==6){loggeer("GameActionTurn=6");}reducePower();}
    @Override public void onRemove(){poO(new FascinationPower(owner,owner));}
    @Override public void updateDescription(){description="temporary power. Gains Fascination";}
    @Override public AbstractPower makeCopy(){return new WillGainFascinationPower(owner,source,amount);}}

//HAHAHAHAHA