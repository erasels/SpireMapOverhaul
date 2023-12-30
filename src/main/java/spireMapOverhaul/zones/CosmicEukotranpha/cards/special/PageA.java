package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.choice.Pages.GaucherieChoice;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.choice.Pages.GranularityChoice;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.choice.Pages.GrapnelChoice;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.choice.Pages.GraupelChoice;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.SELF;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.choiceAP;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.draw;import spireMapOverhaul.SpireAnniversary6Mod;
public class PageA extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(PageA.class.getSimpleName());
    public PageA(){super(ID,1,SKILL,SELF,SPECIAL,COLORLESS);upgradeBaseCost(0);}
    @Override public void use(AbstractPlayer p,AbstractMonster m){
        //Page A
        choiceAP(new AbstractCard[]{new GaucherieChoice(),new GranularityChoice(),new GrapnelChoice(),new GraupelChoice()});draw(1);
    }
    @Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}



