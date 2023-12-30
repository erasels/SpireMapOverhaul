package spireMapOverhaul.zones.CosmicEukotranpha.powers.frameworks;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;
import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;

public class AddCardToHandAtTurnStartPostDrawPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(AddCardToHandAtTurnStartPostDrawPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;public AbstractCard card;public String des;public String poPath;public int turns;public int number;
    public AddCardToHandAtTurnStartPostDrawPower(AbstractCreature owner,AbstractCreature source,AbstractCard card,String description,String poPath){
        this(owner,source,card,description,poPath,-1,1);}
    public AddCardToHandAtTurnStartPostDrawPower(AbstractCreature o,AbstractCreature s,AbstractCard card,String d,String poPath,int turns,int number){super(POWER_ID,true);this.owner=o;this.source=s;
        this.card=card;this.des=d;this.poPath=poPath;this.type=PowerType.BUFF;
        this.canGoNegative=true;this.isTurnBased=false;this.turns=turns;this.number=number;this.amount=-1;updateDescription();}
    @Override public void atStartOfTurnPostDraw(){if(turns!=-1){turns--;if(turns<1){remThis();}}addToBot(new MakeTempCardInHandAction(card,number));updateDescription();}
    @Override public void updateDescription(){if(turns==-1){description=DESCRIPTIONS[0]+card.name+DESCRIPTIONS[1];
        }else{description=DESCRIPTIONS[0]+card.name+DESCRIPTIONS[2]+turns+DESCRIPTIONS[3];}}
    @Override public AbstractPower makeCopy(){return new AddCardToHandAtTurnStartPostDrawPower(owner,source,card,des,poPath,turns,number);}}

