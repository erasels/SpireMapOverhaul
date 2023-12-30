package spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.monsterList;

public class PoisonToFoesOnPlayAtkUntilTurnEndPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(PoisonToFoesOnPlayAtkUntilTurnEndPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;
    public PoisonToFoesOnPlayAtkUntilTurnEndPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=amount;updateDescription();}
    public void onUseCard(AbstractCard card,UseCardAction action){if(card.type==AbstractCard.CardType.ATTACK&&amount>0){for(AbstractMonster mon:monsterList()){poT(mon,new PoisonPower(mon,owner,amount));}}}
    @Override public void atEndOfTurn(boolean isPlayer){remThis();}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];}
    @Override public AbstractPower makeCopy(){return new PoisonToFoesOnPlayAtkUntilTurnEndPower(owner,source,amount);}}


//Next turn: On play Atk: Apply 2 Poison to foes