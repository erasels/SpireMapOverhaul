package spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.getRanMon;
public class PlayTopCardsOfDeckNextTurnEndPower extends BasePower implements CloneablePowerInterface{
public static final String POWER_ID=SpireAnniversary6Mod.makeID(PlayTopCardsOfDeckNextTurnEndPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
public int qqq=0;
public PlayTopCardsOfDeckNextTurnEndPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
	this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=amount;updateDescription();}
@Override public void atEndOfTurn(boolean isPlayer){for(int i=0;i<amount;i++){atb(new PlayTopCardAction(getRanMon(),false));}remThis();}
@Override public void updateDescription(){description=amount+DESCRIPTIONS[0];}
@Override public AbstractPower makeCopy(){return new PlayTopCardsOfDeckNextTurnEndPower(owner,source,amount);}}
