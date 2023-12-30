package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.RandomSelectCardCostZeroToDeckEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.SolarFlaresLookAmazingEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.SELF;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;

import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.att;import spireMapOverhaul.SpireAnniversary6Mod;
public class IBroughtSolarFlares extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(IBroughtSolarFlares.class.getSimpleName());
    public IBroughtSolarFlares(){super(ID,1,SKILL,SELF,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);}
    @Override public void use(AbstractPlayer p,AbstractMonster m){
        //1 cost. Gain 3 Strength. Choose 1 of 3 random cards to add to your deck, It costs 0. Savent Stars will love this, Be careful, or not
        poS(new StrengthPower(AbstractDungeon.player,3));
        atb(new RandomSelectCardCostZeroToDeckEffect(3));
        att(new SolarFlaresLookAmazingEffect());
    }
    @Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}



