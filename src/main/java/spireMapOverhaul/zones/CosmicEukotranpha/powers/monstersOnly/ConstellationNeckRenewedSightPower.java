package spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.MusclePower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;
public class ConstellationNeckRenewedSightPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(ConstellationNeckRenewedSightPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;public int atks;public int atksThisTurn;public int draa;
    public ConstellationNeckRenewedSightPower(AbstractCreature o,AbstractCreature s){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=-1;updateDescription();}
    @Override public void atEndOfTurn(boolean isPlayer){poO(new MusclePower(owner,owner,amount*(sklsInGroup(p.hand)+stasInGroup(p.hand))));}
    @Override public float atDamageFinalReceive(float damage,DamageInfo.DamageType type){if(owner.hasPower("CosmicZone:FascinationPower")&&hasPowAmount(p,"CosmicZone:MusclePower",5)){damage=damage/2;}return super.atDamageFinalReceive(damage,type);}
    @Override public int onLoseHp(int damageAmount){if(owner.hasPower("CosmicZone:FascinationPower")&&hasPowAmount(p,"CosmicZone:MusclePower",5)){atb(new ReducePowerAction(p,owner,"CosmicZone:FascinationPower",5));}return super.onLoseHp(damageAmount);}
    @Override public void updateDescription(){description=DESCRIPTIONS[0];}
    @Override public AbstractPower makeCopy(){return new ConstellationNeckRenewedSightPower(owner,source);}}



//While Fascinated: Player has 5 or more Muscle: Halve hp loss above 20 this receives. On lose hp: Player loses 5 Muscle