package spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.makeInDeck;

public class HairClipAppleDepletedPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(HairClipAppleDepletedPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;
    public HairClipAppleDepletedPower(AbstractCreature o,AbstractCreature s){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.DEBUFF;this.amount=-1;updateDescription();}
    @Override public void atEndOfTurn(boolean isPlayer){if(EnergyPanel.totalCount==0){poO(new BlurPower(owner,1));}}
    @Override public void atStartOfTurn(){if(owner.currentBlock>19){makeInDeck(new VoidCard(),2);}}
    @Override public void updateDescription(){description=DESCRIPTIONS[0];}
    @Override public AbstractPower makeCopy(){return new HairClipAppleDepletedPower(owner,source);}}


//Gain 1 Blur at turn end if you have no energy at turn end. Turn start with 20 or more B: Add 2 Voids to deck