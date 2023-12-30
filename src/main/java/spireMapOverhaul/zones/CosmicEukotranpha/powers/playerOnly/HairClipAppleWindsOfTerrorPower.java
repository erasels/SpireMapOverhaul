package spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;


public class HairClipAppleWindsOfTerrorPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(HairClipAppleWindsOfTerrorPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;public int amii=0;
    public HairClipAppleWindsOfTerrorPower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=amount;updateDescription();}
    public void onUseCard(AbstractCard card,UseCardAction action){if(!card.purgeOnUse&&card.type==AbstractCard.CardType.SKILL&&amount>0){
        amii++;if(amii>=3){flashWithoutSound();amii=0;AbstractMonster m=null;if(action.target!=null){m=(AbstractMonster)action.target;}
            AbstractCard tmp=card.makeSameInstanceOf();AbstractCard tmp0=card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x=card.current_x;tmp.current_y=card.current_y;
            tmp.target_x=(float)Settings.WIDTH/2.0F-300.0F*Settings.scale;tmp.target_y=(float)Settings.HEIGHT/2.0F;
            if(m!=null){tmp.calculateCardDamage(m);}tmp.purgeOnUse=true;
            AbstractDungeon.player.limbo.addToBottom(tmp0);
            tmp0.current_x=card.current_x;tmp0.current_y=card.current_y;
            tmp0.target_x=(float)Settings.WIDTH/2.0F-300.0F*Settings.scale;tmp0.target_y=(float)Settings.HEIGHT/2.0F;
            if(m!=null){tmp0.calculateCardDamage(m);}tmp0.purgeOnUse=true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp,m,card.energyOnUse,true,true),true);
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp0,m,card.energyOnUse,true,true),true);
            reducePower(1);
        }}updateDescription();}
    @Override public void atEndOfTurn(boolean isPlayer){remThis();}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+amii+DESCRIPTIONS[1];}
    @Override public AbstractPower makeCopy(){return new HairClipAppleWindsOfTerrorPower(owner,source,amount);}}
//Lost at turn end. On play 3 Skls: Play it 3 times. Lose 1 of this