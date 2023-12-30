package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.TakeDamageAtNextTurnEndPower;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.ENEMY;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.STATUS;

import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;import spireMapOverhaul.SpireAnniversary6Mod;
public class SunFromSteel extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(SunFromSteel.class.getSimpleName());
    public SunFromSteel(){super(ID,1,STATUS,ENEMY,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);
        baseMagicNumber=magicNumber=0;}
    @Override public void use(AbstractPlayer p,AbstractMonster m){
        //Heal 8 hp. After this has been drawn 3 times: Exhaust. [] When Exhausted: Take 40 D at next turn end [] Turn end in hand: Send this to deck
        //8 40>11 50
        atb(new HealAction(AbstractDungeon.player,AbstractDungeon.player,8+rBAI(upgraded,3)));
    }
    @Override public void triggerWhenDrawn(){magicNumber++;if(magicNumber>2){exhaustT(this);}}
    @Override public void triggerOnExhaust(){poS(new TakeDamageAtNextTurnEndPower(AbstractDungeon.player,AbstractDungeon.player,40+rBAI(upgraded,10)));}
    @Override public void triggerOnEndOfPlayerTurn(){pivotT(this);}
    @Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}






