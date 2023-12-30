package spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Claw;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.Claumissier;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
public class ClaumissierSharpClawsPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(ClaumissierSharpClawsPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;
    public ClaumissierSharpClawsPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=true;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=amount;updateDescription();}
    @Override public void onPlayCard(AbstractCard card,AbstractMonster m){if(card instanceof Claw){poO(new StrengthPower(owner,amount));if(owner instanceof Claumissier){((Claumissier)owner).clawsSeen++;((Claumissier)owner).clawsSeenThisTurn++;((Claumissier)owner).clawSeen();}}updateDescription();}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];}
    @Override public AbstractPower makeCopy(){return new ClaumissierSharpClawsPower(owner,source,amount);}}




//Gain 2 Str on play Claw. "I, Claumissier, Want to see at least 5 Claws"