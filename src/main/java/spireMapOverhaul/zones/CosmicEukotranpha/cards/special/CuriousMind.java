package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.CuriosityPower;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.RandomSelectionAddCardToHandAndOthersToDeckEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.SELF;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;

import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;import spireMapOverhaul.SpireAnniversary6Mod;
public class CuriousMind extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(CuriousMind.class.getSimpleName());
    public CuriousMind(){super(ID,1,SKILL,SELF,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);
        setCostUpgrade(0);}
    //public CuriousMind(){super(SpireAnniversary6Mod.makeID("IBroughtSolarFlares"),cardPath("skill/default.png"),1,CardType.SKILL,TheDefault.Enums.EEHUVDE,CardRarity.SPECIAL,CardTarget.SELF);}
    @Override public void use(AbstractPlayer p,AbstractMonster m){
        //Gain 2 Curiosity. Choose 1 of 3 cards to add to hand and add the other 2 to deck
        poS(new CuriosityPower(AbstractDungeon.player,2));
        atb(new RandomSelectionAddCardToHandAndOthersToDeckEffect(3));
    }
    @Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}