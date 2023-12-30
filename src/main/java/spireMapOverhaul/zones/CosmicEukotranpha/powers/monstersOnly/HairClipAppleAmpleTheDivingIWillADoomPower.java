package spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
public class HairClipAppleAmpleTheDivingIWillADoomPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(HairClipAppleAmpleTheDivingIWillADoomPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;
    public HairClipAppleAmpleTheDivingIWillADoomPower(AbstractCreature o,AbstractCreature s){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=-1;updateDescription();}
    public int onAttackedToChangeDamage(DamageInfo info,int damageAmount){if(damageAmount>owner.currentHealth){damageAmount=owner.currentHealth-1;}updateDescription();return damageAmount;}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]/*+(owner.currentHealth-1)+DESCRIPTIONS[1]*/;}
    @Override public AbstractPower makeCopy(){return new HairClipAppleAmpleTheDivingIWillADoomPower(owner,source);}}


//Can only lose up to 77 hp, Then Enrages if not using Hysteric Surge