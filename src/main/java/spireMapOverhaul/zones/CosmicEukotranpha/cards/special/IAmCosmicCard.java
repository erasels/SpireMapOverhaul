package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.SELF;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;
import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;
public class IAmCosmicCard extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(
				EscapeEarth.class.getSimpleName());
public IAmCosmicCard(){super(ID,1,SKILL,SELF,SPECIAL,COLORLESS);
	baseMagicNumber=magicNumber=0;this.tags.add(CosmicZoneCosmicCard);
}
@Override public void use(AbstractPlayer p,AbstractMonster m){
	//Play 1 Cosmic card in deck, Gain 2 Strength if Power was chosen, Gain 1 energy if card costed 0 or 1
	//TODO: Play Cosmic effect
	
}
@Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}






