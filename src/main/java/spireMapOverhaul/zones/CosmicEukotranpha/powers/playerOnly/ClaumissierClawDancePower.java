package spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Claw;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
public class ClaumissierClawDancePower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(ClaumissierClawDancePower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;public int cards=0;
    public ClaumissierClawDancePower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID,true);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.DEBUFF;cards=0;this.amount=amount;updateDescription();}
    @Override public void onPlayCard(AbstractCard card,AbstractMonster m){cards++;if(cards==3){flashWithoutSound();if(card instanceof Claw){dmg(owner,amount/2);}else{dmg(owner,amount);}remThis();}updateDescription();}
    @Override public void atEndOfTurn(boolean isPlayer){remThis();}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+cards+DESCRIPTIONS[1]+amount+DESCRIPTIONS[2];}
    @Override public AbstractPower makeCopy(){return new ClaumissierClawDancePower(owner,source,amount);}}



//On play 3rd card this turn: Take X D, Half if Claw

//On play 3rd card this turn: Take X D, Half if Claw. Foes gain Y Str, 0 if Claw