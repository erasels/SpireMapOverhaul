package spireMapOverhaul.zones.humidity.cards.powerelic.implementation;

import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoCompendium;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;


@NoCompendium
public class PowerelicTemporaryDuplicateCard extends PowerelicCard {
    public static final String ID = makeID(PowerelicTemporaryDuplicateCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public PowerelicTemporaryDuplicateCard() {
        super(ID, -2, CardType.POWER, CardRarity.SPECIAL, AbstractCard.CardTarget.SELF);
        rawDescription = cardStrings.DESCRIPTION;
    }

    public PowerelicTemporaryDuplicateCard(AbstractRelic temporaryDuplicateRelic) {
        super(ID, -2, CardType.POWER, CardRarity.SPECIAL, AbstractCard.CardTarget.SELF);
        rawDescription = cardStrings.DESCRIPTION;
        this.setRelicInfoForCopiedCard(temporaryDuplicateRelic);
    }

    //since this card overrides a class that already defines makeCopy, we need to override it again so the game saves/loads properly
    @Override
    public AbstractCard makeCopy() {
        return new PowerelicTemporaryDuplicateCard(capturedRelic);
    }
}
