package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.SELF;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;

import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.makeInDeck;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.pivotDP;import spireMapOverhaul.SpireAnniversary6Mod;
public class TheSunSoFarAway extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(
				TheSunSoFarAway.class.getSimpleName());
public TheSunSoFarAway(){super(ID,1,SKILL,SELF,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);
	baseMagicNumber=magicNumber=0;}
@Override public void use(AbstractPlayer p,AbstractMonster m){
	//Shuffle 5 cards in discard pile into deck. Fascinated: Add "Escape Earth" to bottom of deck
	pivotDP(5);if(fasc()){makeInDeck(new EscapeEarth(),1,2);}
}
@Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}






