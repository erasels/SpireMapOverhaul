package spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.draw;
public class DrawOnPlayAtksInATurnPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(DrawOnPlayAtksInATurnPower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;public int atks;public int atksThisTurn;public int draa;
    public DrawOnPlayAtksInATurnPower(AbstractCreature o,AbstractCreature s,int atks,int draw){super(POWER_ID,true);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=-1;this.atks=atks;this.atksThisTurn=0;this.draa=draw;updateDescription();}
    @Override public void onPlayCard(AbstractCard card,AbstractMonster m){atksThisTurn++;if(atksThisTurn==atks){draw(draa);atksThisTurn=0;}updateDescription();}
    @Override public void atEndOfTurn(boolean isPlayer){atksThisTurn=0;}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+draa+DESCRIPTIONS[1]+atks+DESCRIPTIONS[2]+atksThisTurn+DESCRIPTIONS[3]+atks+DESCRIPTIONS[4];}
    @Override public AbstractPower makeCopy(){return new DrawOnPlayAtksInATurnPower(owner,source,atks,draa);}}


//"DESCRIPTIONS":["Draw "," after playing "," Atks in a turn (","/",")"]},