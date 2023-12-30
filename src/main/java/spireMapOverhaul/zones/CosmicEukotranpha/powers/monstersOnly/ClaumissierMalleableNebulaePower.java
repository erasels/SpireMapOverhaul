package spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Claw;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;
public class ClaumissierMalleableNebulaePower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(ClaumissierMalleableNebulaePower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;public int initialAmount;
    public ClaumissierMalleableNebulaePower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID,true);this.owner=o;this.source=s;
        this.canGoNegative=true;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=amount;initialAmount=amount;updateDescription();}
    @Override public void onPlayCard(AbstractCard card,AbstractMonster m){if(card.type==AbstractCard.CardType.ATTACK&&!(card instanceof Claw)){blck(amount);amount++;}if(card instanceof Claw){atb(new RemoveAllBlockAction(owner,source));}updateDescription();}
    @Override public void atEndOfTurn(boolean isPlayer){amount=initialAmount;updateDescription();}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+amount+DESCRIPTIONS[1]+initialAmount+DESCRIPTIONS[2];}
    @Override public AbstractPower makeCopy(){return new ClaumissierMalleableNebulaePower(owner,source,amount);}}

//Reworked: On play non-Claw Atk: 3 B, Increase it by 1 this turn only. [] On play Claw: Lose B
//After on play non-Claw Atk: 3 B, Increase it by 1 this turn only. [] On play Claw: Lose B