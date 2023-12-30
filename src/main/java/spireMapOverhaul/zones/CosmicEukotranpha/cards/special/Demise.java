package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.SELF;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.POWER;
import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;

import spireMapOverhaul.SpireAnniversary6Mod;
public class Demise extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(Demise.class.getSimpleName());
    public Demise(){super(ID,1,POWER,SELF,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);}
    @Override public void use(AbstractPlayer p,AbstractMonster m){
        //Demise: Power. Choose 1 of 3 effects
        //If You Have a Dream
        //Kismet
        //Innocent Ignorance
    }
    @Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}

