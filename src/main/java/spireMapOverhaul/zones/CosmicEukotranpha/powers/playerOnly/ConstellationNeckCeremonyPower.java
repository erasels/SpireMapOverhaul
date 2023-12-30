package spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.MusclePower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.sklsInGroup;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.stasInGroup;
public class ConstellationNeckCeremonyPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(ConstellationNeckCeremonyPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;
    public ConstellationNeckCeremonyPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=true;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=amount;updateDescription();}
    @Override public void atEndOfTurn(boolean isPlayer){poO(new MusclePower(owner,owner,amount*(sklsInGroup(p.hand)+stasInGroup(p.hand))));}
    @Override public void updateDescription(){description=DESCRIPTIONS[0];if(amount!=1){description+=amount;}description+=DESCRIPTIONS[1];}
    @Override public AbstractPower makeCopy(){return new ConstellationNeckCeremonyPower(owner,source,amount);}}



//Gain (Skls+Stas in hand) Muscle-{Increase attack D by 1 & Increase B from cards by 1 if player} at turn end