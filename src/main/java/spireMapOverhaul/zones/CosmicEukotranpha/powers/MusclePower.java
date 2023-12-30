package spireMapOverhaul.zones.CosmicEukotranpha.powers;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.draw;
public class MusclePower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID(MusclePower.class.getSimpleName());private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;public int atks;public int atksThisTurn;public int draa;
    public MusclePower(AbstractCreature o,AbstractCreature s,int amount){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=true;this.isTurnBased=false;if(amount>0){type=PowerType.BUFF;}else{type=PowerType.DEBUFF;}this.amount=amount;updateDescription();}
    public float atDamageGive(float damage,DamageInfo.DamageType type){return type==DamageInfo.DamageType.NORMAL?damage+(float)this.amount:damage;}
    public float modifyBlock(float blockAmount){if(owner!=p){return blockAmount;}/*I'm quite sure I don't need this but just in case*/return (blockAmount+=(float)this.amount)<0.0F?0.0F:blockAmount;}
    @Override public void onPlayCard(AbstractCard card,AbstractMonster m){atksThisTurn++;if(atksThisTurn==atks){draw(draa);atksThisTurn=0;}}
    @Override public void atEndOfTurn(boolean isPlayer){atksThisTurn=0;}
    @Override public void updateDescription(){description=DESCRIPTIONS[0]+amount;if(owner==p){description+=DESCRIPTIONS[1]+amount+DESCRIPTIONS[2];}}
    @Override public AbstractPower makeCopy(){return new MusclePower(owner,source,amount);}}

//Increase attack D by 1 & Increase B from cards by 1 if player