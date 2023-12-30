package spireMapOverhaul.zones.CosmicEukotranpha.cards.choice.Pages;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.SELF;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;import spireMapOverhaul.SpireAnniversary6Mod;
public class NoyadeChoice extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(NoyadeChoice.class.getSimpleName());
    public NoyadeChoice(){super(ID,-2,SKILL,SELF,SPECIAL,COLORLESS);}
    @Override public void use(AbstractPlayer p,AbstractMonster m){
        //After playing four cards in a turn, Discard pile is bigger than draw pile
    }
    @Override public void onChoseThisOption(){}
    @Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}

