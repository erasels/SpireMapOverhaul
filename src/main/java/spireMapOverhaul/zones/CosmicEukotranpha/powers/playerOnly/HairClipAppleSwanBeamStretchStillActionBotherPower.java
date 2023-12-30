package spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;

public class HairClipAppleSwanBeamStretchStillActionBotherPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(HairClipAppleSwanBeamStretchStillActionBotherPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;public int block;public int bod;
    public HairClipAppleSwanBeamStretchStillActionBotherPower(AbstractCreature o,AbstractCreature s,int block,int bod){super(POWER_ID,true);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=-1;this.block=block;this.bod=bod;updateDescription();}
    @Override public void atStartOfTurn(){if(owner.currentBlock>=block){atb(new RemoveAllBlockAction(owner,source));poT(source,new BeatOfDeathPower(source,bod));}}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+block+DESCRIPTIONS[1]+bod+DESCRIPTIONS[2];}
    @Override public AbstractPower makeCopy(){return new HairClipAppleSwanBeamStretchStillActionBotherPower(owner,source,block,bod);}}


//Turn start with 100 or more Block: Lose all Block and Hair Clip Apple gains 50 Beat of Death