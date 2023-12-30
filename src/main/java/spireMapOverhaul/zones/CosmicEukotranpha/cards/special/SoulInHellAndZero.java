package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.ENEMY;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;
import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;

import spireMapOverhaul.SpireAnniversary6Mod;

public class SoulInHellAndZero extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(SoulInHellAndZero.class.getSimpleName());
    public SoulInHellAndZero(){super(ID,1,SKILL,ENEMY,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);}
    @Override public void use(AbstractPlayer p,AbstractMonster m){
        //Spend Str of foe to do things. 5 B when drawn
        //\[1: 10 B] \[5: Draw 2] \[10: Gain 1 Str, Dex] \[20: Exhaust any in top 8 of deck] \[40: Apply 10 Curiosity, 5 Ritual instead if foe used Hysteric Surge this combat]
    }
    @Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}


