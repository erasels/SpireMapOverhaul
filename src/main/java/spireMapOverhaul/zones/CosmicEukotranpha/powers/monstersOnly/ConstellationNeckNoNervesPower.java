package spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnExhaustPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
public class ConstellationNeckNoNervesPower extends BasePower implements CloneablePowerInterface,BetterOnExhaustPower{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(ConstellationNeckNoNervesPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;
    public ConstellationNeckNoNervesPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=amount;updateDescription();}
    @Override public void betterOnExhaust(CardGroup cardGroup,AbstractCard abstractCard){blck(amount);}
    @Override public void onPlayCard(AbstractCard card,AbstractMonster m){if(card.type==AbstractCard.CardType.ATTACK&&card.costForTurn>0){poO(new RegenPower(owner,amount));}}
    @Override public void updateDescription(){description=amount+DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];}
    @Override public AbstractPower makeCopy(){return new ConstellationNeckNoNervesPower(owner,source,amount);}}



//3 B on Exhaust card. Gain 3 Regen when 1 cost or higher atk is played