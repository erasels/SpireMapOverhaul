package spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.curses.Clumsy;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.makeInDeck;

public class HairClipAppleWillAStuChipperEAntlionPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(HairClipAppleWillAStuChipperEAntlionPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;
    public HairClipAppleWillAStuChipperEAntlionPower(AbstractCreature o,AbstractCreature s){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=-1;updateDescription();}
    @Override public void atStartOfTurn(){if(owner.hasPower("CosmicZone:FascinationPower")){makeInDeck(new Clumsy());}}
    @Override public void updateDescription(){description=DESCRIPTIONS[0];}
    @Override public AbstractPower makeCopy(){return new HairClipAppleWillAStuChipperEAntlionPower(owner,source);}}

//While Fascinated: Add Clumsy to deck at turn end