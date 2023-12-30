package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.ENEMY;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.POWER;
import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;

public class VoidHallucination extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(VoidHallucination.class.getSimpleName());
    public VoidHallucination(){super(ID,1,POWER,ENEMY,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);}
    @Override public void use(AbstractPlayer p,AbstractMonster m){
        //Power. Choose, [] [] []. When drawn: Lose 1 energy
    }
    @Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}


