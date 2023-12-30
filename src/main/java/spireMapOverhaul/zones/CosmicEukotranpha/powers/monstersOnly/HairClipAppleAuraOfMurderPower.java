package spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.draw;
public class HairClipAppleAuraOfMurderPower extends BasePower implements CloneablePowerInterface{
public static final String POWER_ID=SpireAnniversary6Mod.makeID(HairClipAppleAuraOfMurderPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
public int qqq=0;
public HairClipAppleAuraOfMurderPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
    this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=amount;updateDescription();}
@Override public void onPlayCard(AbstractCard card,AbstractMonster m){if(card.type==AbstractCard.CardType.ATTACK){draw(amount);}}
@Override public void updateDescription(){description=DESCRIPTIONS[0]+amount;}
@Override public AbstractPower makeCopy(){return new HairClipAppleAuraOfMurderPower(owner,source,amount);}}



//On Atk played: Player draws 2