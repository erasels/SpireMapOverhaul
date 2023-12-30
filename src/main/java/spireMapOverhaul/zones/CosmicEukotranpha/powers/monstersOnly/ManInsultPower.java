package spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.Man;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.BasePower;

public class ManInsultPower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID= SpireAnniversary6Mod.makeID(ManInsultPower.class.getSimpleName());private static final PowerStrings powerStrings= CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    public int qqq=0;public int insult=0;public int cardsInARow=0;public int cardsThisTurn=0;
    public ManInsultPower(AbstractCreature o,AbstractCreature s,int insult){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.insult=insult;this.amount=-1;updateDescription();}
    @Override public void onPlayCard(AbstractCard card,AbstractMonster m){if(insult==0&&card.type==AbstractCard.CardType.SKILL){cardsInARow++;if(cardsInARow==3){remThis();}}else{cardsInARow=0;}
        if(insult==2&&m.getIntentBaseDmg()>0&&card.type==AbstractCard.CardType.ATTACK&&card.damage>m.getIntentDmg()){remThis();}updateDescription();}
    @Override public void onAfterCardPlayed(AbstractCard usedCard){cardsThisTurn++;if(insult==3&&cardsThisTurn==4&&p.discardPile.size()>p.drawPile.size()){remThis();}updateDescription();}
    @Override public void atEndOfTurn(boolean isPlayer){if(insult==1&&p.hand.size()==1){remThis();}cardsThisTurn=0;updateDescription();}
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type){updateDescription();return damage;}
    @Override public void onRemove(){if(owner instanceof Man){((Man)owner).fulfillMeaning(insult);}}
    @Override public void updateDescription(){if(owner.currentBlock>0){description=DESCRIPTIONS[4];}else{description=DESCRIPTIONS[insult];}}
    @Override public AbstractPower makeCopy(){return new ManInsultPower(owner,source,insult);}}

//"You struggle to remember what she said while she has Block"
//
// Insult is shown while Man has no Block, Otherwise it is hidden


//"You're so forgettable I'm sure more of my friends know of the word [Graupel] than they do of you"
//Graupel
//Play three one cost Skills in a row

//"Icky [Oddment]. Do you not know what that means? Then use context clues you vitamin deficient noodle armed minotaur"
//Oddment
//At turn end, Hand contains one card (Before discarding due to game rules)

//"Your shocking [Puissant] can't even be compared to the likes of The Wyvernslayer, As even when multiplied, zero stays the same"
//Puissant
//While an enemy is attacking, Play an attack whose attack damage is more than the enemy's intent

//"I'll give you a free win, As long as you agree to a [Noyade]. Lol, No I won't tell you what it means"
//Noyade
//After playing four cards in a turn, Discard pile is bigger than draw pile